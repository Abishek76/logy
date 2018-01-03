package com.drnds.titlelogy.fragments.vendor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;


import com.drnds.titlelogy.adapter.vendor.VendorClientAdapter;
import com.drnds.titlelogy.model.vendor.VendorClient;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ajith on 9/23/2017.
 */

public class ViewClientFragmentVendor extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    private List<VendorClient> vendorclientArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorClientAdapter mAdapter;
    private FloatingActionButton fab;
    SharedPreferences sp;
    SharedPreferences shareadapter;
    public static final String PREFS_NAME = "VendorClient";
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    private String subId;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        View view = inflater.inflate(R.layout.vendor_fragment_client, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_client_vendor);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        builder = new AlertDialog.Builder(getActivity());
        checkInternetConnection();


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.createclientswipe_container_vendor);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
//                viewSubclient();

            }
        });
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        viewClient();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        viewClient();
    }

    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    public void viewClient() {

        mSwipeRefreshLayout.setRefreshing(true);
        Logger.getInstance().Log("client id : " + getVendorId());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORCLIENT_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    vendorclientArrayList = new ArrayList<>();
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);


                        VendorClient vendorclient = new VendorClient();
                        vendorclient.setCompanyname(details.getString("Company_Name"));
                        vendorclient.setClientcode(details.getString("Client_Code"));
//                        vendorclient.setSubId(details.getString("Sub_Client_Id"));
                        vendorclient.setCounty(details.getString("County"));
                        vendorclient.setState(details.getString("State"));
                        vendorclient.setClientId(details.getString("Client_Id"));


                        vendorclientArrayList.add(vendorclient);
                        mAdapter = new VendorClientAdapter(vendorclientArrayList,getContext());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putString("Client_Id", subId);
//                        editor.putString("Sub_Client_Name", Sub_Client_Name);
//                        editor.putString("Client_Name", Client_Name);
//
//                        editor.commit();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            TastyToast.makeText(getActivity(), "Check Internet Connection", TastyToast.LENGTH_SHORT, TastyToast.INFO);
//            Snackbar snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check Internet Connection..!", Snackbar.LENGTH_LONG);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            snackbar.show();
            return false;
        }
        return false;
    }


    public void refreshClient() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        viewClient();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vendorclientArrayList.clear();
    }

    public void onRefresh() {

        // Fetching data from server
        viewClient();
        vendorclientArrayList.clear();
        mAdapter.notifyDataSetChanged();
    }


}