package com.matrix.visitingcard;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.AsyncH;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.adapter.VCAdapter;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.MyVC;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;
import com.matrix.visitingcard.util.Util;

public class ListMyVCActivity extends Activity implements OnItemClickListener {
	private AsyncH mAsyncHttp;
	private ListView mListViewMyVC;
	private VCAdapter mAdapter;
	private SharedPrefs sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_my_vc);
		initialize();
		initializeViews();
		getAllMyVC();

	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(this);
		Util.addHeadersToUIL(this);
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void setAdapter() {
		mAdapter = new VCAdapter(this, R.layout.list_item_vc, MyVC.getAllVC());
		mListViewMyVC.setAdapter(mAdapter);
	}

	private void initializeViews() {
		mListViewMyVC = (ListView) findViewById(R.id.lvMyVC);
		mListViewMyVC.setOnItemClickListener(this);

	}

	private void getAllMyVC() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"my_vc", "url.properties");

		mAsyncHttp.addHeader("Cookie",
				sp.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));
		ARHandlerGetMyVC handler = new ARHandlerGetMyVC();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);
	}

	class ARHandlerGetMyVC extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.e("ConnectionSuccessful, status code " + statusCode
					+ "content "
					+ (content == null ? "null" : new String(content)));
			MyVC.setVCS(Parser.parseVC(content));

			setAdapter();
		}

		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] response,
				Throwable arg3) {
			VLogger.e("Connection Failed, status code " + statusCode
					+ " response "
					+ (response == null ? "null" : new String(response)));

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String caller = getIntent().getStringExtra(Constants.Intent.CALLER);
		if (caller != null
				&& caller
						.equals(Constants.Intent.Values.CALLER_MYVC_FOR_RESULT)) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra(Constants.Intent.VCR_ID, MyVC.getAllVC().get((int) id).getId());
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		} else {
			Intent i = new Intent(ListMyVCActivity.this, ViewVC.class);
			i.putExtra(Constants.Intent.MY_VC_LIST_ID, (int) id);
			i.putExtra(Constants.Intent.CALLER,
					Constants.Intent.Values.CALLER_MYVC);
			startActivity(i);
		}
	}
}
