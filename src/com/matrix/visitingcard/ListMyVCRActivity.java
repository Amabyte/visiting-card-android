package com.matrix.visitingcard;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.matrix.asynchttplibrary.AsyncH;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.adapter.VCRAdapter;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.ProgressJSONResponseCallBack;
import com.matrix.visitingcard.http.ProgressJsonHttpResponseHandler;
import com.matrix.visitingcard.http.UIReloadCallBack;
import com.matrix.visitingcard.http.response.VCR;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

public class ListMyVCRActivity extends Activity implements
		ProgressJSONResponseCallBack, UIReloadCallBack {
	private AsyncH mAsyncHttp;
	private ListView mListViewVCR;
	private VCRAdapter mAdapter;
	private SharedPrefs sp;
	private VCR lastVCR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vcr_list);
		initialize();
		initializeViews();
		getAllMyVC();
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(this);
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void setAdapter() {
		mAdapter = new VCRAdapter(this, new ArrayList<VCR>());
		mListViewVCR.setAdapter(mAdapter);
	}

	private void initializeViews() {
		mListViewVCR = (ListView) findViewById(R.id.lvVRC);
		setAdapter();
	}

	private void getAllMyVC() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"my_vcr", "url.properties");

		mAsyncHttp.addHeader("Cookie",
				sp.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		mAsyncHttp.communicate(connectionProperties, null, null,
				new ProgressJsonHttpResponseHandler(this, this));
	}

	@Override
	public void onAsyncSuccess(JSONArray jsonArray) {
		ArrayList<VCR> vcrs = new ArrayList<VCR>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				vcrs.add(VCR.parse(jsonArray.getJSONObject(i)));
			} catch (JSONException e) {
				VLogger.e(e.getMessage());
			}
		}
		mAdapter.supportAddAll(vcrs);
	}

	@Override
	public void onAsyncFailure(int status, JSONObject jsonObject) {
		VLogger.e(jsonObject.toString());
	}

	@Override
	public void onAsyncSuccess(JSONObject jsonObject) {
	}

	@Override
	public void onAsyncFailure(int status, String string) {
	}

	@Override
	public void onAsyncStart() {
	}

	@Override
	public void onAsyncFinish() {
	}

	@Override
	public void reloadUi() {
		getAllMyVC();
	}

	public AsyncH getAsyncHttp() {
		return mAsyncHttp;
	}

	public SharedPrefs getSp() {
		return sp;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
			if (lastVCR != null)
				lastVCR.accept(this,
						data.getIntExtra(Constants.Intent.VC_ID, -1));
	}

	public void acceptVCR(VCR vcr) {
		lastVCR = vcr;
		Intent i = new Intent(this, ListMyVCActivity.class);
		i.putExtra(Constants.Intent.CALLER,
				Constants.Intent.Values.CALLER_MYVC_FOR_RESULT);
		startActivityForResult(i, 1);
	}
}
