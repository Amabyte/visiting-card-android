package com.matrix.visitingcard.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.matrix.visitingcard.R;
import com.matrix.visitingcard.http.response.VCR;

public class VCRAdapter extends ArrayAdapter<VCR> {
	private LayoutInflater mInflater;

	public VCRAdapter(Context context, List<VCR> items) {
		super(context, android.R.layout.simple_list_item_1, items);
		this.mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		VCR vcr = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_vcr, null);
			holder = new ViewHolder();
			holder.messageTextView = (TextView) convertView
					.findViewById(R.id.tvMessage);
			holder.acceptButton = (Button) convertView
					.findViewById(R.id.bAccept);
			holder.declineButton = (Button) convertView
					.findViewById(R.id.bDecline);
			holder.acceptButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					VCR tvcr = getItem((Integer) v.getTag());
				}
			});
			holder.declineButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					VCR tvcr = getItem((Integer) v.getTag());
				}
			});
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String message = "";
		if (vcr.getMessage() != null && !vcr.getMessage().equals(""))
			message = "\n" + vcr.getMessage();
		holder.messageTextView.setText(vcr.getUserName()
				+ "is requested your VC." + message);
		holder.acceptButton.setTag(position);
		holder.declineButton.setTag(position);
		return convertView;
	}

	private class ViewHolder {
		public TextView messageTextView;
		public Button acceptButton, declineButton;
	}
}
