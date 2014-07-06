package com.matrix.visitingcard;

import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.util.SharedPrefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		String spValue = SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null);
		if (spValue == null || spValue.length() < 1) {
			startActivity(new Intent(this, SignUpFormActivity.class));
		} else {
			startActivity(new Intent(this, HomeScreenActivity.class));
		}

		finish();
	}
}
