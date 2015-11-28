package com.appsauthority.appwiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.imagezoom.GestureImageView;
import com.offpeaksale.consumer.R;

public class ImageZoomActivity extends BaseActivity {
	GestureImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.image_zoom_layout);
    	image = (GestureImageView) findViewById(R.id.image);
    	Intent intent = getIntent();
    	String imageUrl = intent.getStringExtra("image");
    	Bitmap bitmap = imageCacheloader.imageforUrl(imageUrl);
    	image.setImageBitmap(bitmap);
    	
    }
}