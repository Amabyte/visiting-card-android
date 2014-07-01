package com.matrix.visitingcard;

import java.io.IOException;

import org.apache.http.Header;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.AsyncH;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.parser.AsyncParser;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.request.SocialLoginRequest;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.user.User;

public class SignUpFormActivity extends Activity {

	private AccountManager mAccountManager;
	private Spinner mSpinner;
	protected String mAccountName;
	public ProgressDialog progressDialog;
	private AsyncH mAsyncHttp;
	private AsyncParser mParser;
	private static final int REQ_SIGN_IN_REQUIRED = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_form);
		mAccountManager = AccountManager.get(this);
		initialize();
		initializeViews();
		setAccountSpinner();
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		mParser = new AsyncParser();
	}

	private void initializeViews() {
		findViewById(R.id.buttonLogin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mAccountName = (String) mSpinner.getSelectedItem();
						new RetrieveTokenTask().execute(mAccountName);
					}
				});
		mSpinner = (Spinner) findViewById(R.id.spinnerAccount);

	}

	private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SignUpFormActivity.this);
			progressDialog.setTitle("Login");
			progressDialog.setMessage("Google Login");
			progressDialog.setCancelable(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String accountName = params[0];
			String scopes = "oauth2:profile email";
			String token = null;
			try {
				token = GoogleAuthUtil.getToken(getApplicationContext(),
						accountName, scopes);

				// Log.e("token", "Token:" + token);
			} catch (IOException e) {
				VLogger.e(e.getMessage());
			} catch (UserRecoverableAuthException e) {
				startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
			} catch (GoogleAuthException e) {
				VLogger.e(e.getMessage());
			}
			return token;
		}

		@Override
		protected void onPostExecute(String token) {
			super.onPostExecute(token);
			VLogger.d("token recieved " + token);
			if (token == null) {
				VLogger.e("Unable to login please retry");
				Toast.makeText(SignUpFormActivity.this,
						"Unable to login please retry", Toast.LENGTH_LONG)
						.show();
				return;
			}
			loginToVisitingCardServer(token);

		}
	}

	private void setAccountSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getAccountNames());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
	}

	private String[] getAccountNames() {

		mAccountManager = AccountManager.get(this);
		Account[] accounts = mAccountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}

	public void loginToVisitingCardServer(String token) {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"social_login", "url.properties");

		SocialLoginRequest param = new SocialLoginRequest("google_oauth2",
				token);

		ARHandlerSocialLogin handler = new ARHandlerSocialLogin();

		mAsyncHttp.communicate(connectionProperties, null, param, handler);
	}

	class ARHandlerSocialLogin extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.d("ConnectionSuccessful, status code " + statusCode
					+ "content "
					+ (content == null ? "null" : new String(content)));
			progressDialog.dismiss();

			Parser.parseSocialLogin(content);// Saves data to user singelton

			VLogger.e(User.getInstance().getEmail());

		}

		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] response,
				Throwable arg3) {
			VLogger.e("Connection Failed, status code " + statusCode
					+ " response "
					+ (response == null ? "null" : new String(response)));

			Toast.makeText(SignUpFormActivity.this,
					"Unable to login please retry", Toast.LENGTH_LONG).show();
			progressDialog.dismiss();
		}

	}

}
