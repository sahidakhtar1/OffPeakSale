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

public class CurrencyListAdapter extends ArrayAdapter<String> {

	private List<String> objects;
	private Context context;

	public CurrencyListAdapter(Context context, int resource,
			List<String> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
	}

	public class ViewHolder {

		public TextView filterName;
		public ImageView imgTick;
		public View devider;
		public ImageView iconback,icon;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			filterName = (TextView) view.findViewById(R.id.tvFilterName);
			imgTick = (ImageView) view.findViewById(R.id.imgTick);
			devider = (View) view.findViewById(R.id.devider);
			icon = (ImageView)view.findViewById(R.id.icon);
			iconback = (ImageView)view.findViewById(R.id.iconback);
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

			convertView = vi.inflate(R.layout.currency_list_item, null);

			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		String currency = objects.get(position);
		if (position>2) {
			holder.filterName.setText("      "+objects.get(position));
		}else{
			holder.filterName.setText(objects.get(position));
		}
		
		holder.filterName.setTypeface(Helper.getSharedHelper().normalFont);
		if (currency.equalsIgnoreCase(Helper.getSharedHelper().currency_code)) {
			holder.imgTick.setVisibility(View.VISIBLE);
		} else {
			holder.imgTick.setVisibility(View.GONE);
		}
		holder.devider.setVisibility(View.GONE);
		
//		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		if (position == 0) {
			holder.iconback.setVisibility(View.VISIBLE);
			holder.icon.setVisibility(View.INVISIBLE);
			
		} else if(position == 1 ){
			holder.iconback.setVisibility(View.GONE);
			holder.icon.setVisibility(View.VISIBLE);
			holder.icon.setImageResource(R.drawable.my_profile);
		}else if( position == 2){
			holder.iconback.setVisibility(View.GONE);
			holder.icon.setVisibility(View.VISIBLE);
			holder.icon.setImageResource(R.drawable.ic_currency);
		}
		else{
			holder.iconback.setVisibility(View.GONE);
			holder.icon.setVisibility(View.GONE);
		}

		return convertView;
	}

}
