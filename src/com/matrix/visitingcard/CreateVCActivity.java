package com.matrix.visitingcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.http.response.VCTResponse;
import com.matrix.visitingcard.http.response.VCTResponse.KeysAndTypes;
import com.matrix.visitingcard.logger.VLogger;

public class CreateVCActivity extends Activity {
	private int vctId;
	private VCTResponse vct;
	private AsyncHttp mAsyncHttp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_vc);

		vctId = (int) getIntent().getLongExtra(Constants.Intent.HOME_TO_VC, 0);
		vct = VCTResponse.getAllVCT().get(vctId);

		initialize();
		initializeViews();
		constructInputFields();
	}

	private void initializeViews() {
		((TextView) findViewById(R.id.tvNameVCT)).setText(vct.getName());

	}

	private void constructInputFields() {

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// ViewGroup rootView = (ViewGroup) getWindow().getDecorView()
		// .findViewById(android.R.id.content);

		LinearLayout rootView = (LinearLayout) findViewById(R.id.llCreateVC);
		if (rootView == null) {
			VLogger.e("rootview is null");
			return;
		}

		final ArrayList<KeysAndTypes> keysAndTypes = vct.getKeysAndTypes();
		if (keysAndTypes == null) {
			VLogger.e("no values to be filled :-( ");
			return;
		}

		int size = keysAndTypes.size();
		final View type[] = new View[size];

		for (int i = 0; i < size; i++) {

			KeysAndTypes kt = keysAndTypes.get(i);
			switch (kt.getType()) {
			case TEXT:
				type[i] = inflater.inflate(R.layout.item_type_text, null);
				((EditText) type[i]).setHint(kt.getKey());
				break;
			case TEXTAREA:
				type[i] = inflater.inflate(R.layout.item_type_text, null);
				((EditText) type[i]).setHint(kt.getKey());
				break;
			case IMAGE:
				type[i] = inflater.inflate(R.layout.item_type_text, null);
				((EditText) type[i]).setHint(kt.getKey());
				break;
			default:
				break;
			}
			if (type[i] == null) {
				VLogger.e("type " + i + " is null");
				continue;
			}

			rootView.addView(type[i]);

		}

		Button submit = new Button(this);
		submit.setText("submit");
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCollectedInfo(keysAndTypes, type);
			}
		});
		rootView.addView(submit, params);
	}

	protected void sendCollectedInfo(ArrayList<KeysAndTypes> keysAndTypes,
			View[] views) {
		VLogger.e("send collection");
		String PARAM_KEY = "visiting_card_datas_attributes[][key]";
		String PARAM_VALUE = "visiting_card_datas_attributes[][value]";
		String PARAM_IMAGE = "visiting_card_datas_attributes[][image]";

		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"createVC", "url.properties");

		mAsyncHttp.addHeader("Cookie", SignUpFormActivity.sessionId);
		ARHandlerCreateVC handler = new ARHandlerCreateVC();

		RequestParams params = new RequestParams();

		params.put("visiting_card_template_id", vctId);

		int size = keysAndTypes.size();

		for (int i = 0; i < size; i++) {

			KeysAndTypes kt = keysAndTypes.get(i);
			switch (kt.getType()) {
			case TEXT:
			case TEXTAREA:
				String value = ((EditText) views[i]).getText().toString();
				params.put(PARAM_KEY, kt.getKey());
				params.put(PARAM_VALUE, value);
				// VLogger.e("\nkey " + kt.getKey() + "\nval " + value);
				break;
			case IMAGE:
				params.put(PARAM_KEY, kt.getKey());
				File image = null;
				try {
					image = new File("/storage/emulated/0/VC/images.jpeg");
					VLogger.e("image size " + image.length());
					params.put(PARAM_IMAGE, image);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				break;

			}
		}
		 VLogger.e(params.toString());
		mAsyncHttp.generatePostRequestTemperoryMethod(
				connectionProperties.baseURL, null, params, handler);
	}

	class ARHandlerCreateVC extends AsyncHttpResponseHandler {

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

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
	}

	@Override
	protected void onDestroy() {
		mAsyncHttp.cancelAllRequests(true);
		super.onDestroy();
	}
}
