package com.appsauthority.appwiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

import java.util.List;

import com.appsauthority.appwiz.adapters.OrderDetailAdapter;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

public class OrderDetailActivity extends BaseActivity {
	OrderObject orderObj;
	RelativeLayout headerView;
	ImageView imageBack;
	TextView textViewHeader, tvQRCode;
//	ListView itemListView;
	OrderDetailAdapter adapter;
	MapLayout mapLayout;
	private ImageCacheLoader imageCacheLoader;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.order_detail_layout);
		Intent intent = getIntent();
		imageCacheLoader = new ImageCacheLoader(this);
		orderObj = (OrderObject) intent.getSerializableExtra("orderObj");
		headerView = (RelativeLayout) findViewById(R.id.headerView);
		imageBack = (ImageView) findViewById(R.id.imageBack);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		tvQRCode = (TextView) findViewById(R.id.tvQRCode);
		//itemListView = (ListView) findViewById(R.id.lv_items);
	//	adapter = new OrderDetailAdapter(this, R.layout.row_order_item,
	//			orderObj.products);
	//	itemListView.setAdapter(adapter);

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
		
		setMapFooter();

		TextView tvOrderId = (TextView)findViewById(R.id.tvOrderId);
		TextView	tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
		TextView tvOrderDiscount = (TextView) findViewById(R.id.tvOrderDiscount);
		TextView tvOrderInstruction = (TextView) findViewById(R.id.tvOrderInstruction);
		TextView tvOrderExpiry = (TextView) findViewById(R.id.tvOrderExpiry);
		TextView tvOrderTotal=(TextView) findViewById(R.id.tvOrderTotal);
		ImageView iv_eshop=(ImageView)findViewById(R.id.iv_eshop);
		TextView tv_eshop_ResturantName=(TextView)findViewById(R.id.tv_eshop_ResturantName);
		TextView tvOldPriceDetails=(TextView)findViewById(R.id.tvOldPriceDetails);
		TextView tvNewPriceDetails=(TextView)findViewById(R.id.tvNewPriceDetails);
		LinearLayout MapLinear=(LinearLayout)findViewById(R.id.MapLinear);
		MapLinear.addView(mapLayout);


		try {
			tvOrderId.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderStatus.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderDiscount.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderInstruction.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderExpiry.setTypeface(Helper.getSharedHelper().normalFont);
			tvOrderTotal.setTypeface(Helper.getSharedHelper().normalFont);
			tv_eshop_ResturantName.setTypeface(Helper.getSharedHelper().boldFont);
			tvOldPriceDetails.setTypeface(Helper.getSharedHelper().normalFont);
			tvNewPriceDetails.setTypeface(Helper.getSharedHelper().normalFont);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String orderTex="<u>Order #"+orderObj.orderId+"</u>";
		tvOrderId.setText(Html.fromHtml(orderTex));
		tvOrderStatus.setText("Order Status: "+orderObj.shippingStatus);
		float conversionValue=0.0f;
		String selectedCurrencyCode;
		selectedCurrencyCode = Helper.getSharedHelper().currency_symbol;
		  if (conversionValue == 0) {
		   conversionValue = 1.0f;
		   selectedCurrencyCode = Helper.getSharedHelper().reatiler.defaultCurrency;
		  }
		tvOrderDiscount.setText("Discount: "+Helper.getSharedHelper() .getCurrencySymbol(selectedCurrencyCode)+orderObj.discountAmt);
		tvOrderTotal.setText("Total: "+Helper.getSharedHelper() .getCurrencySymbol(selectedCurrencyCode)+orderObj.orderTotal);
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
		
		imageCacheLoader.displayImage(orderObj.products.get(0).getImage(),
				R.drawable.image_placeholder, iv_eshop);
		tv_eshop_ResturantName.setText(orderObj.products.get(0).getName().trim());
		tvOldPriceDetails.setText(Helper.getSharedHelper() .getCurrencySymbol(selectedCurrencyCode)+orderObj.products.get(0).oldPrice);
		tvOldPriceDetails.setPaintFlags(tvOldPriceDetails.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
		tvNewPriceDetails.setText(Helper.getSharedHelper() .getCurrencySymbol(selectedCurrencyCode)+orderObj.products.get(0).newPrice);
		
	}
	
	private void setMapFooter()
	{
		List<Product> products=orderObj.products;
		if(products!=null)
		{
			Product product = products.get(0);
			mapLayout = new MapLayout(this, this, product.outlets);
			mapLayout.init();
		}
	}
}
