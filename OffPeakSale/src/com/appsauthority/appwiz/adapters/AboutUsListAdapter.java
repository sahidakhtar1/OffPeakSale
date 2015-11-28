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

public class AboutUsListAdapter extends ArrayAdapter<String> {

	private List<String> objects;
	private Context context;

	public AboutUsListAdapter(Context context, int resource,
			List<String> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
	}

	public class ViewHolder {

		public TextView title;
		public ImageView icon,iconBack;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			title = (TextView) view.findViewById(R.id.title);
			icon = (ImageView) view.findViewById(R.id.icon);
			iconBack = (ImageView) view.findViewById(R.id.iconBack);
			
			title.setTypeface(Helper.getSharedHelper().normalFont);
		}

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.about_us_list_item, null);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		String currency = objects.get(position);
		holder.title.setText(objects.get(position));
		holder.title.setTypeface(Helper.getSharedHelper().normalFont);
//		if (currency.equalsIgnoreCase(Helper.getSharedHelper().currency_code)) {
//			holder.icon.setVisibility(View.VISIBLE);
//		} else {
//			holder.icon.setVisibility(View.GONE);
//		}

		if (position == 0) {
		}else if (position == 1) {
			holder.icon.setImageResource(R.drawable.about_us);
			
		}else if (position == 2) {
			holder.icon.setImageResource(R.drawable.termsofuse);
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		ImageView iconBack = (ImageView) convertView.findViewById(R.id.iconBack);
		if (position == 0) {
			iconBack.setVisibility(View.VISIBLE);
			imgIcon.setVisibility(View.INVISIBLE);
		} else {
			iconBack.setVisibility(View.GONE);
			imgIcon.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
