package com.matrix.visitingcard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.StackView;

public class ImprovedStackView extends StackView {

	public ImprovedStackView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ImprovedStackView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImprovedStackView(Context context) {
		super(context);
	}
	
	

	/**
	 * A callback hack for onItemSelected , Default android implementation
	 * doesn't trigger it
	 */
	@Override
	public void setDisplayedChild(int whichChild) {
		int mWhichChild = whichChild;
		if (whichChild >= getCount()) {
			mWhichChild = 0;
		} else if (whichChild < 0) {
			mWhichChild = getCount() - 1;
		}
		android.widget.AdapterView.OnItemSelectedListener listner = this
				.getOnItemSelectedListener();

		if (listner != null) {
			listner.onItemSelected(this, null, mWhichChild, getCount());
		}
		super.setDisplayedChild(whichChild);
	}

}