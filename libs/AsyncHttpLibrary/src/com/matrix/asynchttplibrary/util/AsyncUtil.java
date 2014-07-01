package com.matrix.asynchttplibrary.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Entity;
import android.util.SparseIntArray;

import com.loopj.android.http.RequestParams;
import com.matrix.asynchttplibrary.annotation.AsyncHAnnotation;
import com.matrix.asynchttplibrary.annotation.AsyncHIgnoreParam;
import com.matrix.asynchttplibrary.logger.ALogger;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.request.AsyncRequestParam;
import com.matrix.asynchttplibrary.request.AsyncRequestParam.Type;

public class AsyncUtil {

	public static Object processParams(AsyncRequestParam param,
			boolean shouldEncodeUrl) {
		HashMap<String, String> modelMap = AsyncUtil.createHashMapFromModel(
				param, false);

		Set<Entry<String, String>> set = modelMap.entrySet();

		Iterator<Entry<String, String>> iterator = set.iterator();

		Object rParams = null;

		switch (param.getRequestType()) {
		case DEFAULT:
			rParams = new RequestParams();
			break;
		case JSON:
			rParams = new JSONObject();
			break;
		default:
			ALogger.e("Unable to process Params");
			return null;
		}

		while ((iterator.hasNext())) {
			Entry<String, String> data = iterator.next();
			insert(param.getRequestType(), rParams, data.getKey(),
					data.getValue(), shouldEncodeUrl);

		}

		return rParams;
	}

	private static void insert(Type requestType, Object rjParam, String key,
			String value, boolean shouldEncodeUrl) {

		switch (requestType) {
		case DEFAULT:

			if (value != null && shouldEncodeUrl) {
				try {
					((RequestParams) rjParam).put(key,
							URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				((RequestParams) rjParam).put(key, value);
			}
			break;
		case JSON:
			try {
				((JSONObject) rjParam).put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		default:

		}

		ALogger.v("Params: " + key + " " + value + " type: "
				+ requestType.name());

	}

	public static HashMap<String, String> createHashMapFromModel(Object model,
			boolean excludeNull) {

		HashMap<String, String> map = new HashMap<String, String>();

		Field fields[] = model.getClass().getFields();

		String key, value = null;

		for (int i = 0; i < fields.length; ++i) {

			try {
				value = null;
				key = fields[i].getName();

				// System.out.println(fields[i].getType().getCanonicalName());

				if (fields[i].getType().isPrimitive()) {
					value = String.valueOf(fields[i].get(model));
				} else {
					if (fields[i].getType().getCanonicalName()
							.compareToIgnoreCase("java.lang.String") == 0) {
						value = (String) fields[i].get(model);
					} else if (fields[i].getType().getCanonicalName()
							.equalsIgnoreCase("android.util.SparseIntArray")) {
						// value=String.valueOf(fields[i].get(model));
						SparseIntArray temp = (SparseIntArray) fields[i]
								.get(model);
						if (temp != null) {
							for (int j = 0; j < temp.size(); j++) {
								String tempKey = "" + key + "[" + j + "][0]";
								String tempvalue = String
										.valueOf(temp.keyAt(j));
								map.put(tempKey, tempvalue);
								tempKey = "" + key + "[" + j + "][1]";
								tempvalue = String.valueOf(temp.valueAt(j));
								map.put(tempKey, tempvalue);
							}
						}
						// System.out.println(fields[i].getType().getCanonicalName()+":::value"+key+"::"+value);
						value = null;
					}

					else {
						System.out.println("currently not supported");
					}
				}
				if (fields[i].isAnnotationPresent(AsyncHAnnotation.class)) {
					AsyncHAnnotation annots = ((AsyncHAnnotation) fields[i]
							.getDeclaredAnnotations()[0]);
					key = annots.prefix() + key;
					key = key + annots.postfix();

					if (annots.overrideName() != null
							&& annots.overrideName().length() > 0) {
						key = annots.overrideName();
					}

				}
				boolean ignoreField = false;

				if (fields[i].isAnnotationPresent(AsyncHIgnoreParam.class)) {
					AsyncHIgnoreParam annots = ((AsyncHIgnoreParam) fields[i]
							.getDeclaredAnnotations()[0]);
					if (annots.ignore().equalsIgnoreCase("true")) {
						ignoreField = true;
					} else {
						ignoreField = false;
					}

				}

				if (!ignoreField) {
					if (excludeNull) {
						if (value != null)
							map.put(key, value);
					} else {
						map.put(key, value);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	private static Properties loadPropties(Context context, String file)
			throws IOException {
		Properties properties = new Properties();
		try {
			InputStream fileStream = context.getAssets().open(file);
			properties.load(fileStream);
			fileStream.close();
		} catch (FileNotFoundException e) {
		}
		return properties;
	}

	public static CallProperties getCallProperites(Context context,
			String urlId, String file) {
		Properties prop = null;
		try {
			prop = AsyncUtil.loadPropties(context, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		CallProperties callProperties = new CallProperties();
		callProperties.baseURL = prop.getProperty(urlId + ".baseURL");
		callProperties.protocol = prop.getProperty(urlId + ".protocol");
		callProperties.method = prop.getProperty(urlId + ".method");

		return callProperties;
	}

}
