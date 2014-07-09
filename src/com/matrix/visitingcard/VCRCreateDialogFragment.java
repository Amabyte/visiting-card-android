package com.matrix.visitingcard;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

public class VCRCreateDialogFragment extends DialogFragment implements
		OnClickListener {

	private EditText emailEditText, messsageEditText;
	private Button createVCRButton;
	private AsyncHttp mAsyncHttp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, STYLE_NORMAL);
		View rootView = inflater.inflate(R.layout.fragment_create_vcr,
				container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		emailEditText = (EditText) rootView.findViewById(R.id.etEmail);
		messsageEditText = (EditText) rootView.findViewById(R.id.etMessage);
		createVCRButton = (Button) rootView.findViewById(R.id.bCreateVCR);
		createVCRButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bCreateVCR:
			createVCR();
			break;
		}
	}

	private void createVCR() {
		String email = emailEditText.getText().toString().trim();
		String message = messsageEditText.getText().toString().trim();
		if (email == null || email.equals("")) {
			emailEditText.setError("Emial required");
			return;
		}
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				getActivity(), "create_vcr", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(getActivity())
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		RequestParams params = new RequestParams();
		params.put("email", email);
		if (message == null || message.equals(""))
			params.put("message", message);
		VLogger.e(params.toString());
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		mAsyncHttp.generatePostRequestTemperoryMethod(
				connectionProperties.baseURL, null, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onStart() {
						dialog.setMessage("Please wait...");
						dialog.setCancelable(false);
						dialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						Toast.makeText(getActivity(), "VCR created",
								Toast.LENGTH_SHORT).show();
						dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
						Toast.makeText(getActivity(), "Error : " + statusCode,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
	}

	@Override
	public void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}
}