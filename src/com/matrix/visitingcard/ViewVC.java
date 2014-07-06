package com.matrix.visitingcard;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.request.AsyncRequestParam;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.HomeScreenActivity.ARHandlerSignout;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.request.ShareVCResquest;
import com.matrix.visitingcard.http.response.MyVC;
import com.matrix.visitingcard.http.response.VC;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewVC extends Activity implements OnClickListener {
	private VC vc;
	private ImageView ivVC;
	private SharedPrefs sp;
	private AsyncHttp mAsyncHttp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_vc);

		initialize();
		initializeViews();

		setImage();
	}

	private void setImage() {
		ImageLoader.getInstance()
				.displayImage(
						Constants.URL.IMAGE_BASE
								+ vc.getImagUrls().getOriginal(), ivVC);
	}

	private void initializeViews() {
		ivVC = (ImageView) findViewById(R.id.ivMyVc);
		findViewById(R.id.bShareVc).setOnClickListener(this);
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(this);

	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}

	private void initialize() {
		int id = getIntent().getIntExtra(Constants.Intent.MY_VC_LIST_ID, 0);
		vc = MyVC.getAllVC().get(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bShareVc:
			shareVC();
			break;

		default:
			break;
		}

	}

	private void shareVC() {
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"base", "url.properties");
		connectionProperties.baseURL += (String.format(
				"/visiting_cards/%d/share.json", vc.getId()));

		mAsyncHttp.addHeader("Cookie",
				sp.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		ARHandlerShare handler = new ARHandlerShare();

		ShareVCResquest param = new ShareVCResquest("yajnesh6@gmail.com");
		mAsyncHttp.communicate(connectionProperties, null, param, handler);
	}

	class ARHandlerShare extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.e("ConnectionSuccessful, status code " + statusCode
					+ "content "
					+ (content == null ? "null" : new String(content)));
		}

		@Override
		public void onFailure(int statusCode, Header[] arg1, byte[] response,
				Throwable arg3) {
			VLogger.e("Connection Failed, status code " + statusCode
					+ " response "
					+ (response == null ? "null" : new String(response)));

		}

	}
}
