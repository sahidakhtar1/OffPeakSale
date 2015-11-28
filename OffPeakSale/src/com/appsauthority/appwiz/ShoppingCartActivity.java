package com.appsauthority.appwiz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appauthority.appwiz.interfaces.RedeemRewadsCaller;
import com.appauthority.appwiz.interfaces.ShippingChargeCaller;
import com.appauthority.appwiz.interfaces.UserProfileCaller;
import com.appsauthority.appwiz.adapters.EnquiryListAdapter;
import com.appsauthority.appwiz.adapters.ShoppingCartAdapter;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.EarliestSchedule;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.UpdateUiFromAdapterListener;
import com.offpeaksale.consumer.R;

public class ShoppingCartActivity extends BaseActivity implements
		UpdateUiFromAdapterListener, ShippingChargeCaller, OnItemClickListener,
		RedeemRewadsCaller, UserProfileCaller {

	private ShoppingCartAdapter adapter;
	private EnquiryListAdapter eshopAdapter;
	private ListView listview;

	LinearLayout llShippingCharge, llShippingChageValue, llCollectionAddress, llDeleveryTime,
			llDeliveryOption;
	TextView tv_shipping_charge_lbl;
	TextView tv_shipping_chargeValue;
	TextView tv_grandToatlLBL, tv_grandToatlValue;
	View lineTop;
	View lineBot;
	Button btn_emailEnquiry;

	Retailer retailer;

	private TextView txtCartTotal;
	private LinearLayout cartView;

	private TextView textViewHeader;
	TextView tvEnterCode;
	TextView tvCheckout, tvCOD;

	TextView tvDiscountInfo;
	ImageView imageView;
	EditText edtCoupon;
	String countryId;

	RelativeLayout rlGrandTotalBg, rlCheckOut;
	LinearLayout llShipping;

	RadioButton rdHomeDelivery, rdStoreCollection;
	RadioGroup radioGroup;
	LinearLayout bottom_view;
	TextView tvDeliveryTimeSchedule, tvDeliveryTimeValue,
			tvDeliveryDateSchedule, tvDeliveryDateValue, tvRewards,
			tvRewardsValue, tvEarnedRewardLbl, tvDiscount, tvDiscountValue,
			tvShippingcharge, tvShippingChargeValue, tvEarnedRewardValue,
			tvCollectionAddresslbl, tvCollectionAddressValue;

	View /* vwDeliveryRightBar, vwRewardsRightBar, */vwDeliveryUnderLine,
			vwRewardUnderLine;

	RelativeLayout rlDeliveryTime, rlDeliveryDate, rlCollectionAddress,
			rlRewards, rlCOD;
	LinearLayout llDiscount;

	TextView tvCashCreditValue, tvTotalRewardsValue, tvResultMsg,
			tvEstimatedDelivery, etTimeSlot, etCollectionAddress;
	EditText etRewards;

	EditText etDay, etMon, etYear;
	private SimpleDateFormat sdf;
	ArrayAdapter<String> timeSlotAdapter;
	List<String> list, listCollectionAddress;
	ListPopupWindow listPopupWindow, listCollectionAddressWindow;
	private SharedPreferences spref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.shopping_cart_layout);
		spref = PreferenceManager.getDefaultSharedPreferences(this);
		Helper.getSharedHelper().deliveryOptionSelectedIndex = 0;

		retailer = Helper.getSharedHelper().reatiler;
		listview = (ListView) findViewById(R.id.lv_items);

		sdf = new SimpleDateFormat("dd-MMM-yy");

		list = new ArrayList<String>();
		listCollectionAddress = new ArrayList<String>();
		/*
		 * if (Helper.getSharedHelper().enableDelivery.equalsIgnoreCase("1")) {
		 * String[] timeSlots =
		 * Helper.getSharedHelper().reatiler.deliveryTimeSlots .split(","); for
		 * (int i = 0; i < timeSlots.length; i++) { list.add(timeSlots[i]); } }
		 * else if (Helper.getSharedHelper().reatiler.enableCollection
		 * .equalsIgnoreCase("1")) { String[] timeSlots =
		 * Helper.getSharedHelper().reatiler.collectionTimeSlots .split(",");
		 * for (int i = 0; i < timeSlots.length; i++) { list.add(timeSlots[i]);
		 * }
		 * 
		 * String[] collectionAddresss =
		 * Helper.getSharedHelper().reatiler.collectionAddress .split(",");
		 * 
		 * for (int i = 0; i < collectionAddresss.length; i++) { if (i == 0) {
		 * Helper.getSharedHelper().selectedCollectionAddress =
		 * collectionAddresss[0]; }
		 * listCollectionAddress.add(collectionAddresss[i]); }
		 * 
		 * } else {
		 * 
		 * }
		 */
		tv_grandToatlLBL = (TextView) 
				findViewById(R.id.tvOptionLbl);
		tv_grandToatlValue = (TextView) 
				findViewById(R.id.tvOptionValue);
		tv_grandToatlLBL.setText(R.string.grand_total);
		initailzeFotterView();
		loadListView(0);

		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		tvEnterCode = (TextView) findViewById(R.id.tvEnterCode);
		textViewHeader.setText("My Cart");
		llShippingCharge = (LinearLayout) findViewById(R.id.llShippingCharge);
		llShippingChageValue = (LinearLayout) findViewById(R.id.llShippingChageValue);
		// tv_grandToatlLBL = (TextView) findViewById(R.id.tv_grandToatlLBL);
		tv_shipping_charge_lbl = (TextView) findViewById(R.id.tv_shipping_charge_lbl);
		// tv_grandToatlValue = (TextView)
		// findViewById(R.id.tv_grandToatlValue);
		tv_shipping_chargeValue = (TextView) findViewById(R.id.tv_shipping_chargeValue);
		lineTop = (View) findViewById(R.id.lineTop);
		lineBot = (View) findViewById(R.id.lineBot);
		btn_emailEnquiry = (Button) findViewById(R.id.btn_emailEnquiry);
		cartView = (LinearLayout) findViewById(R.id.cartView);
		tvCheckout = (TextView) findViewById(R.id.tvCheckout);
		tvCOD = (TextView) findViewById(R.id.tvCOD);
		LinearLayout llDevider = (LinearLayout) findViewById(R.id.llDevider);
		rlCOD = (RelativeLayout) findViewById(R.id.rlCOD);
		rlGrandTotalBg = (RelativeLayout) findViewById(R.id.rlGrandTotalBg);
		rlCheckOut = (RelativeLayout) findViewById(R.id.rlCheckOut);
		rlCheckOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkoutPressed(false);
			}
		});
		rlCOD.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkoutPressed(true);
			}
		});

		txtCartTotal = (TextView) findViewById(R.id.txtCartTotal);
		txtCartTotal.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		tvCheckout.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		tvCOD.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		// tvEnterCode.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));
		LinearLayout checkoutLL = (LinearLayout) findViewById(R.id.checkoutLL);
		LinearLayout enquiryLL = (LinearLayout) findViewById(R.id.enquiryLL);
		SharedPreferences spref = PreferenceManager
				.getDefaultSharedPreferences(this);
		countryId = spref.getString(Constants.KEY_COUNTRY_ID, "");
		if (Helper.getSharedHelper().reatiler.enableCOD.equals("1")) {
			ColorDrawable cd = new ColorDrawable(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			rlCOD.setVisibility(View.VISIBLE);
			llDevider.setVisibility(View.VISIBLE);

		} else {
			rlCOD.setVisibility(View.GONE);
			llDevider.setVisibility(View.GONE);
		}

		if (Helper.getSharedHelper().reatiler.enablePay.equals("1")) {

			rlCheckOut.setVisibility(View.VISIBLE);

		} else if (Helper.getSharedHelper().reatiler.enableVerit.equals("1")) {

			rlCheckOut.setVisibility(View.VISIBLE);

		} else {
			rlCheckOut.setVisibility(View.GONE);
		}
		llShippingChageValue.setVisibility(View.VISIBLE);
		checkoutLL.setVisibility(View.VISIBLE);
		enquiryLL.setVisibility(View.GONE);
		if (countryId.length() > 0) {
			ShippingChargeDataHandler scdh = new ShippingChargeDataHandler(
					countryId, this);
			scdh.getShippingCharge();
		} else {
			llShippingChageValue.setVisibility(View.GONE);
			tv_shipping_charge_lbl
					.setText(R.string.shipping_charge_palceholder);
		}
		try {
			// llDevider.setBackgroundDrawable(Helper.getSharedHelper()
			// .getGradientDrawable(retailer.getRetailerTextColor()));
			llDevider.setBackgroundColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			lineBot.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));

			btn_emailEnquiry.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			String headerColor = retailer.getHeaderColor();
			GradientDrawable gd;

			// gd = new
			// GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
			// new int[] { Color.parseColor("#80" + headerColor),
			// Color.parseColor("#" + headerColor),
			// Color.parseColor("#" + headerColor) });
			// gd.setCornerRadius(10);
			ColorDrawable cd = new ColorDrawable(Color.parseColor("#"
					+ headerColor));
			btn_emailEnquiry.setBackgroundDrawable(cd);
			// btn_Shopping.setBackgroundDrawable(getGradientDrawable(retailer
			// .getHeaderColor()));

			// tv_grandToatlLBL.setTextColor(Color.parseColor("#"
			// + retailer.getRetailerTextColor()));
			// tv_grandToatlValue.setTextColor(Color.parseColor("#"
			// + retailer.getRetailerTextColor()));
			tvEnterCode.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			GradientDrawable bgShape = (GradientDrawable) rlGrandTotalBg
					.getBackground();
			bgShape.setColor(Color.parseColor("#" + retailer.getHeaderColor()));
		} catch (Exception e) {

		}
		try {
			setHeaderTheme(this, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
		} catch (Exception e) {
		}
		updatGrandTotal();
		setFont();

		tvEnterCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCouponAlert();
			}
		});

		ImageView imageViewOverflow = (ImageView) findViewById(R.id.imageViewOverflow);
		// imageViewOverflow.setImageResource(R.drawable.backbutton);
		imageViewOverflow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		ImageView imgCart = (ImageView) findViewById(R.id.imgCart);
		if (Helper.getSharedHelper().reatiler.appIconColor != null
				&& Helper.getSharedHelper().reatiler.appIconColor
						.equalsIgnoreCase("black")) {
			imageViewOverflow
					.setBackgroundResource(R.drawable.backbutton_black);
			imgCart.setBackgroundResource(R.drawable.shop_cart_black);
		} else {
			imageViewOverflow.setBackgroundResource(R.drawable.backbutton);
			imgCart.setBackgroundResource(R.drawable.shop_cart);
		}

	}

	void initailzeFotterView() {
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.cart_bottom_layout, null);
		bottom_view = (LinearLayout) view.findViewById(R.id.bottom_view);
		listview.addFooterView(view, "", false);

		llDeleveryTime = (LinearLayout) view
				.findViewById(R.id.llDeleveryTime);

		LinearLayout llDeleveryDate = (LinearLayout) view
				.findViewById(R.id.llDeleveryDate);

		llCollectionAddress = (LinearLayout) view
				.findViewById(R.id.llCollectionAddress);
		llDeliveryOption = (LinearLayout) view
				.findViewById(R.id.llDeliveryOption);
		radioGroup = (RadioGroup) llDeliveryOption
				.findViewById(R.id.radioGroup);
		rdHomeDelivery = (RadioButton) llDeliveryOption
				.findViewById(R.id.rdHomeDelivery);
		rdStoreCollection = (RadioButton) llDeliveryOption
				.findViewById(R.id.rdStoreCollection);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rdHomeDelivery) {
					Helper.getSharedHelper().deliveryOptionSelectedIndex = 0;
					updateDeliveryOption();
				} else {
					Helper.getSharedHelper().deliveryOptionSelectedIndex = 1;
					updateDeliveryOption();
				}
				updatGrandTotal();

			}
		});

		// rdHomeDelivery.set(Color.parseColor("#"
		// + retailer.getHeaderColor()));

		LinearLayout llRewards = (LinearLayout) view
				.findViewById(R.id.llRewards);
		llDiscount = (LinearLayout) view.findViewById(R.id.llDiscount);
		llShipping = (LinearLayout) view.findViewById(R.id.llShipping);

		LinearLayout llGrandTotal = (LinearLayout) 
				findViewById(R.id.llGrandTotal);
		LinearLayout llEarnedrewads = (LinearLayout) view
				.findViewById(R.id.llEarnedrewads);
		
		tvDeliveryTimeSchedule = (TextView) llDeleveryTime
				.findViewById(R.id.tv_optionLable);
		tvDeliveryDateSchedule = (TextView) llDeleveryDate
				.findViewById(R.id.tv_optionLable);
		rlDeliveryTime = (RelativeLayout) llDeleveryTime
				.findViewById(R.id.relValue);
		rlDeliveryDate = (RelativeLayout) llDeleveryDate
				.findViewById(R.id.relValue);
		rlCollectionAddress = (RelativeLayout) llCollectionAddress
				.findViewById(R.id.relValue);

		tvCollectionAddresslbl = (TextView) llCollectionAddress
				.findViewById(R.id.tv_optionLable);
		tvCollectionAddressValue = (TextView) llCollectionAddress
				.findViewById(R.id.tv_optonsValue);
		// vwDeliveryRightBar = (View) llDeleveryTime
		// .findViewById(R.id.list_right1);
		vwDeliveryUnderLine = (View) llDeleveryTime
				.findViewById(R.id.list_underline);
		tvDeliveryTimeValue = (TextView) llDeleveryTime
				.findViewById(R.id.tv_optonsValue);
		tvDeliveryDateValue = (TextView) llDeleveryDate
				.findViewById(R.id.tv_optonsValue);

		tvRewards = (TextView) llRewards.findViewById(R.id.tv_optionLable);
		rlRewards = (RelativeLayout) llRewards.findViewById(R.id.relValue);
		// vwRewardsRightBar = (View) llRewards.findViewById(R.id.list_right1);
		vwRewardUnderLine = (View) llRewards.findViewById(R.id.list_underline);
		tvRewardsValue = (TextView) llRewards.findViewById(R.id.tv_optonsValue);
		tvEarnedRewardLbl = (TextView) llEarnedrewads
				.findViewById(R.id.tvOptionLbl);
		tvEarnedRewardValue = (TextView) llEarnedrewads
				.findViewById(R.id.tvOptionValue);
		if (Helper.getSharedHelper().reatiler.enableRewards
				.equalsIgnoreCase("1")) {

		} else {
			llEarnedrewads.setVisibility(View.GONE);
			llRewards.setVisibility(View.GONE);
		}

		tvDiscount = (TextView) llDiscount.findViewById(R.id.tvOptionLbl);
		tvDiscountValue = (TextView) llDiscount
				.findViewById(R.id.tvOptionValue);
		tvDiscountValue.setText("("
				+ Helper.getSharedHelper().getCurrencySymbol(
						Helper.getSharedHelper().reatiler.defaultCurrency)
				+ " "
				+ Helper.getSharedHelper().conertfloatToSTring(
						Helper.getSharedHelper().discountAmount) + ")");

		tvShippingcharge = (TextView) llShipping.findViewById(R.id.tvOptionLbl);
		tvShippingChargeValue = (TextView) llShipping
				.findViewById(R.id.tvOptionValue);
		if (Helper.getSharedHelper().reatiler.enable_shipping
				.equalsIgnoreCase("1")
				|| Helper.getSharedHelper().enable_shipping
						.equalsIgnoreCase("1")) {

		} else {
			llShipping.setVisibility(View.GONE);
		}

		tvRewards.setText("Redeem Credit Points");
		tvDiscount.setText("Discount Amount");
		tvShippingcharge.setText("Shipping Fee");
		tvShippingChargeValue.setText("Charges Apply");
		
		tvRewardsValue.setText("(" + Helper.getSharedHelper().redeemPoints
				+ ")");
		tvEarnedRewardLbl.setText("Available Credit Points");
		tvEarnedRewardValue.setText(Helper.getSharedHelper().rewardPoints);
		if (Helper.getSharedHelper().enableDelivery.equalsIgnoreCase("1")
				&& Helper.getSharedHelper().reatiler.enableCollection
						.equalsIgnoreCase("1")) {
			llDeliveryOption.setVisibility(View.VISIBLE);
			Helper.getSharedHelper().deliveryOptionSelectedIndex = 0;
		} else if (Helper.getSharedHelper().enableDelivery
				.equalsIgnoreCase("1")) {
			llDeliveryOption.setVisibility(View.GONE);
			Helper.getSharedHelper().deliveryOptionSelectedIndex = 0;
		} else if (Helper.getSharedHelper().reatiler.enableCollection
				.equalsIgnoreCase("1")) {
			llDeliveryOption.setVisibility(View.GONE);
			Helper.getSharedHelper().deliveryOptionSelectedIndex = 1;
		} else {
			llDeliveryOption.setVisibility(View.GONE);
		}
		if (Helper.getSharedHelper().enableDelivery.equalsIgnoreCase("1")) {

			llDeleveryTime.setVisibility(View.VISIBLE);
			llDeleveryTime.setVisibility(View.VISIBLE);
			// String deleveryDate = Helper.getSharedHelper().dateByAddingDays(
			// Integer.parseInt(Helper.getSharedHelper().deliveryDays));
			// Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
			// .replace("-", " ") + ", " + list.get(0);
			// tvDeliveryDateValue.setText(deleveryDate);
			// tvDeliveryTimeValue.setText(list.get(0));
			//
			// tvDeliveryDateSchedule.setText("Delivery Date");
			// tvDeliveryTimeSchedule.setText("Delivery Time");
			// llCollectionAddress.setVisibility(View.GONE);
		} else if (Helper.getSharedHelper().reatiler.enableCollection
				.equalsIgnoreCase("1")) {

			llDeleveryTime.setVisibility(View.VISIBLE);
			llDeleveryTime.setVisibility(View.VISIBLE);
			llCollectionAddress.setVisibility(View.VISIBLE);
			// String deleveryDate = Helper
			// .getSharedHelper()
			// .dateByAddingDays(
			// Integer.parseInt(Helper.getSharedHelper().reatiler.collectionDays));
			// Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
			// .replace("-", " ") + ", " + list.get(0);
			// tvDeliveryDateValue.setText(deleveryDate);
			// tvDeliveryTimeValue.setText(list.get(0));
			//
			// tvDeliveryDateSchedule.setText("Collection Date");
			// tvDeliveryTimeSchedule.setText("Collection Time");
			// tvCollectionAddresslbl.setText("Collection Address");
			// tvCollectionAddressValue
			// .setText(Helper.getSharedHelper().selectedCollectionAddress);
			// // tvCollectionAddressValue.setLines(3);

		} else {
			llDeleveryDate.setVisibility(View.GONE);
			llDeleveryTime.setVisibility(View.GONE);
			llCollectionAddress.setVisibility(View.GONE);
		}

		rlRewards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Boolean isLoggedIn = spref.getBoolean(
						Constants.KEY_IS_USER_LOGGED_IN, false);
				if (isLoggedIn) {
					showRewardPopUp();
				}else{
					Intent intent = new Intent(ShoppingCartActivity.this, ProfileActivity.class);
					intent.putExtra("FROM", "CARTLOGIN");
					startActivity(intent);
				}
				
			}
		});
		rlDeliveryTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDeliveryTimePopUp();
			}
		});
		rlDeliveryDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDeliveryTimePopUp();
			}
		});
		rlCollectionAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDeliveryTimePopUp();
			}
		});
		updateDeliveryOption();
		if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
			rdHomeDelivery.setChecked(true);
		} else {
			rdStoreCollection.setChecked(true);
		}
	}

	void updateDeliveryOption() {
		try {

			list.clear();
			listCollectionAddress.clear();
			;

			if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
				Boolean isTimeLapse = Helper.getSharedHelper().reatiler.deliveryType.equalsIgnoreCase("1")?true:false;
				String[] timeSlots = Helper.getSharedHelper().reatiler.deliveryTimeSlots
						.split(",");
				EarliestSchedule earliestSchedule = Helper
						.getSharedHelper()
						.getDefalultEarliestDate(
								Integer.parseInt(Helper.getSharedHelper().deliveryDays),
								Integer.parseInt(Helper.getSharedHelper().reatiler.deliveryHours),
								timeSlots,isTimeLapse);
				String deleveryDate = earliestSchedule.earliestDate;
				if (!isTimeLapse) {
					for (int i = 0; i < earliestSchedule.possibleTimeSlots.size(); i++) {
						list.add(Helper.getSharedHelper().convert24hrSlotsTo12hr(earliestSchedule.possibleTimeSlots.get(i)));
					}
					tvDeliveryTimeValue.setText(list.get(0));
					llDeleveryTime.setVisibility(View.VISIBLE);
					Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
							.replace("-", " ") + ", " + list.get(0);
				}else{
					llDeleveryTime.setVisibility(View.GONE);
					Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
							.replace("-", " ") ;
				}

				
				llCollectionAddress.setVisibility(View.GONE);
				
				
				tvDeliveryDateValue.setText(deleveryDate);
				

				tvDeliveryDateSchedule.setText("Delivery Date");
				tvDeliveryTimeSchedule.setText("Delivery Time");
				llShipping.setVisibility(View.VISIBLE);

			} else {
				Boolean isTimeLapse = Helper.getSharedHelper().reatiler.collectionType.equalsIgnoreCase("1")?true:false;
				String[] timeSlots = Helper.getSharedHelper().reatiler.collectionTimeSlots
						.split(",");
				EarliestSchedule earliestSchedule = Helper
						.getSharedHelper()
						.getDefalultEarliestDate(
								Integer.parseInt(Helper.getSharedHelper().reatiler.collectionDays),
								Integer.parseInt(Helper.getSharedHelper().reatiler.collectionHours),
								timeSlots,isTimeLapse);
				String deleveryDate = earliestSchedule.earliestDate;

				if (!isTimeLapse) {
					for (int i = 0; i < earliestSchedule.possibleTimeSlots.size(); i++) {
						list.add(Helper.getSharedHelper().convert24hrSlotsTo12hr(earliestSchedule.possibleTimeSlots.get(i)));
					}
					tvDeliveryTimeValue.setText(list.get(0));
					llDeleveryTime.setVisibility(View.VISIBLE);
					Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
							.replace("-", " ") + ", " + list.get(0);
				}else{
					llDeleveryTime.setVisibility(View.GONE);
					Helper.getSharedHelper().deleveryScheduleSelected = deleveryDate
							.replace("-", " ");
				}
				

				String[] collectionAddresss = Helper.getSharedHelper().reatiler.collectionAddress
						.split(",");

				for (int i = 0; i < collectionAddresss.length; i++) {
					if (i == 0) {
						Helper.getSharedHelper().selectedCollectionAddress = collectionAddresss[0];
					}
					listCollectionAddress.add(collectionAddresss[i]);
				}
				llCollectionAddress.setVisibility(View.VISIBLE);
				
				
				tvDeliveryDateValue.setText(deleveryDate);
				

				tvDeliveryDateSchedule.setText("Collection Date");
				tvDeliveryTimeSchedule.setText("Collection Time");
				tvCollectionAddresslbl.setText("Collection Address");
				tvCollectionAddressValue
						.setText(Helper.getSharedHelper().selectedCollectionAddress);
				llShipping.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (Helper.getSharedHelper().disablePayment.equals("1")) {
		//
		// } else {

		countryId = spref.getString(Constants.KEY_COUNTRY_ID, "");
		if (countryId.length() > 0) {
			ShippingChargeDataHandler scdh = new ShippingChargeDataHandler(
					countryId, this);
			scdh.getShippingCharge();
		} else {
			llShippingChageValue.setVisibility(View.GONE);
			tv_shipping_charge_lbl
					.setText(R.string.shipping_charge_palceholder);
		}
		// }

		updateCart();
		String emailId = spref.getString(Constants.KEY_EMAIL, "");
		if (!emailId.equalsIgnoreCase("")) {
			new UserProfileDataHandler(emailId, this);
		}
	}

	void setFont() {
		try {
			tvEnterCode.setTypeface(Helper.getSharedHelper().normalFont);
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			txtCartTotal.setTypeface(Helper.getSharedHelper().normalFont);
			tv_grandToatlLBL.setTypeface(Helper.getSharedHelper().boldFont);
			tv_shipping_charge_lbl
					.setTypeface(Helper.getSharedHelper().normalFont);
			tv_grandToatlValue.setTypeface(Helper.getSharedHelper().boldFont);
			tv_shipping_chargeValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvCheckout.setTypeface(Helper.getSharedHelper().boldFont);
			tvCOD.setTypeface(Helper.getSharedHelper().boldFont);
			tvDeliveryDateSchedule
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvDeliveryDateValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvDeliveryTimeSchedule
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvCollectionAddresslbl
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvCollectionAddressValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvDeliveryTimeValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			rdHomeDelivery.setTypeface(Helper.getSharedHelper().normalFont);
			rdStoreCollection.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewards.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardsValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvDiscount.setTypeface(Helper.getSharedHelper().normalFont);
			tvDiscountValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvShippingcharge.setTypeface(Helper.getSharedHelper().normalFont);
			tvShippingChargeValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvEarnedRewardLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvEarnedRewardValue
					.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

	}

	private void loadListView(int id) {
		// sqLiteHelper.openDataBase();
		// productList = sqLiteHelper.getProducts(id);
		// sqLiteHelper.close();

		// if (Helper.getSharedHelper().disablePayment.equals("1")) {
		// eshopAdapter = new EnquiryListAdapter(context, R.layout.row_eshop,
		// Helper.getSharedHelper().shoppintCartList,
		// (UpdateUiFromAdapterListener) this);
		//
		// listview.setAdapter(eshopAdapter);
		// } else {
		adapter = new ShoppingCartAdapter(context, R.layout.row_shopping_cart,
				Helper.getSharedHelper().shoppintCartList,
				(UpdateUiFromAdapterListener) this);

		listview.setAdapter(adapter);
		// }

	}

	public void continueShopping(View v) {
		// finish();
		// Intent intent = new Intent(this, EShopFragmentActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
	}

	public void checkoutPressed(Boolean isCod) {
		if (Helper.getSharedHelper().shoppintCartList.size() == 0) {
			Toast.makeText(this, "Your shopping cart is empty.",
					Toast.LENGTH_SHORT).show();
			;
		} else {
			Intent intent = new Intent(this, ProfileActivity.class);
			intent.putExtra("FROM", "ESHOP");
			intent.putExtra("isCod", isCod);
			startActivity(intent);
		}

	}

	public void cartPressed(View v) {

	}

	void updatGrandTotal() {
		float total = Float.parseFloat(Helper.getSharedHelper()
				.getCartTotalAmount());
		if (total < Float.parseFloat(Helper.getSharedHelper().redeemPoints)) {
			Helper.getSharedHelper().redeemPoints = "0";
			tvRewardsValue.setText("(" + Helper.getSharedHelper().redeemPoints
					+ ")");

		}
		if (countryId.length() > 0) {
			if (Helper.getSharedHelper().shippingCharge > 0
					&& Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
				if (Helper.getSharedHelper().freeAmount == 0
						|| total < Helper.getSharedHelper().freeAmount) {
					tvShippingChargeValue
							.setText(Helper
									.getSharedHelper()
									.getCurrencySymbol(
											Helper.getSharedHelper().reatiler.defaultCurrency)
									+ " "
									+ Helper.getSharedHelper()
											.conertfloatToSTring(
													Helper.getSharedHelper().shippingCharge));
					total += Helper.getSharedHelper().shippingCharge;
				} else {
					tvShippingChargeValue.setText("Free");
				}
			} else {
				tvShippingChargeValue.setText("Free");
			}
		}

		txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());

		total = total - Integer.parseInt(Helper.getSharedHelper().redeemPoints);

		tv_grandToatlValue
				.setText(Helper.getSharedHelper().reatiler.defaultCurrency
						+ " "
						+ Helper.getSharedHelper().conertfloatToSTring(total));

		// if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
		// cartView.setVisibility(View.VISIBLE);
		// txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
		// } else {
		// cartView.setVisibility(View.GONE);
		// }
		if (Helper.getSharedHelper().enableCreditCode.equals("1")) {
			if (Helper.getSharedHelper().discountPercent.equalsIgnoreCase("0")) {
				tvEnterCode.setVisibility(View.VISIBLE);
				cartView.setVisibility(View.GONE);
				llDiscount.setVisibility(View.GONE);
			} else {
				tvEnterCode.setVisibility(View.GONE);
				cartView.setVisibility(View.VISIBLE);
				llDiscount.setVisibility(View.VISIBLE);
			}

		} else if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			tvEnterCode.setVisibility(View.GONE);
			cartView.setVisibility(View.VISIBLE);
			txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
		} else {
			cartView.setVisibility(View.GONE);
		}

	}

	@Override
	public void updateCart() {
		// TODO Auto-generated method stub
		updatGrandTotal();
	}

	private void showCouponAlert() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_enter_code);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			Button buttonApply = (Button) dialog.findViewById(R.id.buttonApply);
			Button buttonClose = (Button) dialog.findViewById(R.id.buttonClose);
			View lineTop = (View) dialog.findViewById(R.id.lineTop);
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			imageView = (ImageView) dialog.findViewById(R.id.imageView);
			imageView.setVisibility(View.GONE);
			tvDiscountInfo = (TextView) dialog
					.findViewById(R.id.tvDiscountInfo);
			tvDiscountInfo.setVisibility(View.GONE);

			edtCoupon = (EditText) dialog.findViewById(R.id.edtCoupon);

			edtCoupon
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			buttonApply.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonClose.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			buttonApply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new AsyncApplyCoupon().execute();
				}
			});
			buttonClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});

			buttonApply.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonClose.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonApply.setTypeface(Helper.getSharedHelper().boldFont);
			buttonClose.setTypeface(Helper.getSharedHelper().boldFont);
			tvDiscountInfo.setTypeface(Helper.getSharedHelper().boldFont);
			dialog.show();
		} catch (Exception e) {
		}

	}

	void showRewardPopUp() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_rewards);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			RelativeLayout btnClose = (RelativeLayout) dialog
					.findViewById(R.id.btnClose);
			TextView tvCashCreditLbl = (TextView) dialog
					.findViewById(R.id.tvCashCreditLbl);
			tvCashCreditValue = (TextView) dialog
					.findViewById(R.id.tvCashCreditValue);
			TextView tvTotalRewardsLbl = (TextView) dialog
					.findViewById(R.id.tvTotalRewardsLbl);
			tvTotalRewardsValue = (TextView) dialog
					.findViewById(R.id.tvTotalRewardsValue);
			tvCashCreditValue.setText(Helper.getSharedHelper().rewardPoints
					+ " points");
			tvTotalRewardsValue.setText(Helper.getSharedHelper().currency_code
					+ " " + Helper.getSharedHelper().rewardPoints);
			View lineTop = (View) dialog.findViewById(R.id.lineTop);
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			etRewards = (EditText) dialog.findViewById(R.id.etRewards);
			Button btnRedeemRewards = (Button) dialog
					.findViewById(R.id.btnRedeemRewards);
			tvResultMsg = (TextView) dialog.findViewById(R.id.tvResultMsg);

			etRewards
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			btnRedeemRewards.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			etRewards.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// new AsyncApplyCoupon().execute();
				}
			});
			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			btnRedeemRewards.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String pointsToRedeem = etRewards.getText().toString();
					float cartTotal = Float.parseFloat(Helper.getSharedHelper()
							.getCartTotalAmount());
					if (Float.parseFloat(pointsToRedeem) > Float
							.parseFloat(Helper.getSharedHelper().rewardPoints)) {
						Toast.makeText(
								ShoppingCartActivity.this,
								"Insufficient Reward Points",
								Toast.LENGTH_LONG).show();
						return;
					} else if (Integer.parseInt(pointsToRedeem) > cartTotal) {
						Toast.makeText(
								ShoppingCartActivity.this,
								"Entered reward point is higher than cart total amount",
								Toast.LENGTH_LONG).show();
						return;
					}
					RedeemRewardsHandler rrh = new RedeemRewardsHandler(
							pointsToRedeem, spref.getString(
									Constants.KEY_EMAIL, ""),
							ShoppingCartActivity.this);
					rrh.redeemRewards();
				}
			});

			btnRedeemRewards.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			btnRedeemRewards.setTypeface(Helper.getSharedHelper().boldFont);
			tvCashCreditLbl.setTypeface(Helper.getSharedHelper().boldFont);
			tvTotalRewardsLbl.setTypeface(Helper.getSharedHelper().boldFont);

			tvCashCreditValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvTotalRewardsValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			etRewards.setTypeface(Helper.getSharedHelper().normalFont);
			tvResultMsg.setTypeface(Helper.getSharedHelper().normalFont);
			dialog.show();
		} catch (Exception e) {
		}
	}

	void showDeliveryTimePopUp() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_delivery_slots);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			LinearLayout llStoreAddress = (LinearLayout) dialog
					.findViewById(R.id.llStoreAddress);
			LinearLayout llDeliveryTime = (LinearLayout)dialog.findViewById(R.id.llDeliveryTime);

			RelativeLayout btnClose = (RelativeLayout) dialog
					.findViewById(R.id.btnClose);
			TextView tvDeliveryTitle = (TextView) dialog
					.findViewById(R.id.tvDeliveryTitle);
			tvEstimatedDelivery = (TextView) dialog
					.findViewById(R.id.tvEstimatedDelivery);

			TextView tvSelectDeliveryTitle = (TextView) dialog
					.findViewById(R.id.tvSelectDeliveryTitle);
			TextView tvSelectDeliveryTimeTitle = (TextView) dialog
					.findViewById(R.id.tvSelectDeliveryTimeTitle);

			etDay = (EditText) dialog.findViewById(R.id.etDay);
			etMon = (EditText) dialog.findViewById(R.id.etMon);
			etYear = (EditText) dialog.findViewById(R.id.etYear);
			etTimeSlot = (EditText) dialog.findViewById(R.id.etTimeSlot);
			etCollectionAddress = (EditText) dialog
					.findViewById(R.id.etCollectionAddress);
			etCollectionAddress
					.setText(Helper.getSharedHelper().selectedCollectionAddress);

			View lineTop = (View) dialog.findViewById(R.id.lineTop);
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			Button btnDone = (Button) dialog.findViewById(R.id.btnDone);

			etDay.setBackgroundDrawable(getGradientDrawableEditText(retailer
					.getHeaderColor()));
			etMon.setBackgroundDrawable(getGradientDrawableEditText(retailer
					.getHeaderColor()));
			etYear.setBackgroundDrawable(getGradientDrawableEditText(retailer
					.getHeaderColor()));
			etTimeSlot
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			etCollectionAddress
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			btnDone.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			btnDone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					int year = Integer.parseInt(etYear.getText().toString());
					// year -= 2000;
					String delevrySchedule = etDay.getText().toString() + "-"
							+ etMon.getText().toString() + "-" + year;
					tvDeliveryDateValue.setText(delevrySchedule);
					tvDeliveryTimeValue.setText(etTimeSlot.getText().toString()
							.trim());
					tvCollectionAddressValue.setText(etCollectionAddress
							.getText().toString().trim());
					Helper.getSharedHelper().selectedCollectionAddress = etCollectionAddress
							.getText().toString();
					Helper.getSharedHelper().deleveryScheduleSelected = etDay
							.getText().toString()
							+ " "
							+ etMon.getText().toString()
							+ " "
							+ etYear.getText().toString()
							+ ","
							+ etTimeSlot.getText().toString().trim();

				}
			});

			btnDone.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			tvDeliveryTitle.setTypeface(Helper.getSharedHelper().normalFont);
			tvEstimatedDelivery
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvSelectDeliveryTitle
					.setTypeface(Helper.getSharedHelper().boldFont);

			tvSelectDeliveryTimeTitle
					.setTypeface(Helper.getSharedHelper().boldFont);
			etDay.setTypeface(Helper.getSharedHelper().normalFont);
			etMon.setTypeface(Helper.getSharedHelper().normalFont);
			etYear.setTypeface(Helper.getSharedHelper().normalFont);
			etTimeSlot.setTypeface(Helper.getSharedHelper().normalFont);
			

			etDay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDatePicker();
				}
			});
			etMon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDatePicker();
				}
			});
			etYear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDatePicker();
				}
			});
			etTimeSlot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// spTimeSlots.performClick();
					ShowTimeSlotPopup();
				}
			});
			etCollectionAddress.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// spTimeSlots.performClick();
					ShowCollectionAddressPopup();
				}
			});

			dialog.show();
			if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
				llStoreAddress.setVisibility(View.GONE);
				if (Helper.getSharedHelper().reatiler.deliveryType.equalsIgnoreCase("1")) {
					llDeliveryTime.setVisibility(View.GONE);
					tvEstimatedDelivery.setText(tvDeliveryDateValue.getText()
							.toString());
				}else{
					llDeliveryTime.setVisibility(View.VISIBLE);
					tvEstimatedDelivery.setText(tvDeliveryDateValue.getText()
							.toString()
							+ ", "
							+ tvDeliveryTimeValue.getText().toString());
				}
			} else if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 1) {
				tvDeliveryTitle.setText("Earliest date of collection");
				tvSelectDeliveryTitle.setText("Prefered Collection Date");
				tvSelectDeliveryTimeTitle.setText("Prefered Collection Time");
				llStoreAddress.setVisibility(View.VISIBLE);
				if (Helper.getSharedHelper().reatiler.collectionType.equalsIgnoreCase("1")) {
					llDeliveryTime.setVisibility(View.GONE);
					tvEstimatedDelivery.setText(tvDeliveryDateValue.getText()
							.toString());
				}else{
					llDeliveryTime.setVisibility(View.VISIBLE);
					tvEstimatedDelivery.setText(tvDeliveryDateValue.getText()
							.toString()
							+ ", "
							+ tvDeliveryTimeValue.getText().toString());
				}
			}
			String[] comp = tvEstimatedDelivery.getText().toString().split(",");
			
			String dateStr = comp[0];
			String[] dateComp = dateStr.split("-");
			etDay.setText(dateComp[0]);
			etMon.setText(dateComp[1]);
			etYear.setText(dateComp[2]);
			if (comp.length>1) {
				etTimeSlot.setText(comp[1]);
			}
			

		} catch (Exception e) {
		}
	}

	void ShowTimeSlotPopup() {
		listPopupWindow = new ListPopupWindow(this);
		listPopupWindow.setAdapter(new ArrayAdapter(this,
				R.layout.list_item_left_align, list));
		listPopupWindow.setAnchorView(etTimeSlot);

		listPopupWindow.setWidth(listPopupWindow.getWidth());
		// listPopupWindow
		// .setHeight(android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

		// listPopupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.boarder_around));
		listPopupWindow.setModal(true);
		// listPopupWindow.getListView().setDividerHeight(2);
		// listPopupWindow.getListView().setDivider(new
		// ColorDrawable(Color.parseColor("#b6b6b6")));;
		listPopupWindow.setOnItemClickListener(this);
		listPopupWindow.show();
	}

	void ShowCollectionAddressPopup() {
		listCollectionAddressWindow = new ListPopupWindow(this);
		listCollectionAddressWindow.setAdapter(new ArrayAdapter(this,
				R.layout.list_item_left_align, listCollectionAddress));
		listCollectionAddressWindow.setAnchorView(etCollectionAddress);

		listCollectionAddressWindow.setWidth(listCollectionAddressWindow
				.getWidth());
		// listPopupWindow
		// .setHeight(android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		listCollectionAddressWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

		// listPopupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.boarder_around));
		listCollectionAddressWindow.setModal(true);
		// listPopupWindow.getListView().setDividerHeight(2);
		// listPopupWindow.getListView().setDivider(new
		// ColorDrawable(Color.parseColor("#b6b6b6")));;
		listCollectionAddressWindow.setOnItemClickListener(this);
		listCollectionAddressWindow.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (listCollectionAddressWindow != null) {
			etCollectionAddress.setText(listCollectionAddress.get(arg2));

			listCollectionAddressWindow.dismiss();
			listCollectionAddressWindow = null;
			Helper.getSharedHelper().selectedCollectionAddress = listCollectionAddress
					.get(arg2);
		} else {

			etTimeSlot.setText(list.get(arg2));

			listPopupWindow.dismiss();
		}
	}

	void showDatePicker() {
		Date date;
		try {
			date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(etMon
					.getText().toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month = cal.get(Calendar.MONTH);
			int day = Integer.parseInt(etDay.getText().toString());
			int year = Integer.parseInt(etYear.getText().toString());
			if (year < 100) {
				year += 2000;
			}
			DatePickerDialog dialog = new DatePickerDialog(context,
					dateListener, year, month, day);

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE,
					Integer.parseInt(Helper.getSharedHelper().deliveryDays));
			Date dt = c.getTime();
			long minTme = dt.getTime();
			// dialog.getDatePicker().setMinDate(minTme);
			dialog.show();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(yr, monthOfYear, dayOfMonth);
			String DATE_FORMAT_NOW = "MMM";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			String month = sdf.format(calendar.getTime());

			// Calendar c = Calendar.getInstance();
			// c.setTime(new Date());
			// c.add(Calendar.DATE,
			// Integer.parseInt(Helper.getSharedHelper().deliveryDays));
			Date dt = Helper.getSharedHelper().earliestDate;
			Date selectedDate = calendar.getTime();
			int earliestDay = dt.getDate();
			int earliestMonth = dt.getMonth();
			int earliestYear = dt.getYear() + 1900;

			if (yr >= earliestYear) {
				if (monthOfYear >= earliestMonth) {
					if (dayOfMonth == earliestDay) {
						populateTimeSlot(dt.getHours(), dt.getMinutes());
					} else if (dayOfMonth > earliestDay) {
						populateTimeSlot(0, 0);
					} else {
						return;
					}
				} else {
					return;
				}

			} else {
				return;
			}

			String dateString = sdf.format(calendar.getTime());
			etDay.setText(Integer.toString(dayOfMonth));
			etMon.setText(month);
			etYear.setText(Integer.toString(yr));

		}
	};

	void populateTimeSlot(int hour, int min) {
		list.clear();
		if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
			String[] timeSlots = Helper.getSharedHelper().reatiler.deliveryTimeSlots
					.split(",");
			int deliveryHours = Integer
					.parseInt(Helper.getSharedHelper().reatiler.deliveryHours)
					+ hour;
			if (deliveryHours > 24) {
				Toast.makeText(this,
						"No delivery slots avaible for this date.",
						Toast.LENGTH_LONG).show();
				showDatePicker();
				return;
			} else {

			}
			List<String> possibleTimeSlots = Helper.getSharedHelper()
					.findTimePossibleTimeSlotes(timeSlots, deliveryHours, min);

			for (int i = 0; i < possibleTimeSlots.size(); i++) {
				list.add(Helper.getSharedHelper().convert24hrSlotsTo12hr(possibleTimeSlots.get(i)));
			}
		} else if (Helper.getSharedHelper().deliveryOptionSelectedIndex == 1) {
			String[] timeSlots = Helper.getSharedHelper().reatiler.collectionTimeSlots
					.split(",");
			int collectionHours = Integer
					.parseInt(Helper.getSharedHelper().reatiler.collectionHours)
					+ hour;
			if (collectionHours > 24) {
				Toast.makeText(this,
						"No collection slots avaible for this date.",
						Toast.LENGTH_LONG).show();
				showDatePicker();
				return;
			} else {

			}
			List<String> possibleTimeSlots = Helper
					.getSharedHelper()
					.findTimePossibleTimeSlotes(timeSlots, collectionHours, min);
			for (int i = 0; i < possibleTimeSlots.size(); i++) {
				list.add(Helper.getSharedHelper().convert24hrSlotsTo12hr(possibleTimeSlots.get(i)));
			}
		}
		try {
			etTimeSlot.setText(list.get(0));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	
	void showTimeSlots() {

	}

	private final class AsyncApplyCoupon extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();
			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put("creditCode", edtCoupon.getText().toString());
				param.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, Helper
						.getSharedHelper().productIdsInCart());
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_APPLY_COUPON, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null) {
				if (json.has("errorCode")) {
					try {
						imageView.setVisibility(View.VISIBLE);
						tvDiscountInfo.setVisibility(View.VISIBLE);
						String errorCode = json.getString("errorCode");
						if (errorCode != null && errorCode.equals("1")) {
							String discount = "0";
							if (json.has("discount")) {
								discount = json.getString("discount");
							}
							Helper.getSharedHelper().discountPercent = discount;
							if (json.has("discountType")) {
								Helper.getSharedHelper().discountType = json
										.getString("discountType");
								;
							}
							String cartTotalAmount = Helper.getSharedHelper()
									.getCartTotalAmount();

							tvDiscountValue
									.setText("("
											+ Helper.getSharedHelper()
													.getCurrencySymbol(
															Helper.getSharedHelper().reatiler.defaultCurrency)
											+ " "
											+ Helper.getSharedHelper()
													.conertfloatToSTring(
															Helper.getSharedHelper().discountAmount)
											+ ")");
							if (Float.parseFloat(cartTotalAmount) <= 0) {
								imageView.setImageResource(R.drawable.fail);
								tvDiscountInfo.setText("Invalid Voucher");
								Helper.getSharedHelper().discountPercent = "0";
								Helper.getSharedHelper().creditCode = "";
							} else {
								Helper.getSharedHelper().creditCode = edtCoupon
										.getText().toString();
								if (Helper.getSharedHelper().discountType
										.equalsIgnoreCase(Constants.KEY_DEFAULT_DISCOUNT_TYPE)) {
									tvDiscountInfo.setText(discount
											+ "% discount awarded");
								} else {
									tvDiscountInfo.setText(Helper
											.getSharedHelper().currency_code
											+ " "
											+ discount
											+ " discount awarded");
								}
								imageView.setImageResource(R.drawable.success);
							}

							tvEnterCode.setVisibility(View.GONE);
							cartView.setVisibility(View.VISIBLE);

							updatGrandTotal();
						} else {
							imageView.setImageResource(R.drawable.fail);
							tvDiscountInfo.setText("Invalid Voucher");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						imageView.setImageResource(R.drawable.fail);
						tvDiscountInfo.setText("Invalid Voucher");
						e.printStackTrace();
					}
				}
			} else {
				Toast.makeText(context, "Internal Error occured",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void shippingChargeUpdated() {
		// TODO Auto-generated method stub
		updatGrandTotal();
		tv_shipping_charge_lbl.setText(R.string.shipping_charge);
		llShippingChageValue.setVisibility(View.VISIBLE);
	}

	@Override
	public void rewadsPointsRedeemed(Boolean isSucess, String msg) {
		// TODO Auto-generated method stub
		if (isSucess) {
			tvResultMsg.setTextColor(Color.BLACK);
		} else {
			tvResultMsg.setTextColor(Color.RED);
		}
		tvResultMsg.setText(msg);
		tvResultMsg.setVisibility(View.VISIBLE);
		tvRewardsValue.setText("(" + Helper.getSharedHelper().redeemPoints
				+ ")");
		updatGrandTotal();
	}

	@Override
	public void userProfileFetched(String rewardsPoints) {
		// TODO Auto-generated method stub
		tvEarnedRewardValue.setText(rewardsPoints);
	}

}
