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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ViewHolderEShop;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.restaurants.R;

public class EShopListAdapter extends ArrayAdapter<Product> {

	private List<Product> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;

	public EShopListAdapter(Context context, int resource, List<Product> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		imageCacheLoader = new ImageCacheLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderEShop holder = null;
		ImageView imageView = null;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// if (Helper.getSharedHelper().disablePayment.endsWith("1")) {
			// convertView = vi.inflate(R.layout.row_eshop_with_delete, null);
			// }else{
			//
			// }
			convertView = vi.inflate(R.layout.row_eshop, null);

			holder = new ViewHolderEShop(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolderEShop) convertView.getTag();

		Product object = objects.get(position);

		holder.getName().setText(object.getName());
		holder.getShortDesc().setText(object.getShortDescription().trim());
		// String oldPrice = object.getOldPrice();
		float conversionValue = 0.0f;
		try {
			conversionValue = Float
					.parseFloat(Helper.getSharedHelper().currency_conversion_map
							.get(Helper.getSharedHelper().currency_code));
		} catch (Exception e) {
			// TODO: handle exception
		}

		
		float newPrice = Float.parseFloat(object.getNewPrice());
		String selectedCurrencyCode = Helper.getSharedHelper().currency_code;
		if (conversionValue == 0) {
			conversionValue = 1.0f;
			selectedCurrencyCode = Helper.getSharedHelper().reatiler.defaultCurrency;
		}
		if (object.getOldPrice() != null ) {
			float oldProce = Float.parseFloat(object.getOldPrice());
			oldProce = oldProce * conversionValue;
			holder.getOldPrice().setText(
					selectedCurrencyCode
							+ Helper.getSharedHelper()
									.conertfloatToSTring(oldProce));
			holder.getOldPrice().setVisibility(View.VISIBLE);
		}else{
			holder.getOldPrice().setVisibility(View.GONE);
		}
		
		newPrice = newPrice * conversionValue;
		
		holder.getNewPrice().setText(
				selectedCurrencyCode
						+ Helper.getSharedHelper()
								.conertfloatToSTring(newPrice));
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
			holder.getTvQtyIndicator().setTypeface(Helper.getSharedHelper().normalFont);
			holder.getTvSaleIndicator().setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

		int width = imageView.getWidth();
		int height = imageView.getHeight();
		
		if (object.availQty != null) {
			if (Integer.parseInt(object.availQty)<=0) {
				holder.getTvQtyIndicator().setText("Sold Out");
				holder.getTvQtyIndicator().setVisibility(View.VISIBLE);
			}else if(Integer.parseInt(object.availQty)>5){
				holder.getTvQtyIndicator().setVisibility(View.GONE);
			}else{
				holder.getTvQtyIndicator().setText(object.availQty+" Left");
				holder.getTvQtyIndicator().setVisibility(View.VISIBLE);
			}
		}
		if (object.onSale != null && object.onSale.equalsIgnoreCase("1")) {
			holder.getTvSaleIndicator().setVisibility(View.VISIBLE);
		}else{
			holder.getTvSaleIndicator().setVisibility(View.GONE);
		}

		return convertView;
	}
}
