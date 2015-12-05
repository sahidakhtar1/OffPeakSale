package com.appsauthority.appwiz;

import java.util.List;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class OutletMapActivity extends BaseActivity{
	MapLayout mapLayout;
	OrderObject orderObj;
	RelativeLayout headerView;
	TextView textViewHeader;
	ImageView imageBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		getActionBar().hide();
		setContentView(R.layout.outletmap);
		headerView = (RelativeLayout) findViewById(R.id.headerView);
		imageBack = (ImageView) findViewById(R.id.imageBack);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		textViewHeader.setText((String) intent.getStringExtra("resturant_address"));
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			textViewHeader.setTextColor(Color.parseColor("#"
							+ Helper.getSharedHelper().reatiler
									.getRetailerTextColor()));
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
		imageBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initMap();
		LinearLayout MapLinear=(LinearLayout)findViewById(R.id.MapLinear);
		MapLinear.addView(mapLayout);
		
	}
	
	private void initMap()
	{
		orderObj = OrderDetailActivity.orderObj;
		
		List<Product> products=orderObj.products;
		if(products!=null)
		{
			Product product = products.get(0);
			mapLayout = new MapLayout(this, this, product.outlets);
			mapLayout.merchantName=product.getName();
			mapLayout.init();
		}
	}
}
