package com.appsauthority.appwiz.fragments;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appsauthority.appwiz.models.TourObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public final class ProductTourFragment extends Fragment {

	TourObject tourObj;
	public ImageCacheLoader imageCacheloader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public ProductTourFragment(TourObject tourObj) {
		this.tourObj = tourObj;
		Context context = getActivity();
		imageCacheloader = new ImageCacheLoader(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.product_tour_fragment,
				container, false);

		TextView howItWorks = (TextView) rootView.findViewById(R.id.howItWorks);
		
		TextView detail = (TextView) rootView.findViewById(R.id.details);
		ImageView bg = (ImageView) rootView.findViewById(R.id.tourBG);
		ImageView logo = (ImageView) rootView.findViewById(R.id.Logo);
		try {
			howItWorks.setText(tourObj.headerTitle);
			detail.setText(tourObj.description);
			imageCacheloader.displayImage(tourObj.imageUrl,
					R.drawable.image_placeholder, bg);
			howItWorks.setTypeface(Helper.getSharedHelper().boldFont);
			detail.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
