package com.appsauthority.appwiz;

import java.util.List;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.Product;
import com.offpeaksale.consumer.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;


public class OutletMapActivity extends BaseActivity{
	MapLayout mapLayout;
	OrderObject orderObj;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setTitle((String) intent.getStringExtra("resturant_address"));
		setContentView(R.layout.outletmap);
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
			mapLayout.init();
		}
	}
}
