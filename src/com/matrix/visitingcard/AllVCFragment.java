package com.matrix.visitingcard;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.adapter.VCAdapter;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.FriendsVC;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;
import com.matrix.visitingcard.view.ImprovedStackView;

public class AllVCFragment extends Fragment {
	private View parentView;
	private VCAdapter mAdapter;
	private AsyncHttp mAsyncHttp;
	private SharedPrefs sp;
	private ImprovedStackView svAllVc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		parentView = inflater.inflate(R.layout.activity_home_screen, container,
				false);
		
		initialize();
		getAllFriendsVC();
		return parentView;
	}
	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(getActivity());
		svAllVc=(ImprovedStackView)parentView.findViewById(R.id.svAllVc);
		
	}
	@Override
	public void onDestroyView() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroyView();
	}
	
	private void getAllFriendsVC() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(getActivity(),
				"friend_vc", "url.properties");

		mAsyncHttp.addHeader("Cookie",
				sp.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));
		ARHandlerGetFriendVC handler = new ARHandlerGetFriendVC();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);
	}

	class ARHandlerGetFriendVC extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			 VLogger.e("ConnectionSuccessful, status code " + statusCode
			 + "content "
			 + (content == null ? "null" : new String(content)));
			FriendsVC.setVCS(Parser.parseVC(content));

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
	private void setAdapter() {
		mAdapter = new VCAdapter(getActivity(), R.layout.list_item_vc,
				FriendsVC.getAllVC());
		svAllVc.setAdapter(mAdapter);
	}
}
