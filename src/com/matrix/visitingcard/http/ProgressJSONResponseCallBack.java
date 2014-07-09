package com.matrix.visitingcard.http;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ProgressJSONResponseCallBack {
	public void onAsyncStart();

	public void onAsyncFinish();

	public void onAsyncSuccess(JSONArray jsonArray);

	public void onAsyncSuccess(JSONObject jsonObject);

	public void onAsyncFailure(int status, String string);

	public void onAsyncFailure(int status, JSONObject jsonObject);
}
