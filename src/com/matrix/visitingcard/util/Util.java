package com.matrix.visitingcard.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.matrix.visitingcard.constant.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Util {
	
	public static void addHeadersToUIL(Context context) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", SharedPrefs.getInstance(context)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));
		ImageLoader.getInstance().destroy();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.extraForDownloader(headers).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.imageDownloader(new CustomImageDownaloder(context)).build();

		ImageLoader.getInstance().init(config);
	}
//	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
//
//	/**
//	 * Generate a value suitable for use in {@link #setId(int)}. This value will
//	 * not collide with ID values generated at build time by aapt for R.id.
//	 * 
//	 * @return a generated ID value
//	 */
//	@SuppressLint("NewApi")
//	public static int generateViewId() {
//
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//
//			return View.generateViewId();
//
//		}
//		for (;;) {
//			final int result = sNextGeneratedId.get();
//			// aapt-generated IDs have the high byte nonzero; clamp to the range
//			// under that.
//			int newValue = result + 1;
//			if (newValue > 0x00FFFFFF)
//				newValue = 1; // Roll over to 1, not 0.
//			if (sNextGeneratedId.compareAndSet(result, newValue)) {
//				return result;
//			}
//		}
//	}
}
