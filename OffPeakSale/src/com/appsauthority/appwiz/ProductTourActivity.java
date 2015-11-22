package com.appsauthority.appwiz;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.appsauthority.appwiz.adapters.ProductTourFragmentAdapter;
import com.offpeaksale.restaurants.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class ProductTourActivity extends FragmentActivity{

	ProductTourFragmentAdapter mAdapter;
	    ViewPager mPager;
	    PageIndicator mIndicator;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_tour);
		
		SharedPreferences	spref = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit=spref.edit();
		edit.putBoolean("ProductTour", true);
		edit.commit();
        mAdapter = new ProductTourFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
	}
}
