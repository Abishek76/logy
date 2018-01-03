package com.drnds.titlelogy.fragments.client.orderqueuefragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.drnds.titlelogy.activity.client.orderqueueactivity.OrderQueueActivity;
import com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter;
import com.drnds.titlelogy.model.client.OrderQueue;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class OrderQueueFragment extends Fragment {
    private ArrayList<OrderQueue> orderQueueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerOrderQueueAdapter mAdapter;
    SharedPreferences sp;

    private FloatingActionButton fab;
    public static final String ORDERPREFS_NAME = "OrderQueueFragment";

    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_orderqueue, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_orderqueue);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
//        mAdapter = new RecyclerOrderQueueAdapter(orderQueueList);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        fab = (FloatingActionButton) view.findViewById(R.id.fab_orderqueue);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OrderQueueActivity.class);
                getActivity().startActivityForResult(intent, 900);
            }
        });
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        showDialog();



                // Fetching data from server
                prepareProcessingOrder();


        setHasOptionsMenu(true);
        return view;
    }




    private void  prepareProcessingOrder(){
//        mSwipeRefreshLayout.setRefreshing(true);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERQUEUE_URL + getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e("order12", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    JSONArray jsonArray = response.getJSONArray("View_Order_Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                         Logger.getInstance().Log("sub id"+jsonObject);

                        String Order_Id = jsonObject.getString("Order_Id");
                        String Ordder_num = jsonObject.getString("Order_Number");
                        Logger.getInstance().Log("yestusala"+Ordder_num);
                        OrderQueue orderQueue = new OrderQueue();
                        orderQueue.setSubclient(jsonObject.getString("Sub_Client_Name"));
                        orderQueue.setOderno(jsonObject.getString("Order_Number"));

                        orderQueue.setStatus(jsonObject.getString("Progress_Status"));
                        orderQueue.setProducttype(jsonObject.getString("Product_Type"));
                        orderQueue.setState(jsonObject.getString("State"));
                        orderQueue.setCounty(jsonObject.getString("County"));
                        orderQueue.setPropertyaddress(jsonObject.getString("Property_Address"));
                        orderQueue.setOrderPriority(jsonObject.getString("Order_Priority"));
                        orderQueue.setOrder_Id(jsonObject.getString("Order_Id"));
                        orderQueue.setOrdertask(jsonObject.getString("Order_Status"));
                        orderQueue.setCountytype(jsonObject.getString("Order_Assign_Type"));
                        orderQueue.setBarrowername(jsonObject.getString("Barrower_Name"));
                        orderQueue.setSubId(jsonObject.getString("Sub_Client_Id"));
                        orderQueue.setDate(jsonObject.getString("Order_Date"));
                        orderQueueList.add(orderQueue);
                        mAdapter = new RecyclerOrderQueueAdapter(orderQueueList);
                        recyclerView.setAdapter(mAdapter);


                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode

                        // 0 for private mode
//                        SharedPreferences pref = getActivity().getSharedPreferences(ORDERPREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("Order_Id", Order_Id);
                        Logger.getInstance().Log("selected order id is : " + Order_Id);
                        editor.commit();
                        hideDialog();
                    }

                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search Orders");
        search(searchView);
        super.onCreateOptionsMenu(menu,inflater);
    }
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.getFilter().filter(query);

                return true;
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
                "LoginActivity", 0);

        prepareProcessingOrder();
        mAdapter.notifyDataSetChanged();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onResume() {
        super.onResume();
        prepareProcessingOrder();

    }

}


