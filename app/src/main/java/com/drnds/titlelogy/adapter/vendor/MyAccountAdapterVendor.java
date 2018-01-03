package com.drnds.titlelogy.adapter.vendor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drnds.titlelogy.fragments.vendor.myaccount.CompanyInfoTabFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.myaccount.ResetPasswordFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.myaccount.UserProfileTabFragmentVendor;

/**
 * Created by Ajith on 9/25/2017.
 */

public class MyAccountAdapterVendor extends FragmentPagerAdapter {
    public static int int_items = 3;
    public MyAccountAdapterVendor(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CompanyInfoTabFragmentVendor();
            case 1:
                return new UserProfileTabFragmentVendor();
            case 2:
                return new ResetPasswordFragmentVendor();
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

        switch (position) {
            case 0:
                return "Company";
            case 1:
                return "User";
            case 2:
                return "Password";

        }
        return null;
    }
}

