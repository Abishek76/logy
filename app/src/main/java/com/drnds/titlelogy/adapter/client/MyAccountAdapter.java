package com.drnds.titlelogy.adapter.client;


import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.drnds.titlelogy.fragments.client.myaccount.CompanyInfoTabFragment;
import com.drnds.titlelogy.fragments.client.myaccount.ResetPasswordFragment;
import com.drnds.titlelogy.fragments.client.myaccount.UserProfileTabFragment;

/**
 * Created by Ajithkumar on 5/18/2017.
 */

public class MyAccountAdapter extends FragmentPagerAdapter {
    public static int int_items = 3;
    public MyAccountAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CompanyInfoTabFragment();
            case 1:
                return new UserProfileTabFragment();
            case 2:
                return new ResetPasswordFragment();
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





