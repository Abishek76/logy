package com.drnds.titlelogy.adapter.vendor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drnds.titlelogy.fragments.client.gridfragment.EditGridUploadFragment;
import com.drnds.titlelogy.fragments.vendor.gridfragment.VendorEditGridFragment;
import com.drnds.titlelogy.fragments.vendor.gridfragment.VendorEditGridUploadFragment;

/**
 * Created by Ajith on 10/23/2017.
 */

public class VendorEditGridAdapter extends FragmentPagerAdapter {
    private int int_items=2;
    public VendorEditGridAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new VendorEditGridFragment();
            case 1:
                return new VendorEditGridUploadFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return int_items;
    }
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
