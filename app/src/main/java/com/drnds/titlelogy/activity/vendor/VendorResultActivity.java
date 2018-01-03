package com.drnds.titlelogy.activity.vendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.vendor.VendorResultRecyclerviewAdapter;
import com.drnds.titlelogy.model.vendor.VendorSearch;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class VendorResultActivity extends AppCompatActivity {
    private ArrayList<VendorSearch> searchArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorResultRecyclerviewAdapter mAdapter;
    private TextView emptyView;
    private ImageView emptyViewimg;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_result);
        recyclerView = (RecyclerView) findViewById(R.id.recyle_result_vendor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_result_vendor);
        emptyView = (TextView) findViewById(R.id.vendor_empty_view);
        emptyViewimg = (ImageView) findViewById(R.id.vendor_empty_view_image);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareSearch();
        if (searchArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyViewimg.setVisibility(View.VISIBLE);

        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            emptyViewimg.setVisibility(View.GONE);

        }



    }
    public void prepareSearch(){
        Intent intent = getIntent();

        String JSON=intent.getStringExtra("SEARCH");

        try {
            JSONObject json = new JSONObject(JSON);
            JSONArray jsonArray=json.getJSONArray("Orders");
            Log.e(TAG, json.toString());
            for(int i=0;i<jsonArray.length();i++){

                JSONObject details = jsonArray.getJSONObject(i);

                VendorSearch search=new VendorSearch();
//                String orderno=details.getString("Order_Number");
//                search.setOderno(orderno);
//                String subname=details.getString("Sub_Client_Name");
//                search.setSubclient(subname);
//                String orderid=details.getString("Order_Id");
//                search.setOrder_Id(orderid);
//                String progressstatus=details.getString("Progress_Status");
//                search.setStatus(progressstatus);
//                String producttype=details.getString("Product_Type");
//                search.setProducttype(producttype);
//                String state=details.getString("State");
//                search.setState(state);
//                String county=details.getString("County");
//                search.setCounty(county);
//                String propertyaddress=details.getString("Property_Address");
//                search.setPropertyaddress(propertyaddress);
//                String borrowername=details.getString("Barrower_Name");
//                search.setBarrowername(borrowername);
//                String orderPriority=details.getString("Order_Priority");
//                search.setOrderPriority(orderPriority);
//                String orderAssignType=details.getString("Order_Assign_Type");
//                search.setCountytype(orderAssignType);
//                String orderStatus=details.getString("Order_Status");
//                search.setOrdertask(orderStatus);




                search.setOderno(details.getString("Order_Number"));
                search.setSubclient(details.getString("Sub_Client_Name"));
                search.setStatus(details.getString("Progress_Status"));
                search.setProducttype(details.getString("Order_Type"));
                search.setState(details.getString("State"));
                search.setCounty(details.getString("County"));
                search.setPropertyaddress(details.getString("Property_Address"));
                search.setOrder_Id(details.getString("Order_Id"));
                search.setBarrowername(details.getString("Barrower_Name"));
                search.setOrderPriority(details.getString("Order_Priority"));
                search.setCountytype(details.getString("Order_Assign_Type"));
                String Order_Assign_Type=details.getString("Order_Assign_Type");
                search.setOrdertask(details.getString("Order_Status"));






                searchArrayList.add(search);

                mAdapter = new VendorResultRecyclerviewAdapter(searchArrayList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
