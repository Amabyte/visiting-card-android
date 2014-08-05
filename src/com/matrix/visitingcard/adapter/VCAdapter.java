package com.matrix.visitingcard.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.matrix.visitingcard.R;
import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.http.response.VC;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VCAdapter extends ArrayAdapter<VC> {
	private int resId;
	private LayoutInflater mInflater;

	// private List<VCTResponse> items;

	public VCAdapter(Context context, int resourceId, List<VC> items) {
		super(context, resourceId, items);
		this.resId = resourceId;
		// this.items = items;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView ivTemplate;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		VC vc = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resId,null);

			holder = new ViewHolder();

			holder.ivTemplate = (ImageView) convertView
					.findViewById(R.id.ivVctImage);
			// VLogger.e("before "+holder.ivTemplate.getLayoutParams().width +
			// " "+holder.ivTemplate.getWidth());
			// holder.ivTemplate.getLayoutParams().width = (int) (Util
			// .getDisplayWidth(getContext()) * .7);
			// VLogger.e("Log "+Util.getDisplayWidth((Activity) getContext()));
			// VLogger.e("after "+holder.ivTemplate.getLayoutParams().width +
			// " "+holder.ivTemplate.getMeasuredWidth());
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		setImage(holder.ivTemplate, Constants.URL.IMAGE_BASE
				+ vc.getImagUrls().getOriginal());

		// if (vctResponse.getImage() != null) {
		// holder.ivTemplate.setImageBitmap(vctResponse.getImage());
		// }
		return convertView;
	}

	private void setImage(ImageView ivTemplate, String url) {
		ImageLoader.getInstance().displayImage(url, ivTemplate);
	}

}
