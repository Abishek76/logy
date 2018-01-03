package com.drnds.titlelogy.adapter.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;


import com.drnds.titlelogy.fragments.client.orderqueuefragment.EditOrderInfoFragment;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.EditUploadDocFragment;
import com.drnds.titlelogy.util.Logger;


/**
 * Created by ajithkumar on 6/29/2017.
 */

public class EditOrderQueueAdapter extends FragmentPagerAdapter {
    private int int_items=2;
    private String orderId,customername,ordedrpririty,state,county,producttype,ordertask,countytype,status;
    public EditOrderQueueAdapter(FragmentManager fm, String order_Id,String Sub_Client_Name,
                                 String Order_Priority,String State,String County,String Product_Type,
                                 String Order_Status,String Order_Assign_Type,String Progress_Status) {
        super(fm);
        this.orderId = order_Id;
        this.customername = Sub_Client_Name;
        this.ordedrpririty = Order_Priority;
        this.state = State;
        this.county =  County;
        this.producttype =  Product_Type;
        this.ordertask =  Order_Status;
        this.countytype =  Order_Assign_Type;
        this.status =  Progress_Status;
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 :
                Bundle bundle = new Bundle();
                bundle.putString("Order_Id", orderId);
                bundle.putString("Sub_Client_Name", customername);
                bundle.putString("Order_Priority", ordedrpririty);
                bundle.putString("State", state);
                bundle.putString("County", county);
                bundle.putString("Product_Type", producttype);
                bundle.putString("Order_Assign_Type", countytype);
                bundle.putString("Order_Status", ordertask);
                bundle.putString("Progress_Status", status);
                Logger.getInstance().Log("countytype in adapter : "+countytype);
                EditOrderInfoFragment obj = new EditOrderInfoFragment();
                obj.setArguments(bundle);
                return obj;
            case 1 :
                Bundle bund = new Bundle();
                bund.putString("Order_Id", orderId);

                EditUploadDocFragment object = new EditUploadDocFragment();
                object.setArguments(bund);
                return object;

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

