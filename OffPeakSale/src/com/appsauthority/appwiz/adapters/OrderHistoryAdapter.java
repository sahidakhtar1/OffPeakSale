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

import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.ViewHolderOrderHistory;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.restaurants.R;

public class OrderHistoryAdapter extends ArrayAdapter<OrderObject> {

	private List<OrderObject> objects;
	private Context context;
	public int selectedIndex = -1;

	public OrderHistoryAdapter(Context context, int resource,
			List<OrderObject> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolderOrderHistory holder = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.row_order_history, null);

			holder = new ViewHolderOrderHistory(convertView);
			convertView.setTag(holder);
			holder.getTvOrderIdValue().setTypeface(
					Helper.getSharedHelper().boldFont);
			holder.getTvOrderTotalValue().setTypeface(
					Helper.getSharedHelper().boldFont);
			holder.getTvOrderStatusValue().setTypeface(
					Helper.getSharedHelper().normalFont);

			holder.getTvOrderId()
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.getTvOrderDate().setTypeface(
					Helper.getSharedHelper().normalFont);
			holder.getTvOrderStatus().setTypeface(
					Helper.getSharedHelper().normalFont);
			holder.getTvTotalCount().setTypeface(
					Helper.getSharedHelper().normalFont);
		}
		holder = (ViewHolderOrderHistory) convertView.getTag();
		OrderObject object = getItem(position);
		try {
			holder.getTvOrderIdValue().setText(object.orderId);
			String comp[] = object.orderDate.split(" ");

			String text = "";
			String date = "";
			if (comp.length > 0) {
				date = comp[0];
			}
			
			holder.getTvTotalCount().setText(object.products.size()+" items");
			 ;
			holder.getTvOrderTotalValue().setText(Helper.getSharedHelper().reatiler.defaultCurrency
					+ " " + object.orderTotal);
			holder.getTvOrderDate().setText(date);
			holder.getTvOrderStatusValue().setText(object.shippingStatus);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}
}
