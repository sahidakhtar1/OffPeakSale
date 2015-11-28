package com.appsauthority.appwiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

public class ViewQRCodeActivity extends BaseActivity {
	String couponCode;
	ImageView imgView;
	private ImageCacheLoader imageCacheLoader;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.qrcode_layout);
		imageCacheLoader = new ImageCacheLoader(this);
		Intent intent = getIntent();
		couponCode = (String) intent.getStringExtra("couponCode");
		imgView = (ImageView) findViewById(R.id.imgView);
		String qrCodeImageUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data="+couponCode;
		imageCacheLoader.displayImage(qrCodeImageUrl,
				R.drawable.image_placeholder, imgView);
	}
}
