package com.matrix.visitingcard;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
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
	private ProgressDialog pd;

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "C8ZJZ5PWCFZ9WFQ5QKHM");
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

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
		pd = new ProgressDialog(ListMyVCRActivity.this);
		pd.setMessage("Please wait...");
	}

	private void showPD() {
		if (!pd.isShowing()) {
			pd.show();
		}
	}

	private void dismissPD() {
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void setAdapter() {
		mAdapter = new VCRAdapter(this, new ArrayList<VCR>());
		mListViewVCR.setAdapter(mAdapter);
		mListViewVCR.setEmptyView(findViewById(R.id.tvEmpty));
	}

	private void initializeViews() {
		mListViewVCR = (ListView) findViewById(R.id.lvVRC);
		setAdapter();
	}

	private void getAllMyVC() {
		showPD();
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
		dismissPD();
	}

	@Override
	public void onAsyncFailure(int status, JSONObject jsonObject) {
		if (jsonObject != null)
			VLogger.e(jsonObject.toString());
		dismissPD();
	}

	@Override
	public void onAsyncSuccess(JSONObject jsonObject) {
		dismissPD();
	}

	@Override
	public void onAsyncFailure(int status, String string) {
		dismissPD();
	}

	@Override
	public void onAsyncStart() {
	}

	@Override
	public void onAsyncFinish() {
	}

	@Override
	public void reloadUi() {
		finish();
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
		Intent i = new Intent(this, SelectVCActivity.class);
		i.putExtra(Constants.Intent.CALLER,
				Constants.Intent.Values.CALLER_MYVC_FOR_RESULT);
		startActivityForResult(i, 1);
	}
}
