package com.matrix.visitingcard;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.ProgressJSONResponseCallBack;
import com.matrix.visitingcard.http.ProgressJsonHttpResponseHandler;
import com.matrix.visitingcard.http.request.ShareVCResquest;
import com.matrix.visitingcard.util.SharedPrefs;

public class ShareVCDialogFragment extends DialogFragment implements
		OnClickListener, ProgressJSONResponseCallBack {

	private EditText emailEditText;
	private Button createVCRButton;
	private AsyncHttp mAsyncHttp;
	private int vcId;

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(getActivity(), "C8ZJZ5PWCFZ9WFQ5QKHM");
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(getActivity());
	}

	public static ShareVCDialogFragment getNewInstance(int vcId) {
		ShareVCDialogFragment f = new ShareVCDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.Intent.VC_ID, vcId);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, STYLE_NORMAL);
		View rootView = inflater.inflate(R.layout.fragment_share_vc, container,
				false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		vcId = getArguments().getInt(Constants.Intent.VC_ID, -1);
	}

	private void initViews(View rootView) {
		emailEditText = (EditText) rootView.findViewById(R.id.etEmail);
		createVCRButton = (Button) rootView.findViewById(R.id.bShareVC);
		createVCRButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bShareVC:
			shareVC();
			break;
		}
	}

	private void shareVC() {
		String email = emailEditText.getText().toString().trim();
		if (email == null || email.equals("")) {
			emailEditText.setError("Emial required");
			return;
		}
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				getActivity(), "base", "url.properties");
		connectionProperties.baseURL += (String.format(
				"/visiting_cards/%d/share.json", vcId));

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(getActivity())
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		ShareVCResquest param = new ShareVCResquest(email);
		mAsyncHttp.communicate(connectionProperties, null, param,
				new ProgressJsonHttpResponseHandler(getActivity(), this));
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
	}

	@Override
	public void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	@Override
	public void onAsyncSuccess(JSONObject jsonObject) {
		Toast.makeText(getActivity(), "VC shared", Toast.LENGTH_SHORT).show();
		dismiss();
	}

	@Override
	public void onAsyncFailure(int status, JSONObject jsonObject) {
		onAsyncFailure(status, jsonObject.toString());
	}

	@Override
	public void onAsyncFailure(int status, String string) {
		Toast.makeText(getActivity(),
				"Error : " + status + " Details : " + string,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAsyncStart() {
	}

	@Override
	public void onAsyncFinish() {
	}

	@Override
	public void onAsyncSuccess(JSONArray jsonArray) {
	}

}