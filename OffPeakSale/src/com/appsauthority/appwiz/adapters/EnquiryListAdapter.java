package com.appsauthority.appwiz.adapters;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ViewHolderEShop;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.appsauthority.appwiz.utils.UpdateUiFromAdapterListener;
import com.offpeaksale.consumer.R;

public class EnquiryListAdapter extends ArrayAdapter<Product> {

	private List<Product> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;
	UpdateUiFromAdapterListener mUpdateUi;

	public EnquiryListAdapter(Context context, int resource,
			List<Product> objects, UpdateUiFromAdapterListener updateUi) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		imageCacheLoader = new ImageCacheLoader(context);
		mUpdateUi = updateUi;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolderEShop holder = null;
		ImageView imageView = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.row_eshop_with_delete, null);

			holder = new ViewHolderEShop(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolderEShop) convertView.getTag();

		Product object = objects.get(position);

		holder.getName().setText(object.getName());
		holder.getShortDesc().setText(object.getShortDescription().trim());
		holder.getOldPrice().setText(
				Helper.getSharedHelper().currency_code + object.getOldPrice());
		holder.getNewPrice().setText(
				Helper.getSharedHelper().currency_code + object.getNewPrice());
		if (Helper.getSharedHelper().enableRating.equals("1")) {
			holder.getProductRatingBar().setRating(
					Float.parseFloat(object.getProductRating()));
			holder.getProductRatingBar().setVisibility(View.VISIBLE);
		} else {
			holder.getProductRatingBar().setVisibility(View.GONE);
		}

		holder.getOldPrice().setPaintFlags(
				holder.getOldPrice().getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
		// ivStroke = holder.getStroke();
		//
		// // ImageView ivStrike = (ImageView)findViewById(R.id.ivStrike);
		//
		// float textSize = holder.getOldPrice().getTextSize() / 2;
		// int textLength = holder.getOldPrice().getText().length();
		// int totalLengthApprox = (int) (textLength * textSize) - textLength;
		// int height = 2; // YOUR_REQUIRED_HEIGHT
		//
		// RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
		// totalLengthApprox, height);
		// param.addRule(RelativeLayout.CENTER_VERTICAL
		// | RelativeLayout.ALIGN_PARENT_LEFT);
		// param.setMargins(4, 0, 0, 0);
		// ivStroke.setLayoutParams(param);

		imageView = holder.getImage();

		imageCacheLoader.displayImage(object.getImage(),
				R.drawable.image_placeholder, imageView);

		try {
			holder.getShortDesc().setTypeface(
					Helper.getSharedHelper().normalFont);
			holder.getOldPrice().setTypeface(
					Helper.getSharedHelper().normalFont);
			holder.getNewPrice().setTypeface(Helper.getSharedHelper().boldFont);
		} catch (Exception e) {

		}
		holder.getBtn_delete().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Helper.getSharedHelper().shoppintCartList.remove(position);
				objects = Helper.getSharedHelper().shoppintCartList;
				notifyDataSetChanged();
				mUpdateUi.updateCart();

			}
		});

		return convertView;
	}

}
