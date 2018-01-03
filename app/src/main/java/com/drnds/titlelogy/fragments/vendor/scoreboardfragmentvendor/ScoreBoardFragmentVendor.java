package com.drnds.titlelogy.fragments.vendor.scoreboardfragmentvendor;

/**
 * Created by Ajith on 9/23/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorSearchActivity;

import static com.drnds.titlelogy.activity.client.MyaccountActivity.viewPager;

public class ScoreBoardFragmentVendor extends Fragment {
    public TabLayout scoreboard;
    public ViewPager viewPager;
    public  int int_items =3 ;
    private FloatingActionButton fab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.vendor_fragment_scoreboard,null);
        scoreboard= (TabLayout) x.findViewById(R.id.tab_score_vendor);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager_score_vendor);
        fab=(FloatingActionButton)x.findViewById(R.id.fab_search_vendor);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VendorSearchActivity.class);
                startActivity(intent);
            }
        });
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        scoreboard.post(new Runnable() {
            @Override
            public void run() {
                scoreboard.setupWithViewPager(viewPager);
            }
        });

        return x;


    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new ProcessingTabFragmentVendor();
                case 1 : return new PendingTabFragmentVendor();
                case 2 : return new CompletedTabFragmentVendor();

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
                    return "Processing";
                case 1 :
                    return "pending";
                case 2 :
                    return "completed";
            }
            return null;
        }
    }

}




