package com.appsauthority.appwiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsauthority.appwiz.adapters.OrderDetailAdapter;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.restaurants.R;

public class OrderDetailActivity extends BaseActivity {
	OrderObject orderObj;
	RelativeLayout headerView;
	ImageView imageBack;
	TextView textViewHeader, tvQRCode;
	ListView itemListView;
	OrderDetailAdapter adapter;

	LinearLayout llOrderNumber, llOrderStaus, llOrderDate, llOrderTotal,
			llRewardsRedeemed, llRewardsEarned, llDiscount, llShippingFee,
			llDeliveryDate, llAddress, llInstruction;

	TextView tvOrderNumberValue, tvOrderStausValue, tvOrderdateValue,
			tvOrderTotalValue, tvRewardRedeemedValue, tvRewardsEarnedValue,
			tvDiscountValue, tvshippingFeeValue, tvDeliveryDateValue,
			tvAddressValue,tvInstructionValue;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.order_detail_layout);
		Intent intent = getIntent();
		orderObj = (OrderObject) intent.getSerializableExtra("orderObj");
		headerView = (RelativeLayout) findViewById(R.id.headerView);
		imageBack = (ImageView) findViewById(R.id.imageBack);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		tvQRCode = (TextView) findViewById(R.id.tvQRCode);
		itemListView = (ListView) findViewById(R.id.lv_items);
		adapter = new OrderDetailAdapter(this, R.layout.row_order_item,
				orderObj.products);
		itemListView.setAdapter(adapter);

		imageBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tvQRCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ViewQRCodeActivity.class);
				intent.putExtra("couponCode", orderObj.qrCode);
				startActivity(intent);
			}
		});

		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			tvQRCode.setTypeface(Helper.getSharedHelper().normalFont);
			textViewHeader
					.setTextColor(Color.parseColor("#"
							+ Helper.getSharedHelper().reatiler
									.getRetailerTextColor()));
			tvQRCode.setTextColor(Color.parseColor("#"
					+ Helper.getSharedHelper().reatiler.getRetailerTextColor()));
			headerView.setBackgroundColor(Color.parseColor("#"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
			if (Helper.getSharedHelper().reatiler.appIconColor != null
					&& Helper.getSharedHelper().reatiler.appIconColor
							.equalsIgnoreCase("black")) {
				imageBack.setBackgroundResource(R.drawable.backbutton_black);

			} else {
				imageBack.setBackgroundResource(R.drawable.backbutton);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		initializeOrderInfo();
		if (orderObj.shippingStatus.equalsIgnoreCase("Fulfilled")) {
			tvQRCode.setVisibility(View.GONE);
		}
	}

	void initializeOrderInfo() {
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.order_info_layout, null);
		itemListView.addHeaderView(view, "", false);

		llOrderNumber = (LinearLayout) view.findViewById(R.id.llOrderNumber);
		llOrderStaus = (LinearLayout) view.findViewById(R.id.llOrderStaus);
		llOrderDate = (LinearLayout) view.findViewById(R.id.llOrderDate);
		llOrderTotal = (LinearLayout) view.findViewById(R.id.llOrderTotal);
		llRewardsRedeemed = (LinearLayout) view
				.findViewById(R.id.llRewardsRedeemed);
		llRewardsEarned = (LinearLayout) view
				.findViewById(R.id.llRewardsEarned);
		llRewardsEarned.setVisibility(View.GONE);
		llDiscount = (LinearLayout) view.findViewById(R.id.llDiscount);
		llShippingFee = (LinearLayout) view.findViewById(R.id.llShippingFee);
		llDeliveryDate = (LinearLayout) view.findViewById(R.id.llDeliveryDate);
		llAddress = (LinearLayout) view.findViewById(R.id.llAddress);
		llInstruction = (LinearLayout) view.findViewById(R.id.llInstruction);
		
		TextView tvOrderNumberLbl = (TextView) llOrderNumber
				.findViewById(R.id.tvOptionLbl);
		tvOrderNumberValue = (TextView) llOrderNumber
				.findViewById(R.id.tvOptionValue);

		TextView tvOrderStausLbl = (TextView) llOrderStaus
				.findViewById(R.id.tvOptionLbl);
		tvOrderStausValue = (TextView) llOrderStaus
				.findViewById(R.id.tvOptionValue);

		TextView tvOrderDateLbl = (TextView) llOrderDate
				.findViewById(R.id.tvOptionLbl);
		tvOrderdateValue = (TextView) llOrderDate
				.findViewById(R.id.tvOptionValue);

		TextView tvOrderTotalLbl = (TextView) llOrderTotal
				.findViewById(R.id.tvOptionLbl);
		tvOrderTotalValue = (TextView) llOrderTotal
				.findViewById(R.id.tvOptionValue);

		TextView tvRewardsReedeemedLbl = (TextView) llRewardsRedeemed
				.findViewById(R.id.tvOptionLbl);
		tvRewardRedeemedValue = (TextView) llRewardsRedeemed
				.findViewById(R.id.tvOptionValue);

		TextView tvRewardsEarnedLbl = (TextView) llRewardsEarned
				.findViewById(R.id.tvOptionLbl);
		tvRewardsEarnedValue = (TextView) llRewardsEarned
				.findViewById(R.id.tvOptionValue);

		TextView tvDiscountLbl = (TextView) llDiscount
				.findViewById(R.id.tvOptionLbl);
		tvDiscountValue = (TextView) llDiscount
				.findViewById(R.id.tvOptionValue);

		TextView tvShippingFeeLbl = (TextView) llShippingFee
				.findViewById(R.id.tvOptionLbl);
		tvshippingFeeValue = (TextView) llShippingFee
				.findViewById(R.id.tvOptionValue);

		TextView tvDeliveryDateLbl = (TextView) llDeliveryDate
				.findViewById(R.id.tvOptionLbl);
		tvDeliveryDateValue = (TextView) llDeliveryDate
				.findViewById(R.id.tvOptionValue);

		TextView tvAddressLbl = (TextView) llAddress
				.findViewById(R.id.tvOptionLbl);
		tvAddressValue = (TextView) llAddress.findViewById(R.id.tvOptionValue);
		
		TextView tvInstructionLbl = (TextView) llInstruction
				.findViewById(R.id.tvOptionLbl);
		tvInstructionValue = (TextView) llInstruction.findViewById(R.id.tvOptionValue);

		tvOrderNumberLbl.setText("Order id:");
		tvOrderStausLbl.setText("Order Status:");
		tvOrderDateLbl.setText("Order date:");
		tvOrderTotalLbl.setText("Total:");
		tvRewardsEarnedLbl.setText("Credit Earned:");
		tvRewardsReedeemedLbl.setText("Credit Redeemed:");
		tvDiscountLbl.setText("Discount:");
		tvShippingFeeLbl.setText("Shipping Fee:");
		tvDeliveryDateLbl.setText("Delivery Date:");
		tvAddressLbl.setText("Delivery Address:");
		tvInstructionLbl.setText("Instruction:");

		tvOrderNumberValue.setText(orderObj.orderId);
		tvOrderStausValue.setText(orderObj.shippingStatus);
		tvOrderdateValue.setText(orderObj.orderDate);
		tvOrderTotalValue.setText(Helper.getSharedHelper().currency_code
				+ orderObj.orderTotal);
		// tvRewardsEarnedValue.setText(orderObj.);
		tvRewardRedeemedValue.setText(orderObj.reward_redeemed);
		tvDiscountValue.setText(orderObj.discountAmt);
		tvshippingFeeValue.setText(Helper.getSharedHelper().currency_code
				+ orderObj.shippingAmt);
		if (orderObj.deliverydate != null) {
			tvDeliveryDateValue.setText(orderObj.deliverydate);
		} else {
			tvDeliveryDateValue.setText(orderObj.collectiondate);
		}

		if (orderObj.deliveryaddress != null) {
			tvAddressValue.setText(orderObj.deliveryaddress);
		} else {
			tvAddressValue.setText(orderObj.collectionaddress);
		}
		
		if (orderObj.orderInstr != null) {
			tvInstructionValue.setText(orderObj.orderInstr);
			llInstruction.setVisibility(View.VISIBLE);
		} else {
			llInstruction.setVisibility(View.GONE);
		}
		

		try {
			tvOrderNumberLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderStausLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderDateLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderTotalLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardsReedeemedLbl
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardsEarnedLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvDiscountLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvShippingFeeLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvDeliveryDateLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvAddressLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvInstructionLbl.setTypeface(Helper.getSharedHelper().normalFont);

			tvOrderNumberValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderStausValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderdateValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderTotalValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardRedeemedValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardsEarnedValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvDiscountValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvshippingFeeValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvDeliveryDateValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			tvAddressValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvInstructionValue.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
