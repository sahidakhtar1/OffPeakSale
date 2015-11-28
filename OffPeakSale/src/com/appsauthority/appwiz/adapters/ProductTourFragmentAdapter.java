package com.appsauthority.appwiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsauthority.appwiz.fragments.ProductTourFragment;
import com.appsauthority.appwiz.models.TourObject;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;
import com.viewpagerindicator.IconPagerAdapter;

public class ProductTourFragmentAdapter extends FragmentPagerAdapter {
   
    private int mCount = Helper.getSharedHelper().reatiler.tutorialSlides.size();

    public ImageCacheLoader imageCacheloader;
    public ProductTourFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	TourObject tourObj = Helper.getSharedHelper().reatiler.tutorialSlides.get(position);
    	ProductTourFragment tourFragment = new ProductTourFragment(tourObj);
    	
        return tourFragment;
    }

    @Override
    public int getCount() {
        return mCount;
    }

       
}