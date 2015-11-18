package com.appsauthority.appwiz.adapters;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductOption;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.appsauthority.appwiz.utils.UpdateUiFromAdapterListener;
import com.offpeaksale.restaurants.R;

public class OrderDetailAdapter extends ArrayAdapter<Product> {

	private List<Product> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;
	private Retailer retailer;
	HashMap<String, String> quntities;

	Boolean isUPdateQTY = false;

	int iCount = 0;

	List<String> listOptions;

	ListPopupWindow listPopupWindow;

	int selectedOptionIndex = 0;
	TextView selectedOptionValue;
	Product currentProduct;

	static class ViewHolderShoppingCart {
		private View row;
		private ImageView image = null;
		private TextView shortDesc = null;
		private TextView newPrice = null;
		private TextView oldPrice = null;
		private TextView tvRewardsPoints, tvOptions, tvGiftMessage,
				tvPriceDetails;

		public ViewHolderShoppingCart(ImageView img, TextView sdesc,
				TextView price, TextView oldPrice, TextView tvRewardsPoints,
				TextView tvOptions, TextView tvGiftMessage,
				TextView tvPriceDetails) {
			this.image = img;
			this.shortDesc = sdesc;
			this.newPrice = price;
			this.oldPrice = oldPrice;
			this.tvRewardsPoints = tvRewardsPoints;
			this.tvOptions = tvOptions;
			this.tvGiftMessage = tvGiftMessage;
			this.tvPriceDetails = tvPriceDetails;

		}
	}

	public OrderDetailAdapter(Context context, int resource,
			List<Product> objects) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		imageCacheLoader = new ImageCacheLoader(context);
		retailer = Helper.getSharedHelper().reatiler;
		this.quntities = new HashMap<String, String>();
		setQuantities();
	}

	void setQuantities() {
		quntities.clear();
		for (int i = 0; i < objects.size(); i++) {
			Product p = objects.get(i);
			quntities.put(Integer.toString(i), p.getQty());
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolderShoppingCart holder = null;
		// ImageView imageView = null;

		ImageView imageView;
		TextView shortDesc;
		TextView newPrice, oldPrice;

		TextView tvRewardsPoints, tvOptions, tvGiftMessage, tvPriceDetails;
		LinearLayout llFirstOption, llSecondOption;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_order_item, null);
			imageView = (ImageView) convertView.findViewById(R.id.iv_eshop);
			shortDesc = (TextView) convertView
					.findViewById(R.id.tv_eshop_short_desc);
			newPrice = (TextView) convertView.findViewById(R.id.tv_price);
			oldPrice = (TextView) convertView.findViewById(R.id.oldPrice);
			llFirstOption = (LinearLayout) convertView
					.findViewById(R.id.llFirstOption);
			llSecondOption = (LinearLayout) convertView
					.findViewById(R.id.llSecondOption);
			// vwFirstOptionRight1 = (View) llFirstOption
			// .findViewById(R.id.list_right1);
			// vwSecondOptionRight1 = (View) llSecondOption
			// .findViewById(R.id.list_right1);

			tvRewardsPoints = (TextView) convertView
					.findViewById(R.id.tvRewardsPoints);
			tvOptions = (TextView) convertView.findViewById(R.id.tvOptions);
			tvGiftMessage = (TextView) convertView
					.findViewById(R.id.tvGiftMessage);
			tvPriceDetails = (TextView) convertView
					.findViewById(R.id.tvPriceDetails);
			convertView.setTag(new ViewHolderShoppingCart(imageView, shortDesc,
					newPrice, oldPrice, tvRewardsPoints, tvOptions,
					tvGiftMessage, tvPriceDetails));

		}
		holder = (ViewHolderShoppingCart) convertView.getTag();

		try {
			Product object = objects.get(position);

			holder.shortDesc.setText(object.getName().trim());

			Float price = Float.parseFloat(object.getNewPrice());

			int qty = Integer.parseInt(object.quantity);
			// if (object.getIsOptedGiftWrap()) {
			// price += Float
			// .parseFloat(Helper.getSharedHelper().reatiler.gift_price);
			// holder.tvGiftMessage.setText(object.getGiftMsg());
			// holder.tvGiftMessage.setVisibility(View.VISIBLE);
			// } else {
			// holder.tvGiftMessage.setVisibility(View.GONE);
			// }
			float itemTotal = qty * price;
//			holder.newPrice.setText(Helper.getSharedHelper().getCurrencySymbol(
//					Helper.getSharedHelper().reatiler.defaultCurrency)
//					+ ""
//					+ Helper.getSharedHelper().conertfloatToSTring(itemTotal));
//			if (object.getOldPrice() != null) {
//
//				holder.oldPrice
//						.setText(Helper
//								.getSharedHelper()
//								.getCurrencySymbol(
//										Helper.getSharedHelper().reatiler.defaultCurrency)
//								+ ""
//								+ Helper.getSharedHelper().conertfloatToSTring(
//										Float.parseFloat(object.getOldPrice())));
//				holder.oldPrice.setVisibility(View.VISIBLE);
//			} else {
//				holder.oldPrice.setVisibility(View.GONE);
//			}

			imageView = holder.image;
			imageCacheLoader.displayImage(object.getImage(),
					R.drawable.image_placeholder, imageView);
			holder.shortDesc.setTypeface(Helper.getSharedHelper().boldFont);
			holder.newPrice.setTypeface(Helper.getSharedHelper().boldFont);
			holder.oldPrice.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvPriceDetails.setTypeface(Helper.getSharedHelper().normalFont);

			holder.tvRewardsPoints
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvOptions.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvGiftMessage
					.setTypeface(Helper.getSharedHelper().normalFont);

			holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

			String optiontext = null;
			if (object.prodOptions != null && object.prodOptions.length() > 0) {
				optiontext = object.prodOptions;
			}

			if (optiontext != null) {
				holder.tvOptions.setText("Options: " + optiontext);
				holder.tvOptions.setVisibility(View.VISIBLE);
			} else {
				holder.tvOptions.setVisibility(View.GONE);
			}
			String rewardPoints = object.reward_points;
			if (rewardPoints == null) {
				rewardPoints = "0";
			}
			try {
				int rewardsInt = Integer.parseInt(rewardPoints);
				rewardsInt = rewardsInt * qty;
				rewardPoints = Integer.toString(rewardsInt);
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (Helper.getSharedHelper().reatiler.enableRewards
					.equalsIgnoreCase("1")
					&& !rewardPoints.equalsIgnoreCase("0")) {
				holder.tvRewardsPoints.setVisibility(View.VISIBLE);
			} else {
				holder.tvRewardsPoints.setVisibility(View.GONE);
			}
			SpannableString spanableText = new SpannableString(rewardPoints
					+ " Credit Points");
			spanableText.setSpan(new StyleSpan(Typeface.BOLD), 0,
					rewardPoints.length(), 0);
			holder.tvRewardsPoints.setText(spanableText);
			String priceDetail = qty
					+ "X"
					+ Helper.getSharedHelper().conertfloatToSTring(price)
					+ "="
					+ Helper.getSharedHelper().getCurrencySymbol(
							Helper.getSharedHelper().reatiler.defaultCurrency)
					+ Helper.getSharedHelper().conertfloatToSTring(itemTotal);
			holder.tvPriceDetails.setText(priceDetail);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	public ColorDrawable getGradientDrawable(String headerColor) {
		// GradientDrawable gd;
		//
		// gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
		// new int[] { Color.parseColor("#80" + headerColor),
		// Color.parseColor("#" + headerColor),
		// Color.parseColor("#" + headerColor) });
		// gd.setCornerRadius(10);
		ColorDrawable cd = new ColorDrawable(
				Color.parseColor("#" + headerColor));
		return cd;
	}

}
