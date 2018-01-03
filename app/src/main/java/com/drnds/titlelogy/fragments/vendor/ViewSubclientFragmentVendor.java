package com.drnds.titlelogy.fragments.vendor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.vendor.VendorSubclientAdapter;
import com.drnds.titlelogy.model.vendor.VendorSubclient;
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

public class ViewSubclientFragmentVendor extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<VendorSubclient> vendorSubclientList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorSubclientAdapter mAdapter;
    SharedPreferences sp;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vendor_fragment_subclient, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.vendorrecyle_subclient);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        builder = new AlertDialog.Builder(getActivity());
        checkInternetConnection();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.vendorsubclientswipe_container);
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
        viewVendor();
        return view;
    }

    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    public void viewVendor() {

        mSwipeRefreshLayout.setRefreshing(true);
        Logger.getInstance().Log("client id : " + getVendorId());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORSUBCLIENT_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                try {
                    vendorSubclientList = new ArrayList<>();
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);

                        VendorSubclient vendor = new VendorSubclient();
                        vendor.setClientname(details.getString("Sub_Client_Name"));
                        vendor.setSubprocessname(details.getString("Sub_Client_Name"));
                        vendor.setSubprocesscode(details.getString("Sub_Process_Code"));
                        vendor.setState(details.getString("State"));
                        vendor.setCounty(details.getString("County"));
                        vendor.setInvoicename(details.getString("Invoice_Contact_Name"));
                        vendor.setAddress(details.getString("Address"));
                        vendor.setSubId(details.getString("Sub_Client_Id"));


                        vendorSubclientList.add(vendor);
                        mAdapter = new VendorSubclientAdapter(vendorSubclientList,getContext());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vendorSubclientList.clear();
    }

    public void onRefresh() {

        // Fetching data from server
        viewVendor();
        vendorSubclientList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void refreshSublient() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        viewVendor();
    }
}
