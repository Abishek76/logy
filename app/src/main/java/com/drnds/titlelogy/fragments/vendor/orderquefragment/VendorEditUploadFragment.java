package com.drnds.titlelogy.fragments.vendor.orderquefragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridUploadActivity;
import com.drnds.titlelogy.activity.vendor.orderqueueactivity.VendorUploadActivity;
import com.drnds.titlelogy.adapter.client.RecyclerUploadAdapter;
import com.drnds.titlelogy.adapter.vendor.RecyclerVendorUploadAdapter;
import com.drnds.titlelogy.model.client.Upload;
import com.drnds.titlelogy.model.vendor.VendorUpload;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.drnds.titlelogy.adapter.vendor.VendorRecyclerOrderQueueAdapter.VENDORORDER;

/**
 * Created by Ajith on 11/3/2017.
 */

public class VendorEditUploadFragment extends Fragment {
    private List<VendorUpload> uploadArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerVendorUploadAdapter mAdapter;
    private FloatingActionButton fab;
    SharedPreferences sp,pref;
    private ProgressDialog pDialog;
    String value,Order_Id;
    AlertDialog.Builder builder;
    private TextView emptyView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.fragment_vendorupload, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_vendorupload);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        checkInternetConnection();
        builder = new AlertDialog.Builder(getActivity());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        pref=getActivity().getSharedPreferences(VENDORORDER, Context.MODE_PRIVATE);
        Order_Id = pref.getString("Order_Id","");
        Logger.getInstance().Log("idd"+getVendorId());
        fab = (FloatingActionButton) view.findViewById(R.id.fab_vendorupload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplication(), CreateUserActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), VendorUploadActivity.class);
                startActivity(intent);
            }
        });

        viewDocuments();
        return view;
    }

    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }



    public void viewDocuments() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDOR_DOCUMENTS+ Order_Id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Upload_Documents");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);



                        VendorUpload vendorUpload = new VendorUpload();
                        String documentType=details.getString("Document_Type");
                        vendorUpload.setDocumentType(documentType);
                        String discript=details.getString("Description");
                        vendorUpload.setDescription(discript);
                        String date=details.getString("Inserted_Date");
                        vendorUpload.setUploadedDate(date);


                        vendorUpload.setDoumentpath(details.getString("Document_Path"));


                        //Creating editor to store values to shared preferences


                        uploadArrayList.add(vendorUpload);
                        mAdapter = new RecyclerVendorUploadAdapter(uploadArrayList,getContext());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                =(ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            TastyToast.makeText( getActivity(),"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
//            Snackbar snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check Internet Connection..!", Snackbar.LENGTH_LONG);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            snackbar.show();
            return false;
        }
        return false;
    }






    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
