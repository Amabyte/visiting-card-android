package com.matrix.visitingcard;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.util.SharedPrefs;

public class SplashScreenActivity extends Activity {
	public static final String EXTRA_MESSAGE = "message";
	private String SENDER_ID = "445427555808";
	private GoogleCloudMessaging gcm;
	private String regid;
	private SharedPrefs sp;

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "C8ZJZ5PWCFZ9WFQ5QKHM");
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		sp = SharedPrefs.getInstance(this);

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(this);
			VLogger.e("regid is " + regid);
			if (regid.isEmpty()) {
				registerInBackground();
			} else {
				performLogin();
			}
		} else {
			VLogger.e("No valid Google Play Services APK found.");
		}

	}

	private void performLogin() {
		VLogger.e("regid is2 " + regid);

		String spValue = SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null);
		if (spValue == null || spValue.length() < 1) {
			startActivity(new Intent(this, SignUpFormActivity.class));
		} else {
			startActivity(new Intent(this, ResideActivity.class));
		}
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000)
						.show();
			} else {
				VLogger.e("This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		String registrationId = sp.getSharedPrefsValueString(
				Constants.SP.GCM_ID, "");
		if (registrationId.isEmpty()) {
			VLogger.v("Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.

		float registeredVersion = 0;

		registeredVersion = sp.getSharedPrefsValueFloat(
				Constants.SP.APP_VERSION, Integer.MIN_VALUE);
		float currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			VLogger.e("App version changed.");
			return "";
		}
		return registrationId;
	}

	private static float getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(SplashScreenActivity.this);
					}
					regid = gcm.register(SENDER_ID);

					// You should send the registration ID to your server over
					// HTTP,
					// so it can use GCM/HTTP or CCS to send messages to your
					// app.
					// The request to your server should be authenticated if
					// your app
					// is using accounts.
					performLogin();

					// For this demo: we don't need to send it because the
					// device
					// will send upstream messages to a server that echo back
					// the
					// message using the 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(SplashScreenActivity.this, regid);
				} catch (IOException ex) {
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return null;
			}
		}.execute();
	}

	private void storeRegistrationId(Context context, String regId) {
		float appVersion = getAppVersion(context);
		sp.savePreferences(Constants.SP.GCM_ID, regId);
		sp.savePreferences(Constants.SP.APP_VERSION, appVersion);
	}
}
