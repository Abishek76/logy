package com.drnds.titlelogy.fragments.client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.drnds.titlelogy.adapter.VendorViewAdapter;
import com.drnds.titlelogy.model.client.Vendor;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class ViewVendorFragment extends Fragment {
    private List<Vendor> vendorArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorViewAdapter mAdapter;
    SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_viewvendor, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_viewvendor);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        viewVendor();
        return view;
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }
    public void viewVendor(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VIEWVENDOR_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Vendor");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                           String vendorname=details.getString("Company_Name");

                        Logger.getInstance().Log("vendor name : "+vendorname);


                        Vendor vendor = new Vendor();
                        vendor.setVenname(details.getString("Company_Name"));
                        vendor.setVemail(details.getString("Company_Email"));
                        vendor.setState(details.getString("State"));
                        vendor.setCounty(details.getString("County"));
                        vendor.setPhoneno(details.getString("Phone_No"));


                        vendorArrayList.add(vendor);
                        mAdapter = new VendorViewAdapter(vendorArrayList);
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();





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


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}

