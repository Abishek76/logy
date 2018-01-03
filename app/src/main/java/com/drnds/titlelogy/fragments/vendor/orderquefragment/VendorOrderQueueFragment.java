package com.drnds.titlelogy.fragments.vendor.orderquefragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.EditSubclientVendorActivity;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.adapter.vendor.VendorRecyclerOrderQueueAdapter;
import com.drnds.titlelogy.model.vendor.VendorOrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorOrderQueueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ArrayList<VendorOrderQueue> vendororderQueueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorRecyclerOrderQueueAdapter mAdapter;
    SharedPreferences sp;

    private ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String VENDORORDERPREFS_NAME = "OrderQueueFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.vendor_fragment_orderqueue, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.vendor_recycle_orderqueuef);
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        showDialog();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
//        mAdapter = new VendorRecyclerOrderQueueAdapter(vendororderQueueList,getContext());
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();



                // Fetching data from server
                prepareProcessingOrder();


        setHasOptionsMenu(true);
        return view;
//        // Fetching data from server
//        prepareProcessingOrder();
//        Logger.getInstance().Log("vendor999 " + getVendorId());
//
//        return view;
    }



    private void  prepareProcessingOrder(){



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORORDERQUEUE_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray = response.getJSONArray("ScoreBoard");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Logger.getInstance().Log("object is....: " + jsonObject);
                        String Order_Id = jsonObject.getString("Order_Id");
                        String subname = jsonObject.getString("Sub_Client_Name");
                        String orderno = jsonObject.getString("Order_Number");
                        String progressstatus = jsonObject.getString("Progress_Status");
                        String producttype = jsonObject.getString("Order_Type");
                        String barrowername = jsonObject.getString("Barrower_Name");
                        String state = jsonObject.getString("State_Name");
                        String county = jsonObject.getString("County_Name");
                        String propertyaddress = jsonObject.getString("Property_Address");
                        String orderpriority = jsonObject.getString("Order_Priority");
                        String ordertask = jsonObject.getString("Order_Status");
                        String State = jsonObject.getString("State");
                        String ordertype = jsonObject.getString("Order_Assign_Type");
                        String date = jsonObject.getString("Order_Date");
                        String subId = jsonObject.getString("Sub_Client_Id");
                        String clintId = jsonObject.getString("Clinet_Id");

                        VendorOrderQueue vendororderQueue = new VendorOrderQueue();
                        vendororderQueue.setOrder_Id(Order_Id);
                        vendororderQueue.setStateID(State);
                        vendororderQueue.setSubclient(subname);
                        vendororderQueue.setOderno(orderno);
                        vendororderQueue.setStatus(progressstatus);
                        vendororderQueue.setProducttype(producttype);
                        vendororderQueue.setState(state);
                        vendororderQueue.setCounty(county);
                        vendororderQueue.setPropertyaddress(propertyaddress);
                        vendororderQueue.setOrderPriority(orderpriority);
                        vendororderQueue.setOrdertask(ordertask);
                        vendororderQueue.setCountytype(ordertype);
                        vendororderQueue.setBarrowername(barrowername);
                        vendororderQueue.setSubId(subId);
                        vendororderQueue.setDate(date);
                        Logger.getInstance().Log("orderno: " + orderno);

                        vendororderQueueList.add(vendororderQueue);
                        mAdapter = new VendorRecyclerOrderQueueAdapter(vendororderQueueList,getContext());
                        recyclerView.setAdapter(mAdapter);


                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode

                        // 0 for private mode


                        stopDialog();
                    }

                } catch (JSONException e) {
                    stopDialog();
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_vendor, menu);

        MenuItem search = menu.findItem(R.id.vendor_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search Orders");
        search(searchView);
        super.onCreateOptionsMenu(menu,inflater);
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

    public void refreshOrder() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        prepareProcessingOrder();
    }

    @Override
    public void onRefresh() {
//        prepareProcessingOrder();
//        vendororderQueueList.clear();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vendororderQueueList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}








