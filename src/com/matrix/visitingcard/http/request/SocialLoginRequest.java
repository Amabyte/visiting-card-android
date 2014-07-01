package com.matrix.visitingcard.http.request;

import com.matrix.asynchttplibrary.request.AsyncRequestParam;

public class SocialLoginRequest extends AsyncRequestParam {
	public String provider;
	public String token;

	public SocialLoginRequest(String provider, String token) {
		this.provider = provider;
		this.token = token;
	}

}
