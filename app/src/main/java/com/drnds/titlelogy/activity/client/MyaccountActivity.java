package com.drnds.titlelogy.activity.client;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.client.MyAccountAdapter;

public class MyaccountActivity extends AppCompatActivity {
    public static TabLayout myaccount;
    public static ViewPager viewPager;
    FragmentPagerAdapter adapterViewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        toolbar = (Toolbar) findViewById(R.id.toolbar_myaccount);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My account");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        myaccount = (TabLayout) findViewById(R.id.tabs_myaccount);
        viewPager = (ViewPager) findViewById(R.id.viewpager_myaccount);

        adapterViewPager = new MyAccountAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        myaccount.post(new Runnable() {
            @Override
            public void run() {
                myaccount.setupWithViewPager(viewPager);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
