package com.matrix.visitingcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.response.VCTResponse;
import com.matrix.visitingcard.http.response.VCTResponse.KeysAndTypes;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.FileUtil;
import com.matrix.visitingcard.util.SharedPrefs;

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

	private final int SELECT_PHOTO = 1;
	private View type[];

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
		type = new View[size];

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
				type[i] = inflater.inflate(R.layout.item_type_button, null);

				((Button) type[i]).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent photoPickerIntent = new Intent(
								Intent.ACTION_PICK);
						photoPickerIntent.setType("image/*");
						startActivityForResult(photoPickerIntent, SELECT_PHOTO);

					}
				});
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				String filePath = getRealPathFromURI(imageReturnedIntent
						.getData());

				type[type.length - 1].setTag(filePath);
			}
		}
	}

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(this, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	protected void sendCollectedInfo(ArrayList<KeysAndTypes> keysAndTypes,
			View[] views) {
		VLogger.e("send collection");
		String PARAM_KEY = "visiting_card[visiting_card_datas_attributes[%d][key]]";
		String PARAM_VALUE = "visiting_card[visiting_card_datas_attributes[%d][value]]";
		String PARAM_IMAGE = "visiting_card[visiting_card_datas_attributes[%d][image]]";

		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"create_vc", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		ARHandlerCreateVC handler = new ARHandlerCreateVC();

		RequestParams params = new RequestParams();

		params.put("visiting_card[visiting_card_template_id]", vct.getId());

		int size = keysAndTypes.size();

		for (int i = 0; i < size; i++) {

			KeysAndTypes kt = keysAndTypes.get(i);
			switch (kt.getType()) {
			case TEXT:
			case TEXTAREA:
				String value = ((EditText) views[i]).getText().toString();
				params.put(String.format(PARAM_KEY, i), kt.getKey());
				params.put(String.format(PARAM_VALUE, i), value);
				// VLogger.e("\nkey " + kt.getKey() + "\nval " + value);
				break;
			case IMAGE:
				File image;
				String mimeType;
				String filePath = null;
				Object o = views[i].getTag();
				if (o != null) {
					filePath = (String) o;
				}
				try {
					image = new File(filePath);
					VLogger.e("path "+filePath);
					mimeType = FileUtil.getMimeType(image.getAbsolutePath());

					params.put(String.format(PARAM_KEY, i), kt.getKey());
					params.put(String.format(PARAM_VALUE, i), "vcimagevc");
					params.put(String.format(PARAM_IMAGE, i), image, mimeType);

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
			Toast.makeText(CreateVCActivity.this, "Visiting Card Created",
					Toast.LENGTH_LONG).show();

			CreateVCActivity.this.finish();
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
