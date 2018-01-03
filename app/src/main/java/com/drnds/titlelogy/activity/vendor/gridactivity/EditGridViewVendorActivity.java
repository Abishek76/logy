package com.drnds.titlelogy.activity.vendor.gridactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.adapter.vendor.VendorEditGridAdapter;
import com.drnds.titlelogy.util.Logger;

public class EditGridViewVendorActivity extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    private Toolbar toolbar;
    VendorEditGridAdapter vendoreditGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grid_view_vendor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editgrid_vendor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");
        Logger.getInstance().Log("gridadapter");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        vendoreditGridAdapter = new  VendorEditGridAdapter(getSupportFragmentManager());
        orderqueue= (TabLayout) findViewById(R.id.tabs_editgrid_vendor);
        viewPager = (ViewPager) findViewById(R.id.viewpager_editgrid_vendor);

        viewPager.setAdapter(vendoreditGridAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    public void onBackPressed() {
//        new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Closing Activity")
//                .setMessage("Are you sure you want to close this activity?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(EditGridViewVendorActivity.this, NavigationActivity.class);
//                        intent.putExtra("refresh","yes");
//                        intent.putExtra("position",5);
//                        setResult(1003,intent);
//                        finish();
//
//                    }
//
//
//
//                })
//                .setNegativeButton("No", null)
//                .show();
//
//    }



}

