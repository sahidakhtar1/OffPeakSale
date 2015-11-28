package com.appsauthority.appwiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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

		TextView tvOrderId = (TextView)view.findViewById(R.id.tvOrderId);
		TextView	tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
		TextView tvOrderDiscount = (TextView) view.findViewById(R.id.tvOrderDiscount);
		TextView tvOrderInstruction = (TextView) view.findViewById(R.id.tvOrderInstruction);
		TextView tvOrderExpiry = (TextView) view.findViewById(R.id.tvOrderExpiry);


		try {
			tvOrderId.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderStatus.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderDiscount.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderInstruction.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderExpiry.setTypeface(Helper.getSharedHelper().normalFont);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String orderTex="<u>Order #"+orderObj.orderId+"</u>";
		tvOrderId.setText(Html.fromHtml(orderTex));
		tvOrderStatus.setText("Order Status: "+orderObj.shippingStatus);
		tvOrderDiscount.setText("Discount: "+orderObj.discountAmt+"%");
		String comp[] = orderObj.orderDate.split(" ");

		String date = "";
		if (comp.length > 0) {
			date = comp[0];
		}
		
		if(orderObj.shippingStatus.equals("Expired"))
		{
			tvOrderExpiry.setText("Expired On: "+date);
		}else if(orderObj.shippingStatus.equals("Redeemed"))
		{
			tvOrderExpiry.setText("Redeemed On: "+date);
		}else
		{
			tvOrderExpiry.setText("Expiry: "+date);
		}
		
		if (orderObj.orderInstr != null) {
			tvOrderInstruction.setText("Instruction: "+orderObj.orderInstr);
			tvOrderInstruction.setVisibility(View.VISIBLE);
		} else {
			tvOrderInstruction.setVisibility(View.GONE);
		}
		
	}
}
