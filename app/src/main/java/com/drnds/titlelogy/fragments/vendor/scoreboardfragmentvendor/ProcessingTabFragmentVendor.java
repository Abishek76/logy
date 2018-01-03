package com.drnds.titlelogy.fragments.vendor.scoreboardfragmentvendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.gridactivity.GridViewVendorActivity;
import com.drnds.titlelogy.adapter.vendor.VendorProcessingTabGridAdapter;
import com.drnds.titlelogy.model.vendor.VendorProcessTab;
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
import static com.drnds.titlelogy.util.Config.Completedorders;
import static com.drnds.titlelogy.util.Config.Finalreview;
import static com.drnds.titlelogy.util.Config.NewOrderFlag;
import static com.drnds.titlelogy.util.Config.Order_Filter;
import static com.drnds.titlelogy.util.Config.Propertytyping;
import static com.drnds.titlelogy.util.Config.Propertytypingqc;
import static com.drnds.titlelogy.util.Config.Taxcerificate;
import static com.drnds.titlelogy.util.Config.Titlesearch;
import static com.drnds.titlelogy.util.Config.Titlesearchqc;
import static com.drnds.titlelogy.util.Config.newoRderCount;

/**
 * Created by Ajith on 9/23/2017.
 */

public class ProcessingTabFragmentVendor extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<VendorProcessTab> vendorprocessTablist = new ArrayList<>();
    private ArrayList<String> griddata;
    GridView gv;
    private VendorProcessingTabGridAdapter mAdapter;
    SharedPreferences sp;

    String Order_Task,Order_Status,Vendor_Id;
    public static final String VENDORMY_PREFS_NAME= "Griview";
    SharedPreferences pref;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog pDialog;
    private  String NEW_ORDERS,SEARCH_ORDES,SEARCH_QC_ORDERS,TYPING_ORDERS,TYPING_QC_ORDERS,FINAL_REVIEW_ORDERS,TAX_ORDERS,COMPLETED_ORDERS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.vendor_fragment_processingtab, container, false);
        gv= (GridView) view.findViewById(R.id.vendorgridview_proceesingtab);
        mAdapter = new VendorProcessingTabGridAdapter(getActivity(),vendorprocessTablist);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        pref = getActivity().getSharedPreferences(VENDORMY_PREFS_NAME, Context.MODE_PRIVATE);
        griddata = new ArrayList<String>();

        Logger.getInstance().Log("vendor scoreboard " +getVendorId());
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.vendorprocessswipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        vendorprocessTablist.clear();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
            }
        });

        vendorprepareProcessingOrder();
        checkInternetConnection();



        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VendorProcessTab item = (VendorProcessTab) parent.getItemAtPosition(position);
                showDialog();
                pDialog.setMessage("Loading ...");
                switch (position) {
                    case 0:
//new

                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (NEW_ORDERS != "0") {
                            Order_Task = "0";
                            Order_Status = "0";
                            Order_Filter = "NEW_ORDERS";
                            NewOrderFlag = false;
                            fireEvent();
                        }else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No New Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                        }
                        break;
                    case 1:
//titsera
                        if (SEARCH_ORDES != "0") {
                            Logger.getInstance().Log("position=" + newoRderCount + position);
                            Order_Task = "2";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";

                            fireEvent();
                        }else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//searchqc
                    case 2:
                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (SEARCH_QC_ORDERS != "0") {
                            Order_Task ="3";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";
                            fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    typing
                    case 3:
                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (TYPING_ORDERS != "0") {
                            Order_Task ="4";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";
                            fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    tyqc
                    case 4:
                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (TYPING_QC_ORDERS != "0") {
                            Order_Task ="7";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";
                            fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    finrev
                    case 5:
                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (FINAL_REVIEW_ORDERS != "0") {
                            Order_Task ="12";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";
                            fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    tax cert
                    case 6:
                        Logger.getInstance().Log("position="+newoRderCount+position);
                        if (TAX_ORDERS != "0") {
                            Order_Task ="19";
                            Order_Status = "0";
                            Order_Filter = "GET_PROCESSING_ORDERS";

                            fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    comp
//                    case 7:
//                        Logger.getInstance().Log("position="+newoRderCount+position);
//                        if (COMPLETED_ORDERS != "0") {
//                            Order_Task ="0";
//                            Order_Status = "0";
//                            Order_Filter = "COMPLETED_ORDERS";
//                            fireEvent();
//                        }
//                        else{
//                            hideDialog();
//                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
//                        }
//
//                        break;
                }


            }
        });
        return view;
    }
    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    public void  vendorprepareProcessingOrder() {
        mSwipeRefreshLayout.setRefreshing(true);
        showDialog();
        pDialog.setMessage("Loading ...");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORSCOREBOARD_URL+getVendorId() , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray=response.getJSONArray("ScoreBoard");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);


                        NEW_ORDERS = jsonObject.getString("NEW_ORDERS");
                        SEARCH_ORDES = jsonObject.getString("SEARCH_ORDES");
                        SEARCH_QC_ORDERS =jsonObject.getString("SEARCH_QC_ORDERS");
                        COMPLETED_ORDERS =jsonObject.getString("COMPLETED_ORDERS");
                        TYPING_ORDERS =jsonObject.getString("TYPING_ORDERS");
                        TYPING_QC_ORDERS =jsonObject.getString("TYPING_QC_ORDERS");
                        FINAL_REVIEW_ORDERS =jsonObject.getString("FINAL_REVIEW_ORDERS");
                        TAX_ORDERS =jsonObject.getString("TAX_ORDERS");
                        VendorProcessTab process=new VendorProcessTab(jsonObject.getString("NEW_ORDERS"),"New Orders");
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("SEARCH_ORDES"),"Title Search");
                        Logger.getInstance().Log("title search"+process);
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("SEARCH_QC_ORDERS"),"Title Search  Qc");
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("TYPING_ORDERS"),"Property Typing");
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("TYPING_QC_ORDERS"),"Property Typing  Qc");
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("FINAL_REVIEW_ORDERS"),"Final Review");
                        vendorprocessTablist.add(process);
                        process=new VendorProcessTab(jsonObject.getString("TAX_ORDERS"),"Tax Certificate");
                        vendorprocessTablist.add(process);

//                        process=new VendorProcessTab(jsonObject.getString("COMPLETED_ORDERS"),"Completed");
//                        vendorprocessTablist.add(process);

                        Logger.getInstance().Log("search : " + SEARCH_ORDES);

                        Titlesearch = Integer.parseInt(SEARCH_ORDES);
                        Titlesearchqc=Integer.parseInt(SEARCH_QC_ORDERS);
                        Propertytyping=Integer.parseInt(TYPING_ORDERS);
                        Propertytypingqc=Integer.parseInt(TYPING_QC_ORDERS);
                        Finalreview=Integer.parseInt(FINAL_REVIEW_ORDERS);
                        Taxcerificate=Integer.parseInt(TAX_ORDERS);
                        Completedorders=Integer.parseInt(COMPLETED_ORDERS);
                        gv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void fireEvent(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
               Config.urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("hiii", response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response" + response);
                    // Check for error node in json
                    if (!error) {


//                        hideDialog();
                        stopDialog();


                        Intent intent = new Intent(getActivity(), GridViewVendorActivity.class);

                        intent.putExtra("JSONVENDOR", response.toString());
                        Logger.getInstance().Log("Json object  : " + response.toString());
                        startActivity(intent);


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
                params.put("Vendor_Id",getVendorId());
                params.put("Order_Task",Order_Task);
                params.put("Order_Status", Order_Status );
                params.put("Order_Filter",Order_Filter);

                Logger.getInstance().Log("params " +params);
                return params;
            }
        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
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


    public void onRefresh() {

        // Fetching data from server
        vendorprepareProcessingOrder();
        vendorprocessTablist.clear();
        mAdapter.notifyDataSetChanged();
    }


}
