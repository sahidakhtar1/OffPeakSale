package com.appsauthority.appwiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsauthority.appwiz.fragments.ProductTourFragment;
import com.offpeaksale.restaurants.R;
import com.viewpagerindicator.IconPagerAdapter;

public class ProductTourFragmentAdapter extends FragmentPagerAdapter {
   
    private int mCount = 3;

    public ProductTourFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ProductTourFragment();
    }

    @Override
    public int getCount() {
        return mCount;
    }

       
}