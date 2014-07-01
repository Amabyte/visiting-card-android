package com.matrix.visitingcard.util;

import android.app.Application;

import com.matrix.visitingcard.db.DB;


public class VisitingCardApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		DB.open(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		DB.close();
	}
}
