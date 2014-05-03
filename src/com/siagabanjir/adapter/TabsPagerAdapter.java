package com.siagabanjir.adapter;

import java.util.ArrayList;

import com.siagabanjir.DataPintuAir;
import com.siagabanjir.HomeFragment;
import com.siagabanjir.MyPlaceFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
	private ArrayList<DataPintuAir> dataKritis;
	private Context context;
	
    public TabsPagerAdapter(FragmentManager fm, ArrayList<DataPintuAir> dp, Context context) {
        super(fm);
        this.context = context;
        this.dataKritis = dp;
    }
 
    @Override
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            // Home fragment activity
            return new HomeFragment(dataKritis, context);
        case 1:
            // MyPlace fragment activity
            return new MyPlaceFragment(context);
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}