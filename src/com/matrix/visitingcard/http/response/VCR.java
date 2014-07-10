package com.matrix.visitingcard.http.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.ListMyVCRActivity;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.ProgressJSONResponseCallBack;
import com.matrix.visitingcard.http.ProgressJsonHttpResponseHandler;
import com.matrix.visitingcard.http.request.AcceptVCRResquest;

public class VCR {
	private long id;
	private String userName, toUserName, message;

	public VCR(long id, String userName, String toUserName, String message) {
		this.id = id;
		this.userName = userName;
		this.toUserName = toUserName;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public String getToUserName() {
		return toUserName;
	}

	public String getUserName() {
		return userName;
	}

	public static VCR parse(JSONObject jsonObject) throws JSONException {
		long id = jsonObject.getLong("id");
		String message = jsonObject.getString("message");
		String userName = jsonObject.getJSONObject("user").getString("name");
		String toUserName = jsonObject.getJSONObject("to_user").getString(
				"name");
		if (message.equals("null"))
			message = null;
		return new VCR(id, userName, toUserName, message);
	}

	public void decline(Activity activity) {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				activity, "base", "url.properties");
		connectionProperties.method = "DELETE";
		connectionProperties.baseURL += (String.format(
				"/visiting_card_requests/my/%s/decline.json", this.getId()));

		final ListMyVCRActivity vcrActivity = (ListMyVCRActivity) activity;

		vcrActivity.getAsyncHttp().addHeader(
				"Cookie",
				vcrActivity.getSp().getSharedPrefsValueString(
						Constants.SP.SESSION_ID, null));

		vcrActivity.getAsyncHttp().communicate(
				connectionProperties,
				null,
				null,
				new ProgressJsonHttpResponseHandler(activity,
						new ProgressJSONResponseCallBack() {

							@Override
							public void onAsyncSuccess(JSONObject jsonObject) {
								Toast.makeText(vcrActivity, "VCR declined",
										Toast.LENGTH_SHORT).show();
								vcrActivity.reloadUi();
							}

							@Override
							public void onAsyncSuccess(JSONArray jsonArray) {
							}

							@Override
							public void onAsyncStart() {
							}

							@Override
							public void onAsyncFinish() {
							}

							@Override
							public void onAsyncFailure(int status, String string) {
								Toast.makeText(vcrActivity,
										"Error : " + status, Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onAsyncFailure(int status,
									JSONObject jsonObject) {
								onAsyncFailure(status, jsonObject.toString());
							}
						}));
	}

	public void accept(Activity activity, int vcId) {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				activity, "base", "url.properties");
		connectionProperties.method = "POST";
		connectionProperties.baseURL += (String.format(
				"/visiting_card_requests/my/%s/accept.json", this.getId()));

		final ListMyVCRActivity vcrActivity = (ListMyVCRActivity) activity;

		vcrActivity.getAsyncHttp().addHeader(
				"Cookie",
				vcrActivity.getSp().getSharedPrefsValueString(
						Constants.SP.SESSION_ID, null));
		AcceptVCRResquest params = new AcceptVCRResquest(vcId);

		vcrActivity.getAsyncHttp().communicate(
				connectionProperties,
				null,
				params,
				new ProgressJsonHttpResponseHandler(activity,
						new ProgressJSONResponseCallBack() {

							@Override
							public void onAsyncSuccess(JSONObject jsonObject) {
								Toast.makeText(vcrActivity, "VCR Accepted",
										Toast.LENGTH_SHORT).show();
								vcrActivity.reloadUi();
							}

							@Override
							public void onAsyncSuccess(JSONArray jsonArray) {
							}

							@Override
							public void onAsyncStart() {
							}

							@Override
							public void onAsyncFinish() {
							}

							@Override
							public void onAsyncFailure(int status, String string) {
								Toast.makeText(vcrActivity,
										"Error : " + status, Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onAsyncFailure(int status,
									JSONObject jsonObject) {
								onAsyncFailure(status, jsonObject.toString());
							}
						}));
	}
}
