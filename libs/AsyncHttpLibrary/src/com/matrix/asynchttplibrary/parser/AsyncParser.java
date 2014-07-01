package com.matrix.asynchttplibrary.parser;

import com.google.gson.Gson;
import com.matrix.asynchttplibrary.logger.ALogger;
import com.matrix.asynchttplibrary.response.AsyncResponseBody;

/**
 * 
 * @author Yajnesh T
 * @description Parses String / JSON/ JSONArray using GSON
 */
public class AsyncParser {
	//
	// public static enum ParserType {
	// AUTODETECT, JSON, XML, STRING, UNIDENTIFIED
	// }

	// public Object parseResponse(byte[] content, AsyncResponseBody
	// responseModel,
	// ParserType parseType) {
	//
	// switch (parseType) {
	// case STRING:
	// return parseString(content);
	// case JSON:
	// return parseJSON(content, responseModel);
	// default:
	// ALogger.e("Parser not implemented for " + parseType.toString());
	// break;
	// }
	//
	// return null;
	// }
	/**
	 * Return Object populated with JSON data
	 * 
	 * @param content
	 *            : JSON data
	 * @param responseModel
	 *            : object to be populated
	 * @return populated object
	 */
	public AsyncResponseBody parseJSON(byte[] content,
			AsyncResponseBody responseModel) {

		String jsonResponse = new String(content);

		if (!jsonResponse.equalsIgnoreCase("[]")) {

			try {
				Gson gson = new Gson();

				ALogger.v(jsonResponse);

				responseModel = gson.fromJson(jsonResponse,
						responseModel.getClass());
			} catch (Exception e) {
				ALogger.e(e.getLocalizedMessage());
				e.printStackTrace();
				return null;
			}
		}

		return responseModel;
	}

	/**
	 * Return Object array populated with JSON data
	 * 
	 * @param content
	 *            : JSON data
	 * @param responseModel
	 *            : object to be populated
	 * @return populated array of object
	 */
	public AsyncResponseBody[] parseJSONArray(byte[] content,
			AsyncResponseBody[] responseModel) {
		String jsonResponse = new String(content);

		if (!jsonResponse.equalsIgnoreCase("[]")) {

			try {
				Gson gson = new Gson();

				ALogger.v(jsonResponse);

				responseModel = gson.fromJson(jsonResponse,
						responseModel.getClass());
			} catch (Exception e) {
				ALogger.e(e.getLocalizedMessage());
				e.printStackTrace();
				return null;
			}
		}

		return responseModel;

	}

	/**
	 * Try to convert response to String
	 * 
	 * @param content
	 *            response data
	 * @return String form of response data
	 */
	public String parseString(byte[] content) {
		return new String(content);
	}
}
