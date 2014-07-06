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
import com.matrix.visitingcard.adapter.VCTAdapter;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.VCTResponse;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

public class ListOfVCTActivity extends Activity implements OnItemClickListener {
	private AsyncH mAsyncHttp;
	private ListView mListViewVCT;
	private VCTAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_vct);
		initialize();
		initializeViews();

		getVCT();

	}

	private void initializeViews() {
		mListViewVCT = (ListView) findViewById(R.id.lvVCT);
		mListViewVCT.setOnItemClickListener(this);

	}

	private void setAdapter() {
		mAdapter = new VCTAdapter(this, R.layout.list_item_vct,
				VCTResponse.getAllVCT());
		mListViewVCT.setAdapter(mAdapter);
	}

	private void getVCT() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"get_vct", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));
		ARHandlerGetVCT handler = new ARHandlerGetVCT();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);

	}

	class ARHandlerGetVCT extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			// VLogger.e("ConnectionSuccessful, status code " + statusCode
			// + "content "
			// + (content == null ? "null" : new String(content)));
			VCTResponse.setVCTs(Parser.parseVCT(content));
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

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent i = new Intent(ListOfVCTActivity.this, CreateVCActivity.class);
		i.putExtra(Constants.Intent.HOME_TO_VC, id);

		startActivity(i);
		finish();

	}

}
