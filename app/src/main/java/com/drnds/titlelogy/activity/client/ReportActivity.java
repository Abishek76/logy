package com.drnds.titlelogy.activity.client;

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
import com.drnds.titlelogy.adapter.client.RecyclerviewReportAdapter;
import com.drnds.titlelogy.model.client.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class ReportActivity extends AppCompatActivity {
    private ArrayList<Report> reportArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerviewReportAdapter mAdapter;
    private Toolbar toolbar;
    private TextView emptyView;
    private ImageView emptyViewimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar_report);
        emptyView = (TextView) findViewById(R.id.report_empty_view);
        emptyViewimg = (ImageView) findViewById(R.id.report_empty_view_image);
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
        try {
            JSONObject json = new JSONObject(JSON);
            JSONArray jsonArray = json.getJSONArray("Orders");
            Log.e(TAG, json.toString());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject details = jsonArray.getJSONObject(i);

                Report report = new Report();
                report.setSubclient(details.getString("Sub_Client_Name"));
                report.setOderno(details.getString("Order_Number"));
                report.setStatus(details.getString("Progress_Status"));
                report.setProducttype(details.getString("Product_Type"));
                report.setState(details.getString("State"));
                report.setCounty(details.getString("County"));
                report.setPropertyaddress(details.getString("Property_Address"));
                report.setOrderId(details.getString("Order_Id"));

                report.setOrderpriority(details.getString("Order_Priority"));
                report.setCountytype(details.getString("Order_Assign_Type"));

                report.setOrdertask(details.getString("Order_Status"));

                report.setBarrowername(details.getString("Barrower_Name"));
                reportArrayList.add(report);

                mAdapter = new RecyclerviewReportAdapter(reportArrayList);
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
