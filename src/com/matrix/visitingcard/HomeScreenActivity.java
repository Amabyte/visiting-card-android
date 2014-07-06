package com.matrix.visitingcard;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.ListMyVCActivity.ARHandlerGetMyVC;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.MyVC;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeScreenActivity extends Activity implements OnClickListener {

	private AsyncHttp mAsyncHttp;
	private SharedPrefs sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		initialize();
		initializeViews();
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(this);
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void initializeViews() {

		findViewById(R.id.bCreateNewVC).setOnClickListener(this);
		findViewById(R.id.bViewMyVC).setOnClickListener(this);
		findViewById(R.id.bSignout).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bCreateNewVC:
			startActivity(new Intent(HomeScreenActivity.this,
					ListOfVCTActivity.class));
			break;
		case R.id.bViewMyVC:
			startActivity(new Intent(HomeScreenActivity.this,
					ListMyVCActivity.class));
			break;
		case R.id.bSignout:
			signout();
			break;
		}
	}

	private void signout() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"sign_out", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		ARHandlerSignout handler = new ARHandlerSignout();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);

	}

	class ARHandlerSignout extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.e("ConnectionSuccessful, status code " + statusCode
					+ "content "
					+ (content == null ? "null" : new String(content)));
			sp.destroy();
			//TODO : clear GCM shit

		}

		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] response,
				Throwable arg3) {
			VLogger.e("Connection Failed, status code " + statusCode
					+ " response "
					+ (response == null ? "null" : new String(response)));

		}

	}

}
