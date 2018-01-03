package com.drnds.titlelogy.activity.client.gridactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.client.RecyclergridviewAdapter;
import com.drnds.titlelogy.model.client.GridItem;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;
import static com.drnds.titlelogy.fragments.client.scoreboardfragment.ProcessingTabFragment.MY_PREFS_NAME;

public class GridviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<GridItem> gridItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclergridviewAdapter mAdapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_griditem);
        toolbar = (Toolbar) findViewById(R.id.toolbar_gridrecycle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Order Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.gridview_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                prepareCompletedOrder();
                // Fetching data from server
            }
        });


    }

    private void   prepareCompletedOrder(){

        mSwipeRefreshLayout.setRefreshing(true);

        Intent intent = getIntent();

        String JSON=intent.getStringExtra("JSON");
        try {
            JSONObject json = new JSONObject(JSON);
            JSONArray jsonArray=json.getJSONArray("View_Order_Info");
            Log.e(TAG, json.toString());
            for(int i=0;i<jsonArray.length();i++){

                JSONObject details = jsonArray.getJSONObject(i);

                GridItem gridItem=new GridItem();
                gridItem.setSubclient(details.getString("Sub_Client_Name"));
                gridItem.setOderno(details.getString("Order_Number"));
                gridItem.setStatus(details.getString("Progress_Status"));
                gridItem.setProducttype(details.getString("Product_Type"));
                gridItem.setState(details.getString("State"));
                gridItem.setCounty(details.getString("County"));
                gridItem.setPropertyaddress(details.getString("Property_Address"));
                gridItem.setOrderId(details.getString("Order_Id"));
                gridItem.setBarrowername(details.getString("Barrower_Name"));
                gridItem.setOrderpriority(details.getString("Order_Priority"));
                gridItem.setCountytype(details.getString("Order_Assign_Type"));
                String Order_Assign_Type=details.getString("Order_Assign_Type");
                gridItem.setOrdertask(details.getString("Order_Status"));
                String SubId=details.getString("Sub_Client_Id");
                gridItem.setSubId(SubId);
                String date=details.getString("Order_Date");
                gridItem.setDate(date);
                Logger.getInstance().Log("   Order_Assign_Type7777" + Order_Assign_Type);



                gridItemList.add(gridItem);

                mAdapter = new RecyclergridviewAdapter(gridItemList);


                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSwipeRefreshLayout.setRefreshing(false);





    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search Orders");
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);

                return true;

            }
        });
    }
    @Override
    public void onRefresh() {

        gridItemList.clear();


        prepareCompletedOrder();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
