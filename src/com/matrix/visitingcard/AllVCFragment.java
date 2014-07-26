package com.matrix.visitingcard;

import org.apache.http.Header;

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
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.adapter.VCAdapter;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.FriendsVC;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

public class AllVCFragment extends Fragment implements OnItemClickListener {
	private View parentView;
	private VCAdapter mAdapter;
	private AsyncHttp mAsyncHttp;
	private SharedPrefs sp;
	private ListView lvAllVc;

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
		lvAllVc=(ListView)parentView.findViewById(R.id.lvAllVc);
		lvAllVc.setOnItemClickListener(this);
		
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
		
		VLogger.e("total "+ FriendsVC.getAllVC().size());
		lvAllVc.setAdapter(mAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(getActivity(), ViewVC.class);
		i.putExtra(Constants.Intent.MY_VC_LIST_ID, (int) id);
		i.putExtra(Constants.Intent.CALLER,
				Constants.Intent.Values.CALLER_FRIENDVC);
		startActivity(i);

	}
}
