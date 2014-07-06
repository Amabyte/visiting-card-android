package com.matrix.visitingcard.http.request;

import com.matrix.asynchttplibrary.request.AsyncRequestParam;

public class ShareVCResquest extends AsyncRequestParam {
	public String email;

	public ShareVCResquest(String email) {
		this.email = email;
	}
}
