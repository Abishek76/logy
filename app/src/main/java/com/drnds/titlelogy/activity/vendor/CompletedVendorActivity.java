package com.drnds.titlelogy.activity.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.gridactivity.GridViewVendorActivity;
import com.drnds.titlelogy.adapter.vendor.VendorCompletedAdapter;
import com.drnds.titlelogy.adapter.vendor.VendorRecyclegridviewAdapter;
import com.drnds.titlelogy.model.vendor.VendorGridItem;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.drnds.titlelogy.util.Config.NewOrderFlag;
import static com.drnds.titlelogy.util.Config.Order_Filter;
import static com.drnds.titlelogy.util.Config.selectedArrays;

public class CompletedVendorActivity extends AppCompatActivity {
    private ArrayList<VendorGridItem> vendorgridItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorCompletedAdapter mAdapter;
    SharedPreferences sp;
    SharedPreferences grid;
    LinearLayout linearLayout;

    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    Button select,accept;

    Boolean flag = false;
    ArrayList asss = new ArrayList();
    String jsonStr;

    String vendorID;
    String vId,Report_Type,From_Date,To_Date,Client_Id,Sub_Client_Id;
    JSONArray array = new JSONArray();

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendorcompleted);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_vencomp);
        toolbar = (Toolbar) findViewById(R.id.toolbar_vendorcomp);

        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);




        sp = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        vendorID = sp.getString("Vendor_Id","");
        Logger.getInstance().Log("vendor 22"+vendorID);

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
         vId=intent.getStringExtra("Vendor_Id");
        Report_Type=intent.getStringExtra("2");
        From_Date=intent.getStringExtra("From_Date");
        To_Date=intent.getStringExtra("To_Date");
        Client_Id=intent.getStringExtra("Client_Id");
        Sub_Client_Id=intent.getStringExtra("Sub_Client_Id");
        Logger.getInstance().Log("date"+To_Date);
        fireEvent();
    }

    public void fireEvent(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.VENDORREPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("hiii9", response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response" + response);
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

                            VendorGridItem vendorgridItem = new VendorGridItem();
                            String subname = details.getString("Sub_Client_Name");
                            vendorgridItem.setSubclient(subname);
                            String orderid = details.getString("Order_Id");

                            vendorgridItem.setOrderId(orderid);

                            String orderno = details.getString("Order_Number");
                            vendorgridItem.setOderno(orderno);
                            String state = details.getString("State");
                            vendorgridItem.setState(state);
                            String county = details.getString("County");
                            vendorgridItem.setCounty(county);
                            String borrowername = details.getString("Barrower_Name");
                            vendorgridItem.setBarrowername(borrowername);
                            String address = details.getString("Property_Address");
                            String orderstatus = details.getString("Order_Status");
                            vendorgridItem.setOrdertask(orderstatus);
                            String producttype = details.getString("Order_Type");
                            vendorgridItem.setProducttype(producttype);
                            vendorgridItem.setPropertyaddress(address);

                            String progressstatus = details.getString("Order_Status");
                            vendorgridItem.setStatus(progressstatus);
                            String order_priority = details.getString("Order_Priority");
                            vendorgridItem.setOrderPriority(order_priority);
                            String Order_Assign_Type = details.getString("Order_Assign_Type");
                            vendorgridItem.setCountytype(Order_Assign_Type);
//

                            vendorgridItemList.add(vendorgridItem);

                            mAdapter = new VendorCompletedAdapter(vendorgridItemList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

//                        hideDialog();
                        stopDialog();




                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Vendor_Id", vId);
                params.put("Report_Type", "2");
                params.put("From_Date", From_Date);
                params.put("To_Date", To_Date);
                params.put("Client_Id",Client_Id);
                params.put("Sub_Client_Id",Sub_Client_Id);


                Logger.getInstance().Log("params " +params);
                return params;
            }
        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

//        System.out.print("on resume"+ selectedOrders);




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

