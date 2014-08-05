package com.matrix.visitingcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.matrix.visitingcard.constant.Constants;

public class SelectVCActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_vc);
		ListMyVCFragment f = ListMyVCFragment.getInstance(true);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.fragment_container, f);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void onResult(int id) {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(Constants.Intent.VC_ID, id);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
