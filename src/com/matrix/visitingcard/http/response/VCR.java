package com.matrix.visitingcard.http.response;

import org.json.JSONException;
import org.json.JSONObject;

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

}
