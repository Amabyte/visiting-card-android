package com.matrix.visitingcard;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListOfVCTActivity extends Fragment implements OnItemClickListener {
	private AsyncH mAsyncHttp;
	private ListView mListViewVCT;
	private VCTAdapter mAdapter;
	private View parentView;
	private ProgressDialog pd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.activity_list_vct, container,
				false);
		initialize();
		initializeViews();

		getVCT();

		return parentView;

	}

	private void initializeViews() {
		mListViewVCT = (ListView) parentView.findViewById(R.id.lvVCT);
		mListViewVCT.setOnItemClickListener(this);

	}

	private void setAdapter() {
		mAdapter = new VCTAdapter(getActivity(), R.layout.list_item_vct,
				VCTResponse.getAllVCT());
		mListViewVCT.setAdapter(mAdapter);
		mListViewVCT.setEmptyView(parentView.findViewById(R.id.tvEmpty));
	}

	private void getVCT() {
		showPD();
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				getActivity(), "get_vct", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(getActivity())
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
			dismissPD();
		}

		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] response,
				Throwable arg3) {
			VLogger.e("Connection Failed, status code " + statusCode
					+ " response "
					+ (response == null ? "null" : new String(response)));
			dismissPD();
		}

	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		pd = new ProgressDialog(getActivity());
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
	public void onDestroyView() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroyView();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent i = new Intent(getActivity(), CreateVCActivity.class);
		i.putExtra(Constants.Intent.HOME_TO_VC, id);

		startActivity(i);

	}

}
