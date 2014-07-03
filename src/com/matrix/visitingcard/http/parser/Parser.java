package com.matrix.visitingcard.http.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.matrix.visitingcard.http.response.VCTResponse;
import com.matrix.visitingcard.http.response.VCTResponse.KeysAndTypes;
import com.matrix.visitingcard.http.response.VCTResponse.KeysType;
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

	public static void parseVCT(byte[] content) {

		if (content == null || content.length == 0) {
			VLogger.e("content is null");
			return;
		}

		ArrayList<VCTResponse> vcts = VCTResponse.getAllVCT();

		try {
			JSONArray jsonArray = new JSONArray(new String(content));

			for (int i=0; i < jsonArray.length(); i++) {
				VCTResponse vct = new VCTResponse();
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				vct.setId(jsonObject.getInt("id"));
				vct.setName(jsonObject.getString("name"));

				JSONObject jsonSampleUrls = jsonObject
						.getJSONObject("sample_urls");
				VCTResponse.SampleUrls sampleUrls = vct.new SampleUrls();

				sampleUrls.setOriginal(jsonSampleUrls.getString("original"));
				sampleUrls.setThumb(jsonSampleUrls.getString("thumb"));
				sampleUrls.setMedium(jsonSampleUrls.getString("medium"));

				vct.setSampleUrls(sampleUrls);

				JSONArray jsonKeysAndTypes = jsonObject
						.getJSONArray("keys_and_types");
				ArrayList<VCTResponse.KeysAndTypes> keysAndTypes = new ArrayList<VCTResponse.KeysAndTypes>();

				for (int j = 0; j < jsonKeysAndTypes.length(); j++) {
					JSONObject jsonKeyAndType = jsonKeysAndTypes
							.getJSONObject(j);
					String typeString = jsonKeyAndType.getString("type");
					String key = jsonKeyAndType.getString("key");

					KeysAndTypes keyAndType = vct.new KeysAndTypes();

					KeysType type = KeysType.TEXT;

					if (typeString.equalsIgnoreCase(KeysType.TEXTAREA.name())) {
						type = KeysType.TEXTAREA;
					} else if (typeString.equalsIgnoreCase(KeysType.IMAGE
							.name())) {
						type = KeysType.IMAGE;
					}
					keyAndType.setType(type);
					keyAndType.setKey(key);
					keysAndTypes.add(keyAndType);

				}

				vct.setKeysAndTypes(keysAndTypes);

				vcts.add(vct);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
