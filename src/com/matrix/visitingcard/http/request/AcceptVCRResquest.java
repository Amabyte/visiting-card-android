package com.matrix.visitingcard.http.request;

import com.matrix.asynchttplibrary.request.AsyncRequestParam;

public class AcceptVCRResquest extends AsyncRequestParam {
	public int visiting_card_id;

	public AcceptVCRResquest(int visiting_card_id) {
		this.visiting_card_id = visiting_card_id;
	}
}
