package com.drnds.titlelogy.fragments.client.createuserfrag;

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
import com.drnds.titlelogy.activity.client.createuseractivity.CreateUserActivity;
import com.drnds.titlelogy.adapter.client.CreateUserAdapter;
import com.drnds.titlelogy.model.client.CreateUser;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class CreateUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {

    private List<CreateUser> createUserArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CreateUserAdapter mAdapter;
    private FloatingActionButton fab;
    SharedPreferences sp;
    private ProgressDialog pDialog;
    String value;
    AlertDialog.Builder builder;
    String Client_User_Id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.fragment_createuser, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_createuser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        checkInternetConnection();
        builder = new AlertDialog.Builder(getActivity());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_createuser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplication(), CreateUserActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), CreateUserActivity.class);
                getActivity().startActivityForResult(intent, 1002);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.createuserswipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
//                viewUser();

            }
        });
        return view;
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
    }



    public void viewUser() {
        mSwipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VIEWUSER_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                createUserArrayList.clear();
                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        value = details.getString("Client_User_Id");
                        Logger.getInstance().Log("values : " + value);
                        String First_Name = details.getString("First_Name");
                        String Last_Name = details.getString("Last_Name");
                        String Email = details.getString("Email");
                        String Alternative_Email = details.getString("Alternative_Email");

                        Logger.getInstance().Log("Alternative_Email : " + Alternative_Email);

                        CreateUser createUser = new CreateUser();

                        createUser.setClientUid(details.getString("Client_User_Id"));
                        createUser.setFname(details.getString("First_Name"));
                        createUser.setEmail(details.getString("Email"));
                        createUser.setLname(details.getString("Last_Name"));
                        createUser.setAltemail(details.getString("Alternative_Email"));





                        createUserArrayList.add(createUser);
                        mAdapter = new CreateUserAdapter(createUserArrayList,getContext());
                        recyclerView.setAdapter(mAdapter);

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
    public void refreshClient() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        viewUser();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        viewUser();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        createUserArrayList.clear();
    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        viewUser();
        createUserArrayList.clear();
        mAdapter.notifyDataSetChanged();
    }
}


