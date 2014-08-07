package com.matrix.visitingcard;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;

import com.flurry.android.FlurryAgent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.matrix.asynchttplibrary.model.CallProperties;
import com.matrix.asynchttplibrary.util.AsyncUtil;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.AsyncHttp;
import com.matrix.visitingcard.http.parser.Parser;
import com.matrix.visitingcard.logger.VLogger;
import com.matrix.visitingcard.user.User;
import com.matrix.visitingcard.util.SharedPrefs;
import com.matrix.visitingcard.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class ResideActivity extends FragmentActivity implements
		View.OnClickListener {

	private ResideMenu resideMenu;
	private ResideMenuItem itemHome;
	private AsyncHttp mAsyncHttp;
	private SharedPrefs sp;
	private ResideMenuItem itemMyVc;
	private ResideMenuItem itemCreateVc;
	private ResideMenuItem itemSignout;
	private ResideMenuItem itemRequestVC;
	private ResideMenuItem itemListVCR;
	private ProgressDialog pd;

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

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initialize();
		loadUserData();
		Util.addHeadersToUIL(this);

		setUpMenu();

		changeFragment(new AllVCFragment());
	}

	private void initialize() {
		mAsyncHttp = AsyncHttp.getNewInstance();
		sp = SharedPrefs.getInstance(this);

		pd = new ProgressDialog(ResideActivity.this);
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

	private void setUpMenu() {

		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_background);
		resideMenu.attachToActivity(this);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);

		// create menu items;
		itemHome = new ResideMenuItem(this, R.drawable.friend_vc_icon,
				"Received VCs");
		itemHome.setOnClickListener(this);

		itemMyVc = new ResideMenuItem(this, R.drawable.my_vcs_icon, "My VCs");
		itemMyVc.setOnClickListener(this);

		itemCreateVc = new ResideMenuItem(this, R.drawable.create_new_vc_icon,
				"Create VC");
		itemCreateVc.setOnClickListener(this);

		itemRequestVC = new ResideMenuItem(this, R.drawable.create_vcr,
				"Request VC");
		itemRequestVC.setOnClickListener(this);

		itemListVCR = new ResideMenuItem(this, R.drawable.list_vcr_icon,
				"Requests");
		itemListVCR.setOnClickListener(this);

		itemSignout = new ResideMenuItem(this, R.drawable.signout_icon,
				"Sign Out");
		itemSignout.setOnClickListener(this);

		resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemMyVc, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemCreateVc, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemRequestVC, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemListVCR, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemSignout, ResideMenu.DIRECTION_LEFT);

		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {

		if (v == itemHome) {
			changeFragment(new AllVCFragment());
		} else if (v == itemMyVc) {
			changeFragment(new ListMyVCFragment());
		} else if (v == itemCreateVc) {
			changeFragment(new ListOfVCTFragment());
		} else if (v == itemRequestVC) {
			createVCR();
		} else if (v == itemListVCR) {
			changeFragment(new AllVCFragment());
			startActivity(new Intent(this, ListMyVCRActivity.class));
		} else if (v == itemSignout) {
			signout();
		}

		resideMenu.closeMenu();
	}

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	// What good method is to access resideMenu？
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	private void loadUserData() {
		showPD();
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"profile", "url.properties");

		mAsyncHttp.addHeader("Cookie",
				sp.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));
		ARHandlerGetProfile handler = new ARHandlerGetProfile();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);
	}

	class ARHandlerGetProfile extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.e("Get profile ConnectionSuccessful, status code "
					+ statusCode + "content "
					+ (content == null ? "null" : new String(content)));
			User.setInstance(Parser.parseUser(content));
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

	private void signout() {
		showPD();
		CallProperties connectionProperties = AsyncUtil.getCallProperites(this,
				"sign_out", "url.properties");

		mAsyncHttp.addHeader("Cookie", SharedPrefs.getInstance(this)
				.getSharedPrefsValueString(Constants.SP.SESSION_ID, null));

		ARHandlerSignout handler = new ARHandlerSignout();

		mAsyncHttp.communicate(connectionProperties, null, null, handler);

	}

	class ARHandlerSignout extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] content) {

			VLogger.e("ConnectionSuccessful, status code " + statusCode
					+ "content "
					+ (content == null ? "null" : new String(content)));
			sp.destroy();
			ImageLoader.getInstance().clearDiskCache();
			ImageLoader.getInstance().clearMemoryCache();
			// TODO : clear GCM shit

			dismissPD();
			startActivity(new Intent(ResideActivity.this,
					SplashScreenActivity.class));

			finish();
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

	private void createVCR() {
		VCRCreateDialogFragment createVCRDialogFragment = new VCRCreateDialogFragment();
		createVCRDialogFragment.show(getSupportFragmentManager(), "creat_vcr");
	}
}