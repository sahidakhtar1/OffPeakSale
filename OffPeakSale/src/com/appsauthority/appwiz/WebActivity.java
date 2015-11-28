package com.appsauthority.appwiz;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class WebActivity extends BaseActivity {
	private Bundle bundleArgs = null;
	private String url;
	private Retailer retailer;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_web);
		retailer = Helper.getSharedHelper().reatiler;
		TextView textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		textViewHeader.setText("Terms of Use");
		View lineBot = (View) findViewById(R.id.lineBot);
		View lineTop = (View) findViewById(R.id.lineTop);
		Button btn_agree = (Button) findViewById(R.id.btn_agree);
		
		ImageView btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			setHeaderTheme(this, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			lineBot.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			btn_agree.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			btn_agree.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

		} catch (Exception e) {

		}
		bundleArgs = getIntent().getExtras();

		url = bundleArgs.getString("terms_of_use_link");
		//url = "http://appwizlive.com/";

		WebView w = (WebView) findViewById(R.id.webView);

		w.getSettings().setJavaScriptEnabled(true);
		w.loadUrl(url);

	}
	public void agreePressed(View v) {

		finish();
	}

}
