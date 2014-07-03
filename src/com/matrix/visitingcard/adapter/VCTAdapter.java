package com.matrix.visitingcard.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.matrix.visitingcard.R;
import com.matrix.visitingcard.http.response.VCTResponse;

public class VCTAdapter extends ArrayAdapter<VCTResponse>  {
	private Context context;
	private int resId;
	private LayoutInflater mInflater;
//	private List<VCTResponse> items;




	public VCTAdapter(Context context, int resourceId,
			List<VCTResponse> items) {
		super(context, resourceId, items);
		this.context = context;
		this.resId = resourceId;
		//this.items = items;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		TextView id;
		TextView name;
		

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		
		ViewHolder holder = null;
		VCTResponse vctResponse = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(resId, null);
			holder = new ViewHolder();

			holder.id = (TextView) convertView
					.findViewById(R.id.tvVctId);
			holder.name = (TextView) convertView
					.findViewById(R.id.tvVctName);
	

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.id.setText(vctResponse.getId()+"");
		holder.name.setText(vctResponse.getName());
		return convertView;
	}

}
