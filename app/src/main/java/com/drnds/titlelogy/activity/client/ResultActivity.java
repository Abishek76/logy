package com.drnds.titlelogy.activity.client;

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
import com.drnds.titlelogy.adapter.client.ResultRecyclerviewAdapter;
import com.drnds.titlelogy.model.client.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Search> searchArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ResultRecyclerviewAdapter mAdapter;
    private TextView emptyView;
    private ImageView emptyViewimg;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        recyclerView = (RecyclerView) findViewById(R.id.recyle_result);
        toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyViewimg = (ImageView) findViewById(R.id.empty_view_image);
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

                Search search=new Search();
                search.setSubclient(details.getString("Sub_Client_Name"));
                search.setOderno(details.getString("Order_Number"));
                search.setStatus(details.getString("Progress_Status"));
                search.setProducttype(details.getString("Product_Type"));
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

                mAdapter = new ResultRecyclerviewAdapter(searchArrayList);
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
