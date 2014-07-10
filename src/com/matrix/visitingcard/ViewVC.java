package com.matrix.visitingcard;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.response.FriendsVC;
import com.matrix.visitingcard.http.response.MyVC;
import com.matrix.visitingcard.http.response.VC;
import com.matrix.visitingcard.logger.VLogger;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewVC extends FragmentActivity implements OnClickListener {
	private VC vc;
	private ImageView ivVC;
	private boolean showShareButton;

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
		Button bShare = (Button) findViewById(R.id.bShareVC);
		bShare.setOnClickListener(this);

		bShare.setVisibility(showShareButton ? View.VISIBLE : View.INVISIBLE);

	}

	private void initialize() {
		int id = getIntent().getIntExtra(Constants.Intent.MY_VC_LIST_ID, 0);
		String whoIsCalling = getIntent().getStringExtra(
				Constants.Intent.CALLER);

		if (whoIsCalling != null
				&& whoIsCalling
						.equalsIgnoreCase(Constants.Intent.Values.CALLER_MYVC)) {
			vc = MyVC.getAllVC().get(id);
			showShareButton = true;
		} else if (whoIsCalling != null
				&& whoIsCalling
						.equalsIgnoreCase(Constants.Intent.Values.CALLER_FRIENDVC)) {
			vc = FriendsVC.getAllVC().get(id);
			showShareButton = false;
		} else {
			VLogger.e("Caller not identified");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bShareVC:
			shareVC();
			break;

		default:
			break;
		}

	}

	private void shareVC() {
		ShareVCDialogFragment f = ShareVCDialogFragment.getNewInstance(vc
				.getId());
		f.show(getSupportFragmentManager(), "share_vc");
	}
}
