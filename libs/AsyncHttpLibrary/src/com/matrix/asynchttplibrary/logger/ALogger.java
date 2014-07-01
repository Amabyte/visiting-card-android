package com.matrix.asynchttplibrary.logger;

import android.util.Log;

public class ALogger {
	private static final boolean enableLog = true;
	private static final boolean enableCallerClassName = true;

	public static void e(String msg) {
		if (enableLog) {
			Log.e(getCallerClassName(), msg);
		}
	}

	public static void d(String msg) {
		if (enableLog) {
			Log.d(getCallerClassName(), msg);
		}
	}

	public static void v(String msg) {
		if (enableLog) {
			Log.v(getCallerClassName(), msg);
		}
	}

	public static void i(String msg) {
		if (enableLog) {
			Log.i(getCallerClassName(), msg);
		}
	}

	public static void w(String msg) {
		if (enableLog) {
			Log.w(getCallerClassName(), msg);
		}
	}

	public static void wtf(String msg) {
		if (enableLog) {
			Log.wtf(getCallerClassName(), msg);
		}
	}

	public static String getCallerClassName() {

		if (enableCallerClassName) {
			return new Exception().getStackTrace()[2].getClassName();
		} else {
			return "AsyncH Library";
		}
	}
}
