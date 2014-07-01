package com.matrix.visitingcard.http.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.user.User;

public class Parser {

	/**
	 * { "is_new_user": false, "user": { "id": 4, "name": "Michel Vin",
	 * "created_at": "2014-06-03T18:29:19.354Z", "updated_at":
	 * "2014-06-04T16:30:20.772Z", "email": "testing98459845@gmail.com" } }
	 * 
	 * @param content
	 * @return
	 */
	public static void parseSocialLogin(byte[] content) {

		if (content == null || content.length == 0) {
			VLogger.e("content is null");
			return;
		}

		User user = User.getInstance();

		try {
			JSONObject jsonObject = new JSONObject(new String(content));

			user.setNewUser(jsonObject.getBoolean("is_new_user"));

			jsonObject = jsonObject.getJSONObject("user");// reusing same
															// variable :P
			user.setId(jsonObject.getString("id"));
			user.setName(jsonObject.getString("name"));
			user.setCreatedAt(jsonObject.getString("created_at"));
			user.setUpdatedAt(jsonObject.getString("updated_at"));
			user.setEmail(jsonObject.getString("email"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
