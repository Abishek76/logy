package com.drnds.titlelogy.activity.client.orderqueueactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.activity.client.createuseractivity.EditCreateUserActivity;
import com.drnds.titlelogy.activity.client.gridactivity.GridUploadActivity;
import com.drnds.titlelogy.adapter.client.EditOrderQueueAdapter;
import com.drnds.titlelogy.util.Logger;

import static com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

public class EditOrderActivity extends AppCompatActivity {
    public TabLayout orderqueue;
    public ViewPager viewPager;
    EditOrderQueueAdapter editOrderQueueAdapter;
    String Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Assign_Type,Order_Task,status;
    private Toolbar toolbar;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar_editorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sp= getApplicationContext().getSharedPreferences(
                ORDERQUEUE, 0);
        //set Fragmentclass Arguments

        Order_Id = sp.getString("Order_Id","");
        state =sp.getString("State","");
        county = sp.getString("County","");
        producttype= sp.getString("Product_Type","");
        Sub_Client_Name = sp.getString("Sub_Client_Name","");
        Order_Priority = sp.getString("Order_Priority","");
        Order_Task =sp.getString("Order_Status","");
        Order_Assign_Type = sp.getString("Order_Assign_Type","");
        status =sp.getString("Progress_Status","");


        editOrderQueueAdapter = new  EditOrderQueueAdapter(getSupportFragmentManager(),
                Order_Id,Sub_Client_Name,Order_Priority,state,county, producttype,Order_Task,Order_Assign_Type,status);

        orderqueue= (TabLayout) findViewById(R.id.tabs_editorder);
        viewPager = (ViewPager) findViewById(R.id.viewpager_editorder);

        viewPager.setAdapter(editOrderQueueAdapter);

        orderqueue.post(new Runnable() {
            @Override
            public void run() {
                orderqueue.setupWithViewPager(viewPager);
            }
        });

//        getSupportFragmentManager().beginTransaction().replace(R.id.frag,obj).commit();
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
//                        Intent intent = new Intent(EditOrderActivity.this, NavigationActivity.class);
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



