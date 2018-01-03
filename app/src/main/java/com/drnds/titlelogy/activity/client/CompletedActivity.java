package com.drnds.titlelogy.activity.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.client.RecyclerCompletedAdapter;
import com.drnds.titlelogy.adapter.client.RecyclergridviewAdapter;
import com.drnds.titlelogy.adapter.client.RecyclerviewReportAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorCompletedAdapter;
import com.drnds.titlelogy.model.client.GridItem;
import com.drnds.titlelogy.model.vendor.VendorGridItem;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class CompletedActivity extends AppCompatActivity {
    private ArrayList<GridItem> gridItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerCompletedAdapter mAdapter;
    SharedPreferences pref;

    SwipeRefreshLayout mSwipeRefreshLayout;

    SharedPreferences sp;
    SharedPreferences grid;
    LinearLayout linearLayout;

    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    Button select,accept;


    String jsonStr;

    String vendorID;
    String vId,Report_Type,From_Date,To_Date,Client_Id,Sub_Client_Id;


    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_completedgrid);
        toolbar = (Toolbar) findViewById(R.id.toolbar_gridcompleted);

        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Order Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Intent intent = getIntent();

        Report_Type=intent.getStringExtra("2");
        From_Date=intent.getStringExtra("From_Date1");
        To_Date=intent.getStringExtra("To_Date1");
        Client_Id=intent.getStringExtra("Client_Id1");

        Logger.getInstance().Log("date"+To_Date);
        fireEvent();
    }
    public void   fireEvent(){
        pDialog.setMessage("Fetching Data ...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.REPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean  error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response"+response);
                    // Check for error node in json
                    if (!error) {

                        JSONArray jsonArray=jObj.getJSONArray("Orders");
                        int num = 1;
                        if (jsonArray.length() < 50) {
                            num = jsonArray.length();
                        }else {
                            num = 50;

                        }
                        for(int i=0;i<num;i++) {
                            JSONObject details = jsonArray.getJSONObject(i);
                            GridItem gridItem=new GridItem();
                            gridItem.setSubclient(details.getString("Sub_Client_Name"));
                            gridItem.setOderno(details.getString("Order_Number"));
                            gridItem.setStatus(details.getString("Order_Status"));
                            gridItem.setProducttype(details.getString("Product_Type"));
                            gridItem.setState(details.getString("State"));
                            gridItem.setCounty(details.getString("County"));
                            gridItem.setPropertyaddress(details.getString("Property_Address"));
                            gridItem.setOrderId(details.getString("Order_Id"));

                            gridItem.setOrderpriority(details.getString("Order_Priority"));
                            gridItem.setCountytype(details.getString("Order_Assign_Type"));
                            String date=details.getString("Order_Date");
                            gridItem.setDate(date);

//                            gridItem.setOrdertask(details.getString("Order_Status"));

                            gridItem.setBarrowername(details.getString("Barrower_Name"));
                            gridItemList.add(gridItem);

                            mAdapter = new RecyclerCompletedAdapter(gridItemList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }else

                    {
                        TastyToast.makeText(CompletedActivity.this,"Some thing went wrong",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Client_Id",Client_Id);
                params.put("Report_Type", "2");
                params.put("From_Date", From_Date );
                params.put("To_Date",To_Date);
                Logger.getInstance().Log("Report_Type"+params);

                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}



}
