package com.drnds.titlelogy.fragments.client.scoreboardfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridviewActivity;
import com.drnds.titlelogy.adapter.client.ProcessingTabGridAdapter;
import com.drnds.titlelogy.model.client.ProcessTab;
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

import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class ProcessingTabFragment extends Fragment  {
    private ArrayList<ProcessTab> processTablist = new ArrayList<>();
    private ArrayList<String> griddata;
    GridView gv;
    private ProcessingTabGridAdapter mAdapter;
    SharedPreferences sp;

    String Pass_Order_Task_Id,Pass_Order_Status_Id;
    String Pass_Order_Filter;
    public static final String MY_PREFS_NAME= "Griview";
    SharedPreferences pref;
    private ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int newoRderCount,Titlesearch,Titlesearchqc,Propertytyping,Propertytypingqc,Finalreview,
    Taxcerificate,Completedorders=0;
    private  String NEW_ORDERS,SEARCH_ORDES,SEARCH_QC_ORDERS,TYPING_ORDERS,TYPING_QC_ORDERS,FINAL_REVIEW_ORDERS,TAX_ORDERS,COMPLETED_ORDERS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_processingtab, container, false);
        gv= (GridView) view.findViewById(R.id.gridview_proceesingtab);
        mAdapter = new ProcessingTabGridAdapter(getActivity(),processTablist);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
       pref = getActivity().getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
        griddata = new ArrayList<String>();

        processTablist.clear();
        mAdapter.notifyDataSetChanged();

        checkInternetConnection();
        prepareProcessingOrder();


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProcessTab item = (ProcessTab) parent.getItemAtPosition(position);
                showDialog();
                pDialog.setMessage("Loading ...");
                switch (position) {
                    case 0:
                        if (NEW_ORDERS != "0") {
                        Pass_Order_Task_Id = "0";
                                Pass_Order_Status_Id = "13";
                                Pass_Order_Filter = "NEW_ORDERS";
                        fireEvent();
                        }else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No New Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                        }
                        break;
                    case 1:
                        if (SEARCH_ORDES != "0") {
                        Pass_Order_Task_Id = "2";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                        break;

                    case 2:
                        if (SEARCH_QC_ORDERS != "0") {
                        Pass_Order_Task_Id = "3";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
                    case 3:
                        if (TYPING_ORDERS != "0") {
                        Pass_Order_Task_Id = "4";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                        break;
                    case 4:
                        if (TYPING_QC_ORDERS != "0") {
                        Pass_Order_Task_Id = "7";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                        break;
                    case 5:
                        if (FINAL_REVIEW_ORDERS != "0") {
                        Pass_Order_Task_Id = "18";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                        break;
                    case 6:
                        if (TAX_ORDERS != "0") {
                        Pass_Order_Task_Id = "12";
                        Pass_Order_Status_Id = "0";
                        Pass_Order_Filter = "GET_PROCESSING_ORDERS";
                        fireEvent();
                        }
                        else{
                            hideDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }

                        break;
//                    case 7:
////                        if (COMPLETED_ORDERS != "0") {
//                        Pass_Order_Task_Id = "0";
//                        Pass_Order_Status_Id = "0";
//                        Pass_Order_Filter = "COMPLETED_ORDERS";
//                        fireEvent();
//
////                        else{
////                            hideDialog();
////                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
////                        }
//                        break;
                }


            }
        });
        return view;
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }
    public void  prepareProcessingOrder() {

        showDialog();

        pDialog.setMessage("Loading ...");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SCOREBOARD_URL+getClientId() , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    System.out.println("er0");
                    JSONArray jsonArray=response.getJSONArray("ScoreBoard");
//                    if(jsonArray.isNull(0)){
                    Logger.getInstance().Log("response"+jsonArray);
//                        Toasty.error(getActivity(), "No orders found", Toast.LENGTH_SHORT, true).show();
//                    }
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


                        ProcessTab process=new ProcessTab(jsonObject.getString("NEW_ORDERS"),"New Orders");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("SEARCH_ORDES"),"Title Search");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("SEARCH_QC_ORDERS"),"Title Search Qc");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TYPING_ORDERS"),"Property Typing");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TYPING_QC_ORDERS"),"Property Typing  Qc");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("TAX_ORDERS"),"Tax Certificate");
                        processTablist.add(process);
                        process=new ProcessTab(jsonObject.getString("FINAL_REVIEW_ORDERS"),"Final Review");
                        processTablist.add(process);
//                        process=new ProcessTab(jsonObject.getString("COMPLETED_ORDERS"),"Completed");
//                        processTablist.add(process);

                        Logger.getInstance().Log("COMPLETED_ORDERS is : " + COMPLETED_ORDERS);
                        newoRderCount=Integer.parseInt(NEW_ORDERS);
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
                  System.out.println("er1");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TastyToast.makeText( getActivity().getApplicationContext(),"Some thing went wrong",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                System.out.println("er2");
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
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.POSTSCORE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                 hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("response", response.toString());
                    boolean  error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response"+response);
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray=jObj.getJSONArray("View_Order_Info");
//                        hideDialog();
                        stopDialog();


                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Sub_Client_Name = jsonObject.getString("Sub_Client_Name");
                            String Order_Number = jsonObject.getString("Order_Number");
                            String Status = jsonObject.getString("Status");
                            String Product_Type = jsonObject.getString("Product_Type");
                            String State = jsonObject.getString("State");
                            String County = jsonObject.getString("County");
                            String Property_Address = jsonObject.getString("Property_Address");
                            String Order_Id = jsonObject.getString("Order_Id");
                            String Order_Status = jsonObject.getString("Order_Status");




                            SharedPreferences.Editor edt = pref.edit();
                            edt.putString("Sub_Client_Name", Sub_Client_Name);
                            edt.putString("Order_Number", Order_Number);
                            edt.putString("Status", Status);
                            edt.putString("Product_Type", Product_Type);
                            edt.putString("State", State);
                            edt.putString("County", County);
                            edt.putString("Property_Address", Property_Address);
                            edt.putString("Order_Id", Order_Id);
                            edt.commit();
                        }
                        Intent intent = new Intent(getActivity(),GridviewActivity.class);

                        intent.putExtra("JSON", response.toString());
                        startActivity(intent);

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
                params.put("Client_Id",getClientId());
                params.put("Order_Task_id",Pass_Order_Task_Id);
                params.put("Order_Status_Id", Pass_Order_Status_Id );
                params.put("Order_Filter",Pass_Order_Filter);

               Logger.getInstance().Log("profrag"+params);
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

}


