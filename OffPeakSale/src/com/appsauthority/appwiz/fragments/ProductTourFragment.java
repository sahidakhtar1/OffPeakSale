package com.appsauthority.appwiz.fragments;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.restaurants.R;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.product_tour_fragment, container,false);
    	
    	TextView howItWorks=(TextView)rootView.findViewById(R.id.howItWorks);
    	howItWorks.setTypeface(Helper.getSharedHelper().normalFont,Typeface.BOLD);
    	TextView detail=(TextView)rootView.findViewById(R.id.details);
    	ImageView bg=(ImageView)rootView.findViewById(R.id.tourBG);
    	ImageView logo=(ImageView)rootView.findViewById(R.id.Logo);
    	
    	
		return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
