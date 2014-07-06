package com.matrix.visitingcard.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefs {
	private static SharedPreferences sp = null;
	private static Context context;
	private static final String SHARED_PREF_NAME = "com.matrix.visitingcard.util.sharedprefs";

	private static SharedPrefs instance = null;

	public static SharedPrefs getInstance(Context context) {
		if (instance == null) {
			SharedPrefs.context = context;
			instance = new SharedPrefs();
		}
		return instance;
	}

	protected SharedPrefs() {
		sp = context.getSharedPreferences(SHARED_PREF_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * Get Shared Preference value from specified key
	 * 
	 * @param context
	 *            The reference context
	 * @param key
	 *            key to be searched
	 * @param defaultValue
	 *            default value if key is not found
	 * @return If the key is present in SharedPrefs, returns the value with
	 *         respect to key , or else returns defaultValue
	 */
	public String getSharedPrefsValueString(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	/**
	 * Get Shared Preference value from specified key
	 * 
	 * @param context
	 *            The reference context
	 * @param key
	 *            key to be searched
	 * @param defaultValue
	 *            default value if key is not found
	 * @return If the key is present in SharedPrefs, returns the value with
	 *         respect to key , or else returns defaultValue
	 */
	public boolean getSharedPrefsValueBool(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	/**
	 * Get Shared Preference value from specified key
	 * 
	 * @param context
	 *            The reference context
	 * @param key
	 *            key to be searched
	 * @param defaultValue
	 *            default value if key is not found
	 * @return If the key is present in SharedPrefs, returns the value with
	 *         respect to key , or else returns defaultValue
	 */
	public float getSharedPrefsValueFloat(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	/**
	 * Save the key value pair in SharedPrefs
	 * 
	 * @param context
	 *            The reference context
	 * @param key
	 *            Key of the value to be stored
	 * @param value
	 *            value to be stored
	 */
	public void savePreferences(String key, String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Save the key value pair in SharedPrefs
	 * 
	 * @param context
	 *            The reference context
	 * @param key
	 *            Key of the value to be stored
	 * @param value
	 *            value to be stored
	 */
	public void savePreferences(String key, float value) {
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void destroy() {
		sp.edit().clear().commit();
	}

}