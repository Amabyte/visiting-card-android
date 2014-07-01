package com.matrix.visitingcard.http;

import com.matrix.asynchttplibrary.AsyncH;

public class AsyncHttp extends AsyncH {

	private static AsyncHttp instance = null;

	protected AsyncHttp() {
	}

	public synchronized static AsyncHttp getInstance() {
		if (instance == null) {
			instance = new AsyncHttp();
			instance.addHeader("Content-Type", "application/json");
			instance.addHeader("Accept",
					"application/vnd.visiting-card+json;version=1");
		}
		return instance;
	}

}
