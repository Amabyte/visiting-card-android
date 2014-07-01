package com.techjini.asynchttplibrary.request;

import com.techjini.asynchttplibrary.annotation.AsyncHIgnoreParam;

public class AsyncRequestParam {
	@AsyncHIgnoreParam(ignore = "true")
	public static enum Type {
		DEFAULT, JSON
	};

	@AsyncHIgnoreParam(ignore = "true")
	private Type requestType = Type.DEFAULT;

	public Type getRequestType() {
		return requestType;
	}

	public void setRequestType(Type requestType) {
		this.requestType = requestType;
	}

}
