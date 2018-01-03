package com.drnds.titlelogy.activity.vendor;

import android.content.Intent;
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
import com.drnds.titlelogy.adapter.vendor.VendorRecyclerViewReportAdapter;
import com.drnds.titlelogy.model.vendor.VendorReport;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class VendorReportActivity extends AppCompatActivity {
    private ArrayList<VendorReport> reportArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorRecyclerViewReportAdapter mAdapter;
    private Toolbar toolbar;
    private TextView emptyView;
    private ImageView emptyViewimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_report);
        recyclerView = (RecyclerView) findViewById(R.id.vendor_recycle_report);
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_report);
        emptyView = (TextView) findViewById(R.id.vendor_report_empty_view);
        emptyViewimg = (ImageView) findViewById(R.id.vendor_report_empty_view_image);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Report Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareReport();



        if (reportArrayList.isEmpty()) {
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
    private void    prepareReport() {


        Intent intent = getIntent();

        String JSON = intent.getStringExtra("JsonReport");
        Logger.getInstance().Log("JSON"+JSON);

        try {
            JSONObject json = new JSONObject(JSON);
            JSONArray jsonArray = json.getJSONArray("Orders");
            Log.e(TAG, json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject details = jsonArray.getJSONObject(i);

                VendorReport report = new VendorReport();
                String subname=details.getString("Sub_Client_Name");
                report.setSubclient(subname);
                String orderno=details.getString("Order_Number");
                report.setOderno(orderno);
                String status=details.getString("Order_Status");
                report.setStatus(status);
                String ordertyepe=details.getString("Order_Type");
                report.setProducttype(ordertyepe);
                String state=details.getString("State");
                report.setState(state);
                String county=details.getString("County");
                report.setCounty(county);
                String address=details.getString("Property_Address");
                report.setPropertyaddress(address);
                String borrowername=details.getString("Barrower_Name");
                report.setBorrowername(borrowername);


                reportArrayList.add(report);

                mAdapter = new VendorRecyclerViewReportAdapter(reportArrayList);
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




