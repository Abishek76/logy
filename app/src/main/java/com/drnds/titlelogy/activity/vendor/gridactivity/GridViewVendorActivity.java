package com.drnds.titlelogy.activity.vendor.gridactivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.adapter.vendor.VendorRecyclegridviewAdapter;
import com.drnds.titlelogy.model.vendor.VendorGridItem;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;
import static com.drnds.titlelogy.activity.vendor.VendorLoginActivity.VENDORLOGINPREFS_NAME;
import static com.drnds.titlelogy.fragments.client.scoreboardfragment.ProcessingTabFragment.MY_PREFS_NAME;
import static com.drnds.titlelogy.util.Config.NewOrderFlag;
import static com.drnds.titlelogy.util.Config.selectedArrays;


public class GridViewVendorActivity extends AppCompatActivity {
    private ArrayList<VendorGridItem> vendorgridItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorRecyclegridviewAdapter mAdapter;
    SharedPreferences sp;
    SharedPreferences grid;
    LinearLayout linearLayout;
    private ProgressDialog pDialog;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    Button select,accept;
    boolean isIconChange=false;
    Boolean flag = false;
    ArrayList asss = new ArrayList();
    String jsonStr;
    private ArrayList<String>selectedOrders=new ArrayList<>();
    String vendorID;
    JSONArray array = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_vendor);
//        sp = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.vendor_recycle_griditem);
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_gridrecycle);
        linearLayout=(LinearLayout)findViewById(R.id.linear_select);
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        select=(Button)findViewById(R.id.button_select);
        accept=(Button)findViewById(R.id.button_accept);
        selectedOrders=new ArrayList<>();

        sp = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        vendorID = sp.getString("Vendor_Id","");
        Logger.getInstance().Log("vendor 22"+vendorID);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                isIconChange = !isIconChange;
                if(isIconChange){
                    select.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                    selectedOrders.clear();
                    System.out.println("selectedOrders check"+selectedOrders);
                    isIconChange = false;
                    flag = true;
                    mAdapter.notifyDataSetChanged();
                } else {
                    select.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked, 0);

//                    if (flag) {
//                        prepareCompletedOrder();
//                        flag=false;
//
//                    }

                    System.out.println("selectedOrders checked "+selectedOrders);
                    isIconChange = true;
                    mAdapter.notifyDataSetChanged();

                }

                if(!isIconChange) {
                    //If Text is Select All then loop to all array List items and check all of them
                    for (VendorGridItem vendorGridItem : vendorgridItemList) {
                        vendorGridItem.setSelected(false);
                        Logger.getInstance().Log("select false = " + vendorGridItem.getOrderId());
                        selectedArrays.remove(vendorGridItem.getOrderId());
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    for (VendorGridItem vendorGridItem : vendorgridItemList) {
                        vendorGridItem.setSelected(true);
                        Logger.getInstance().Log("select trtue = " + vendorGridItem.getOrderId());
                        selectedArrays.add(vendorGridItem.getOrderId());
                    }
                    Logger.getInstance().Log("select arrays " + selectedArrays);
                    mAdapter.notifyDataSetChanged();
                }




            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String selectedString = "";
//                selectedOrders  = selectedArrays;
//
//                for(int i = 0 ; i < selectedOrders.size(); i++ ){
//
//                    if (i == 0) {
//                        selectedString = selectedOrders.get(i);
//                    }else{
//                        selectedString = selectedString + "," + selectedOrders.get(i);
//                    }
//                }

                System.out.println("asss"+selectedOrders);

                //
                sendCheck();
            }
        });

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Order Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareCompletedOrder();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(NewOrderFlag!=true){
            linearLayout.setVisibility(View.VISIBLE);

            NewOrderFlag=true;
        }

//        System.out.print("on resume"+ selectedOrders);
    }


    private void   prepareCompletedOrder(){




        Intent intent = getIntent();

        String JSON=intent.getStringExtra("JSONVENDOR");
        Logger.getInstance().Log("  JSONgrid" +JSON);
        try {
            JSONObject json = new JSONObject(JSON);
            JSONArray jsonArray=json.getJSONArray("View_Order_Info");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject details = jsonArray.getJSONObject(i);

                VendorGridItem vendorgridItem=new VendorGridItem();
                String subname=details.getString("Sub_Client_Name");
                vendorgridItem.setSubclient(subname);
                String orderid=details.getString("Order_Id");

                selectedOrders.add(orderid);
                vendorgridItem.setOrderId(orderid);

                String orderno=details.getString("Order_Number");
                vendorgridItem.setOderno(orderno);
                String state=details.getString("State");
                vendorgridItem.setState(state);
                String county=details.getString("County");
                vendorgridItem.setCounty(county);
                String borrowername=details.getString("Barrower_Name");
                vendorgridItem.setBarrowername(borrowername);
                String address=details.getString("Property_Address");
                String orderstatus=details.getString("Order_Status");
                vendorgridItem.setOrdertask(orderstatus);
                String producttype=details.getString("Order_Type");
                vendorgridItem.setProducttype(producttype);
                vendorgridItem.setPropertyaddress(address);

                String progressstatus=details.getString("Progress_Status");
                vendorgridItem.setStatus(progressstatus);
                String order_priority=details.getString("Order_Priority");
                vendorgridItem.setOrderPriority(order_priority);
                String Order_Assign_Type=details.getString("Order_Assign_Type");

                vendorgridItem.setCountytype(Order_Assign_Type);
             String date = details.getString("Order_Date");
                vendorgridItem.setDate(date);







                vendorgridItemList.add(vendorgridItem);

                mAdapter = new VendorRecyclegridviewAdapter(vendorgridItemList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }

//        System.out.print("on "+ selectedOrders);

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
    public void sendCheck(){
        showDialog();
        System.out.println("1");
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, Config.ACCEPT_ORDERS,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Logger.getInstance().Log("in response");
                hideDialog();

                try {
//                    JSONObject jObj = new JSONObject(response);
                    Log.e(TAG, response.toString());
                    boolean error = response.getBoolean("error");

                    Logger.getInstance().Log("in error response" + error);
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // Now store the user in SQLite
                        System.out.println("2");
                        startActivity(new Intent(getApplicationContext(), VendorNavigationActivity.class));
                        finish();
                        selectedOrders.clear();

                    }

                    else{
                        Logger.getInstance().Log("in error ");
                        TastyToast.makeText( getApplicationContext(),"Enter Valid Credentials...",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    Logger.getInstance().Log("in stock ");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params= new HashMap<String, String>();

//                JSONObject contactsObj = new JSONObject();
//                JSONArray contactsArray = new JSONArray();
//                try {
//
//                        //Do your stuff here.
//
//                        for (int i = 0; i < selectedArrays.size(); i++) {
//                            JSONObject contact = new JSONObject();
//                            contact.put("Order_Id", selectedArrays.get(i));
//                            contactsArray.put(i, contact);
//
//                        }
//
//
//                    //contactsObj.put()
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                final String jsonString = "[\n" +
//                        " {\n" +
//                        "  \"Order_Id\": 3469\n" +
//                        " }\n" +
//                        "]";

                // String rawUser  = new Gson().toJson(jsonString );


                // jsonStr = selectedArrays.toString();


                String selectedString = "";
                selectedOrders.clear();
                selectedOrders  = selectedArrays;

                for(int i = 0 ; i < selectedOrders.size(); i++ ){

                    if (i == 0) {
                        selectedString = selectedOrders.get(i);
                    }else{
                        selectedString = selectedString + "," + selectedOrders.get(i);
                    }
                }

                params.put("Order_Id", selectedString);
                params.put("Vendor_Id",vendorID);
//





                //Logger.getInstance().Log("json array6"+jsonStr);
                //Logger.getInstance().Log("json array6"+rawUser);

                Logger.getInstance().Log("json array2"+params);
                return params;
            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/json; charset=UTF-8");
//                return params;
//            }




        };

        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);




    }


}





