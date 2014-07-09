package com.matrix.visitingcard.http;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ProgressJsonHttpResponseHandler extends JsonHttpResponseHandler {
	ProgressJSONResponseCallBack callBack;
	ProgressDialog dialog;

	public ProgressJsonHttpResponseHandler(Activity activity,
			ProgressJSONResponseCallBack callBack) {
		this.callBack = callBack;
		dialog = new ProgressDialog(activity);
	}

	@Override
	public void onStart() {
		dialog.setMessage("Please wait...");
		dialog.setCancelable(false);
		dialog.show();
		callBack.onAsyncStart();
		super.onStart();
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		super.onSuccess(statusCode, headers, response);
		callBack.onAsyncSuccess(response);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		super.onSuccess(statusCode, headers, response);
		callBack.onAsyncSuccess(response);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			String responseString, Throwable throwable) {
		super.onFailure(statusCode, headers, responseString, throwable);
		callBack.onAsyncFailure(statusCode, responseString);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, JSONObject errorResponse) {
		super.onFailure(statusCode, headers, throwable, errorResponse);
		callBack.onAsyncFailure(statusCode, errorResponse);
	}

	@Override
	public void onFinish() {
		super.onFinish();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		callBack.onAsyncFinish();
	}
}
