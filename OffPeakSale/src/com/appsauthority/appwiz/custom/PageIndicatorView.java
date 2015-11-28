package com.appsauthority.appwiz.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.offpeaksale.consumer.R;

public class PageIndicatorView extends LinearLayout {
	int count;
	Context context;
	int selectedPage = 0;
	LinearLayout llIndicatorContainer;

	public PageIndicatorView(Context context, int count) {
		super(context);
		this.context = context;
		this.count = count;
		init();
	}

	void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(
				R.layout.page_indicator_container_layout, null, false);
		llIndicatorContainer = (LinearLayout) rootView
				.findViewById(R.id.llIndicatorContainer);
		this.addView(rootView);

		for (int i = 0; i < count; i++) {
			View view = inflater.inflate(R.layout.page_indicator_view_layout,
					null, false);
			view.setTag(i);
			View indicator = (View) view.findViewById(R.id.vwCircle);
			llIndicatorContainer.addView(view);
			if (i == selectedPage) {
				indicator.setBackgroundResource(R.drawable.page_selected);
			} else {
				indicator.setBackgroundResource(R.drawable.page_unselected);
			}
		}

	}

	public void setSelectedPage(int pageNo) {
		if (pageNo >= count) {

		} else {
			View prevSelecedView = llIndicatorContainer
					.getChildAt(selectedPage);
			View preIndicator = (View) prevSelecedView
					.findViewById(R.id.vwCircle);
			preIndicator.setBackgroundResource(R.drawable.page_unselected);
			selectedPage = pageNo;
			View curSelecedView = llIndicatorContainer.getChildAt(selectedPage);
			View curIndicator = (View) curSelecedView
					.findViewById(R.id.vwCircle);
			curIndicator.setBackgroundResource(R.drawable.page_selected);
		}
	}

}
