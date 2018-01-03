package com.drnds.titlelogy.activity.vendor.orderqueueactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.adapter.vendor.VendorEditOrderQueueAdapter;

public class EditOrderVendorActivity extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    VendorEditOrderQueueAdapter vendoreditOrderQueueAdapter;
    String Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Assign_Type,Order_Task,status,barrowername,ordernumber;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_vendor);
        vendoreditOrderQueueAdapter = new  VendorEditOrderQueueAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_editorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        orderqueue= (TabLayout) findViewById(R.id.vendor_tabs_editorder);
        viewPager = (ViewPager) findViewById(R.id.vendor_viewpager_editorder);

        viewPager.setAdapter(vendoreditOrderQueueAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
      onBackPressed() ;
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
//                        Intent intent = new Intent(EditOrderVendorActivity.this, VendorNavigationActivity.class);
//                        intent.putExtra("refresh","yes");
//                        intent.putExtra("position",2);
//                        setResult(1005,intent);
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



