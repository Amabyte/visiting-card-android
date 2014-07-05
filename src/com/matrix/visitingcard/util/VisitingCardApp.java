package com.matrix.visitingcard.util;

import android.app.Application;

import com.matrix.visitingcard.db.DB;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class VisitingCardApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		DB.open(getApplicationContext());

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		DB.close();

		ImageLoader.getInstance().stop();
		ImageLoader.getInstance().destroy();

	}
}
