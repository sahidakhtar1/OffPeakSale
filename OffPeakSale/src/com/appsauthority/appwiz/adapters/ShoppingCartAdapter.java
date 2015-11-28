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
import com.offpeaksale.consumer.R;

public class ShoppingCartAdapter extends ArrayAdapter<Product> implements
		OnItemClickListener {

	private List<Product> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;
	private Retailer retailer;
	UpdateUiFromAdapterListener mUpdateUi;
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
		private EditText edt_qty = null;
		private Button btn_update = null;
		private Button btn_delete = null;
		private View vwFirstOptionRight1, vwSecondOptionRight1,
				vwFirstOptionUnderline, vwSecondOptionUnderline;
		private RelativeLayout rlFirstOption, rlSecondOption;
		private TextView tvFirstOptinLbl, tvSeconoptionLbl, tvFirstOptionValue,
				tvSecondOptionValue, tvRewardsPoints, tvOptions,tvGiftMessage;
		private LinearLayout llFirstOption, llSecondOption;

		public ViewHolderShoppingCart(ImageView img, TextView sdesc,
				TextView price, TextView oldPrice, EditText qty, Button update,
				Button delete, View vwFirstOptionRight1,
				View vwSecondOptionRight1, View vwFirstOptionUnderline,
				View vwSecondOptionUnderline, RelativeLayout rlFirstOption,
				RelativeLayout rlSecondOption, TextView tvFirstOptinLbl,
				TextView tvSeconoptionLbl, TextView tvFirstOptionValue,
				TextView tvSecondOptionValue, LinearLayout llFirstOption,
				LinearLayout llSecondOption, TextView tvRewardsPoints,
				TextView tvOptions, TextView tvGiftMessage) {
			this.image = img;
			this.shortDesc = sdesc;
			this.newPrice = price;
			this.oldPrice = oldPrice;
			this.edt_qty = qty;
			this.btn_delete = delete;
			this.btn_update = update;
			// this.vwFirstOptionRight1 = vwFirstOptionRight1;
			// this.vwSecondOptionRight1 = vwSecondOptionRight1;
			this.vwFirstOptionUnderline = vwFirstOptionUnderline;
			this.vwSecondOptionUnderline = vwSecondOptionUnderline;

			this.rlFirstOption = rlFirstOption;
			this.rlSecondOption = rlSecondOption;

			this.tvFirstOptinLbl = tvFirstOptinLbl;
			this.tvSeconoptionLbl = tvSeconoptionLbl;
			this.tvFirstOptionValue = tvFirstOptionValue;
			this.tvSecondOptionValue = tvSecondOptionValue;

			this.llFirstOption = llFirstOption;
			this.llSecondOption = llSecondOption;
			this.tvRewardsPoints = tvRewardsPoints;
			this.tvOptions = tvOptions;
			this.tvGiftMessage = tvGiftMessage;

		}
	}

	public ShoppingCartAdapter(Context context, int resource,
			List<Product> objects, UpdateUiFromAdapterListener updateUi) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		imageCacheLoader = new ImageCacheLoader(context);
		retailer = Helper.getSharedHelper().reatiler;
		this.quntities = new HashMap<String, String>();
		mUpdateUi = updateUi;
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
		final EditText edt_qty;
		Button btn_delete;
		Button btn_update;

		View vwFirstOptionRight1, vwSecondOptionRight1, vwFirstOptionUnderline, vwSecondOptionUnderline;
		RelativeLayout rlFirstOption, rlSecondOption;
		TextView tvFirstOptinLbl, tvSeconoptionLbl, tvFirstOptionValue, tvSecondOptionValue, tvRewardsPoints, tvOptions,tvGiftMessage;
		LinearLayout llFirstOption, llSecondOption;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_shopping_cart, null);
			btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
			edt_qty = (EditText) convertView.findViewById(R.id.edt_qty);
			btn_update = (Button) convertView.findViewById(R.id.btn_update);
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

			vwFirstOptionUnderline = (View) llFirstOption
					.findViewById(R.id.list_underline);
			vwSecondOptionUnderline = (View) llSecondOption
					.findViewById(R.id.list_underline);

			rlFirstOption = (RelativeLayout) llFirstOption
					.findViewById(R.id.relValue);
			rlSecondOption = (RelativeLayout) llSecondOption
					.findViewById(R.id.relValue);

			tvFirstOptinLbl = (TextView) llFirstOption
					.findViewById(R.id.tv_optionLable);
			tvSeconoptionLbl = (TextView) llSecondOption
					.findViewById(R.id.tv_optionLable);
			tvFirstOptionValue = (TextView) llFirstOption
					.findViewById(R.id.tv_optonsValue);
			tvSecondOptionValue = (TextView) llSecondOption
					.findViewById(R.id.tv_optonsValue);
			tvRewardsPoints = (TextView) convertView
					.findViewById(R.id.tvRewardsPoints);
			tvOptions = (TextView) convertView.findViewById(R.id.tvOptions);
			tvGiftMessage = (TextView)convertView.findViewById(R.id.tvGiftMessage);
			convertView.setTag(new ViewHolderShoppingCart(imageView, shortDesc,
					newPrice, oldPrice, edt_qty, btn_update, btn_delete, null,
					null, vwFirstOptionUnderline, vwSecondOptionUnderline,
					rlFirstOption, rlSecondOption, tvFirstOptinLbl,
					tvSeconoptionLbl, tvFirstOptionValue, tvSecondOptionValue,
					llFirstOption, llSecondOption, tvRewardsPoints, tvOptions,tvGiftMessage));
			edt_qty.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));
			edt_qty.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// v.requestFocusFromTouch();
					//
					// edt_qty.requestFocus();

					edt_qty.postDelayed(new Runnable() {
						public void run() {
							// InputMethodManager manager = (InputMethodManager)
							// context
							// .getSystemService(Context.INPUT_METHOD_SERVICE);
							// manager.showSoftInput(edt_qty, 0);
							// edt_qty.selectAll();
							edt_qty.requestFocus();
							edt_qty.selectAll();
						}
					}, 200);
					return false;
				}
			});

			edt_qty.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if ((arg2.getAction() == KeyEvent.ACTION_DOWN)
							&& (arg1 == KeyEvent.KEYCODE_ENTER)) {
						updateQTY(position, edt_qty.getText().toString());
						return true;
					}
					return false;
				}
			});
			edt_qty.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					try {
						String qty = s.toString();
						if (Integer.parseInt(qty) < 1) {
							qty = "1";
						}
						if (s != null) {

						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});

			btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					InputMethodManager inputMethodManager = (InputMethodManager) context
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							edt_qty.getWindowToken(), 0);
					edt_qty.clearFocus();
					Helper.getSharedHelper().shoppintCartList.remove(position);
					objects = Helper.getSharedHelper().shoppintCartList;
					Helper.getSharedHelper().discountPercent = "0";
					setQuantities();
					notifyDataSetChanged();
					mUpdateUi.updateCart();

				}
			});

			btn_update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// String qty = quntities.get(Integer.toString(position));
					// if (qty != null && Integer.parseInt(qty) <= 9) {
					// Product product =
					// Helper.getSharedHelper().shoppintCartList
					// .get(position);
					// product.setQty(qty);
					// setQuantities();
					// notifyDataSetChanged();
					// mUpdateUi.updateCart();
					// } else {
					//
					// Toast.makeText(context, "Maximum 9 qunatity allowed.",
					// Toast.LENGTH_LONG).show();
					// }
					InputMethodManager inputMethodManager = (InputMethodManager) context
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							edt_qty.getWindowToken(), 0);
					updateQTY(position, edt_qty.getText().toString());

				}
			});
			// convertView.setTag(holder);
		}
		holder = (ViewHolderShoppingCart) convertView.getTag();

		try {
			Product object = objects.get(position);

			holder.shortDesc.setText(object.getShortDescription().trim());
			int qty = Integer
					.parseInt(quntities.get(Integer.toString(position)));
			// Float price = Float.parseFloat(object.newPriceArr.get(Helper
			// .getSharedHelper().reatiler.defaultCurrency));

			Float price = Float.parseFloat(object.getNewPrice());
			
			if (object.getIsOptedGiftWrap()) {
				price += Float.parseFloat(Helper.getSharedHelper().reatiler.gift_price);
				holder.tvGiftMessage.setText(object.getGiftMsg());
				holder.tvGiftMessage.setVisibility(View.VISIBLE);
			}else{
				holder.tvGiftMessage.setVisibility(View.GONE);
			}
			float itemTotal = qty * price;
			holder.newPrice
					.setText(Helper.getSharedHelper().getCurrencySymbol(
							Helper.getSharedHelper().reatiler.defaultCurrency)
							+ ""
							+ Helper.getSharedHelper().conertfloatToSTring(
									itemTotal));
			if (object.getOldPrice() != null) {

				holder.oldPrice
						.setText(Helper
								.getSharedHelper()
								.getCurrencySymbol(
										Helper.getSharedHelper().reatiler.defaultCurrency)
								+ ""
								+ Helper.getSharedHelper().conertfloatToSTring(
										Float.parseFloat(object.getOldPrice())));
				holder.oldPrice.setVisibility(View.VISIBLE);
			}else{
				holder.oldPrice.setVisibility(View.GONE);
			}
			holder.edt_qty.setText(Integer.toString(qty));

			holder.btn_update
					.setBackgroundDrawable(getGradientDrawable(retailer
							.getHeaderColor()));

			imageView = holder.image;
			imageCacheLoader.displayImage(object.getImage(),
					R.drawable.image_placeholder, imageView);
			holder.shortDesc.setTypeface(Helper.getSharedHelper().boldFont);
			holder.btn_update.setTypeface(Helper.getSharedHelper().boldFont);
			holder.edt_qty.setTypeface(Helper.getSharedHelper().normalFont);
			holder.newPrice.setTypeface(Helper.getSharedHelper().boldFont);
			holder.oldPrice.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvFirstOptinLbl
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvSeconoptionLbl
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvFirstOptionValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvSecondOptionValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvRewardsPoints
					.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvOptions.setTypeface(Helper.getSharedHelper().normalFont);
			holder.tvGiftMessage.setTypeface(Helper.getSharedHelper().normalFont);

			holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

			String optiontext = null;
			if (object.getSelectedOption1() != null) {
				optiontext = object.getSelectedOption1();
			}
			if (object.getSelectedOption2() != null) {
				if (optiontext != null) {
					optiontext = optiontext + ", "
							+ object.getSelectedOption2();
				} else {
					optiontext = object.getSelectedOption2();
				}
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
					.equalsIgnoreCase("1")) {
				holder.tvRewardsPoints.setVisibility(View.VISIBLE);
			} else {
				holder.tvRewardsPoints.setVisibility(View.GONE);
			}
			SpannableString spanableText = new SpannableString(rewardPoints
					+ " Credit Points");
			spanableText.setSpan(new StyleSpan(Typeface.BOLD), 0,
					rewardPoints.length(), 0);
			holder.tvRewardsPoints.setText(spanableText);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	void updateQTY(int position, String qty) {
		// String qty = quntities.get(Integer.toString(position));
		if (qty != null && Integer.parseInt(qty) <= 9) {
			isUPdateQTY = true;
			quntities.put(Integer.toString(position), qty);
			Product product = Helper.getSharedHelper().shoppintCartList
					.get(position);
			product.setQty(qty);
			setQuantities();
			notifyDataSetChanged();
			mUpdateUi.updateCart();
		} else {

			Toast.makeText(context, "Maximum 9 qunatity allowed.",
					Toast.LENGTH_LONG).show();
		}
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

	public void updatePressed(View v) {
		int tag = Integer.parseInt((String) v.getTag());
		Product p = objects.get(tag);
	}

	void populateList(final Product product, final TextView tvOptinValue,
			RelativeLayout rlFirstOption, final View vwOptionUnderline,
			final int index) {

		// listOptions.add("S");
		// listOptions.add("M");
		// listOptions.add("L");
		// listOptions.add("Xl");
		// listOptions.add("XXl");

		rlFirstOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectedOptionIndex = index;
				listOptions = new ArrayList<String>();
				currentProduct = product;
				selectedOptionValue = tvOptinValue;
				ProductOption option = product.getProduct_options().get(index);
				String[] values = option.optionValue.split(",");
				for (String value : values) {
					listOptions.add(value);
				}
				showMenuOption(vwOptionUnderline);
			}
		});
	}

	// void populateSecondList(ProductOption option) {
	// tvSeconoptionLbl.setText(option.optionLabel);
	// secondList = new ArrayList<String>();
	// // secondList.add("Red color product");
	// // secondList.add("Green");
	// // secondList.add("Blue");
	// // secondList.add("Black");
	//
	// String[] values = option.optionValue.split(",");
	// for (String value : values) {
	// secondList.add(value);
	// }
	// rlSecondOption.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// selectedOptionIndex = 1;
	// showMenuOption();
	// }
	// });
	// }

	void showMenuOption(View anchorePoint) {
		listPopupWindow = new ListPopupWindow(this.context);
		listPopupWindow.setAdapter(new ArrayAdapter(this.context,
				R.layout.list_item, listOptions));
		listPopupWindow.setAnchorView(anchorePoint);
		// if (selectedOptionIndex == 0) {
		// listPopupWindow.setAdapter(new ArrayAdapter(this,
		// R.layout.list_item, firstList));
		// listPopupWindow.setAnchorView(vwFirstOptionUnderline);
		// } else if (selectedOptionIndex == 1) {
		// listPopupWindow.setAdapter(new ArrayAdapter(this,
		// R.layout.list_item, secondList));
		// listPopupWindow.setAnchorView(vwSecondOptionUnderline);
		// }

		listPopupWindow.setWidth(anchorePoint.getWidth());
		listPopupWindow
				.setHeight(android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		// listPopupWindow.set

		// listPopupWindow.setBackgroundDrawable(this.context.getResources()
		// .getDrawable(R.drawable.boarder_around));
		listPopupWindow.setModal(true);
		// listPopupWindow.getListView().setDividerHeight(2);
		// listPopupWindow.getListView().setDivider(new
		// ColorDrawable(Color.parseColor("#b6b6b6")));;
		listPopupWindow.setOnItemClickListener(this);
		listPopupWindow.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		selectedOptionValue.setText(listOptions.get(arg2));
		if (selectedOptionIndex == 0) {
			// tvFirstOptionValue.setText(firstList.get(arg2));
			currentProduct.setSelectedOption1(listOptions.get(arg2));
		} else if (selectedOptionIndex == 1) {
			// tvSecondOptionValue.setText(secondList.get(arg2));
			currentProduct.setSelectedOption2(listOptions.get(arg2));
		}
		listPopupWindow.dismiss();
	}

}
