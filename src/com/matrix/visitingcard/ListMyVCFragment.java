package com.matrix.visitingcard;

import org.apache.http.Header;

import android.app.Activity;
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

import com.flurry.android.FlurryAgent;
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

public class ListMyVCFragment extends Fragment implements OnItemClickListener {
	private AsyncH mAsyncHttp;
	private ListView mListViewMyVC;
	private VCAdapter mAdapter;
	private SharedPrefs sp;
	private View parentView;
	private ProgressDialog pd;
	private boolean isOpenForResult = false;

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

	public static ListMyVCFragment getInstance(boolean isOpenForResult) {
		ListMyVCFragment f = new ListMyVCFragment();
		Bundle b = new Bundle();
		b.putBoolean(Constants.Intent.SELECT_VC, isOpenForResult);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (isAdded() && getArguments() != null)
			isOpenForResult = getArguments().getBoolean(
					Constants.Intent.SELECT_VC);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.activity_list_my_vc, container,
				false);
		initialize();
		initializeViews();
		getAllMyVC();

		return parentView;

	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(getActivity());
		Util.addHeadersToUIL(getActivity());

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

	private void setAdapter() {
		mAdapter = new VCAdapter(getActivity(), R.layout.list_item_vc,
				MyVC.getAllVC());
		mListViewMyVC.setAdapter(mAdapter);
	}

	private void initializeViews() {
		mListViewMyVC = (ListView) parentView.findViewById(R.id.lvMyVC);
		mListViewMyVC.setOnItemClickListener(this);
		mListViewMyVC.setEmptyView(parentView.findViewById(R.id.tvEmpty));

	}

	private void getAllMyVC() {
		showPD();
		CallProperties connectionProperties = AsyncUtil.getCallProperites(
				getActivity(), "my_vc", "url.properties");

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isOpenForResult) {
			((SelectVCActivity) getActivity()).onResult(MyVC.getAllVC()
					.get((int) id).getId());
			return;
		}
		Intent i = new Intent(getActivity(), ViewVC.class);
		i.putExtra(Constants.Intent.MY_VC_LIST_ID, (int) id);
		i.putExtra(Constants.Intent.CALLER, Constants.Intent.Values.CALLER_MYVC);
		startActivity(i);
	}
}
