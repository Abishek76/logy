package com.drnds.titlelogy.adapter.vendor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drnds.titlelogy.fragments.vendor.orderquefragment.VendorEditOrderInfoFragment;
import com.drnds.titlelogy.fragments.vendor.orderquefragment.VendorEditUploadFragment;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorEditOrderQueueAdapter extends FragmentPagerAdapter {
    private int int_items=2;
    public VendorEditOrderQueueAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return new VendorEditOrderInfoFragment();
            case 1 : return new VendorEditUploadFragment();

        }
        return null;
    }

    @Override
    public int getCount() {

        return int_items;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0 :
                return "Order Information";
            case 1 :
                return "Upload/Download";

        }
        return null;
    }





}