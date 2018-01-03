package com.drnds.titlelogy.fragments.client.createsubclientfrag;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.drnds.titlelogy.activity.client.subclientactivity.SubClientActivity;
import com.drnds.titlelogy.adapter.client.RecyclerSubclientAdapter;
import com.drnds.titlelogy.model.client.Subclient;
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
 * Created by Ajithkumar on 5/17/2017.
 */

public class CreateSubclientFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ItemTouchHelper itemTouchHelper;
    private List<Subclient> subclientArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerSubclientAdapter mAdapter;
    private FloatingActionButton fab;
    SharedPreferences sp;
    SharedPreferences shareadapter;
    public static final String PREFS_NAME = "Subclient";
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    private String subId;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        View view = inflater.inflate(R.layout.fragment_subclient, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_subclient);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        builder = new AlertDialog.Builder(getActivity());
        checkInternetConnection();


        fab = (FloatingActionButton) view.findViewById(R.id.fab_subclient);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubClientActivity.class);
                getActivity().startActivityForResult(intent, 1001);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.subclientswipe_container);
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        viewSubclient();
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
    }




    public void viewSubclient() {
        mSwipeRefreshLayout.setRefreshing(true);
        Logger.getInstance().Log("client id : " + getClientId());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIENT_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    subclientArrayList = new ArrayList<>();
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Sub_Client_Name = details.getString("Sub_Client_Name");
                        String Email = details.getString("Email");
                        subId = details.getString("Sub_Client_Id");
                        String State_ID = details.getString("State_ID");
                        String County_ID = details.getString("County_ID");
                        String State = details.getString("State");
                        String County = details.getString("County");
                        String City = details.getString("City");
                        String Address = details.getString("Address");
                        String Zip_Code = details.getString("Zip_Code");
                        String Alternative_Email = details.getString("Alternative_Email");
                        Logger.getInstance().Log("Alternative_Email : " + Alternative_Email);
                        Logger.getInstance().Log("Sub_Client_Name : " + Sub_Client_Name);
                        Subclient subclient = new Subclient();
                        subclient.setCustname(details.getString("Sub_Client_Name"));
                        subclient.setEmail(details.getString("Email"));
                        subclient.setSubId(details.getString("Sub_Client_Id"));
                        subclient.setCounty(details.getString("County"));
                        subclient.setState(details.getString("State"));
                        subclient.setCity(details.getString("City"));
                        subclient.setAddresss(details.getString("Address"));
                        subclient.setZip(details.getString("Zip_Code"));
                        subclient.setAltemail(details.getString("Alternative_Email"));

                        subclientArrayList.add(subclient);
                        mAdapter = new RecyclerSubclientAdapter(subclientArrayList, getContext());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();


//                        sp = getActivity().getApplicationContext().getSharedPreferences(
//                                "Subclient", 0); // 0 for private mode

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Sub_Client_Id", subId);
                        editor.putString("Sub_Client_Name", Sub_Client_Name);

                        editor.commit();


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


    public void refreshData() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        viewSubclient();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subclientArrayList.clear();
    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        viewSubclient();
        subclientArrayList.clear();
        mAdapter.notifyDataSetChanged();
    }
}












