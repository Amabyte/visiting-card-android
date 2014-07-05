package com.matrix.visitingcard.http.request;

import com.matrix.asynchttplibrary.request.AsyncRequestParam;

public class SocialLoginRequest extends AsyncRequestParam {
	public String provider;
	public String token;
	public String device_id;
	public String device_type = "android";

	public SocialLoginRequest(String provider, String token, String deviceId) {
		this.provider = provider;
		this.token = token;
		this.device_id = deviceId;
	}

}
