package com.appsauthority.appwiz.adapters;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class FilterListAdapter extends ArrayAdapter<String> {

	private List<String> objects;
	private Context context;

	public FilterListAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
	}

	public class ViewHolder {

		public TextView filterName;
		public ImageView imgTick;
		public View devider;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			filterName = (TextView) view.findViewById(R.id.tvFilterName);
			imgTick = (ImageView) view.findViewById(R.id.imgTick);
			devider = (View) view.findViewById(R.id.devider);
			devider.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(null));
			filterName.setTypeface(Helper.getSharedHelper().normalFont);
		}

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.filter_list_item, null);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		holder.filterName.setText(objects.get(position));
		if (position == Helper.getSharedHelper().filterIndex) {
			holder.imgTick.setVisibility(View.VISIBLE);
		} else {
			holder.imgTick.setVisibility(View.GONE);
		}

		return convertView;
	}

}
