package com.appsauthority.appwiz.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsauthority.appwiz.models.CategoryObject;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class EShopCategoryListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<CategoryObject> navDrawerItems;

	public EShopCategoryListAdapter(Context context,
			ArrayList<CategoryObject> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.eshop_category_list_item,
					null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		txtTitle.setTypeface(Helper.getSharedHelper().normalFont);
		CategoryObject category = navDrawerItems.get(position);
		// txtTitle.setText(Helper.getSharedHelper().toTitleCase(
		// category.category_name));
		txtTitle.setText(category.category_name);
		if (position == 0) {
			imgIcon.setVisibility(View.VISIBLE);
		} else {
			imgIcon.setVisibility(View.GONE);
		}

		return convertView;
	}

}
