package com.drnds.titlelogy.fragments.vendor.orderquefragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.fragments.client.DatePickerFragment;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.activity.vendor.VendorLoginActivity.VENDORLOGINPREFS_NAME;
import static com.drnds.titlelogy.adapter.vendor.VendorRecyclerOrderQueueAdapter.VENDORORDER;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorEditOrderInfoFragment extends Fragment {
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,spinner8;
    private EditText inputOrdernum, inputCity, inputZip, inputAddress, inputBorrowername, inputApn, inputDate;
    Button submit;
    private TextInputLayout inputLayoutborrowername,inputLayoutordernum,inputLayoutapn;

    SharedPreferences sp,vendor,clintlogin;
    private ArrayList<String> customer;
    private ArrayList<String> customerIds;
    private ArrayList<String> states;
    private ArrayList<String> stateIds;
    private ArrayList<String> county;
    private ArrayList<String> countyIds;
    private ArrayList<String> product;
    private ArrayList<String> productIds;
    private ArrayList<String> ordertask;
    private ArrayList<String> orderttaskIds;
    private ArrayList<String> status;
    private ArrayList<String> statusIds;
    private ArrayList<String> orderpriority;
    private ArrayList<String> orderpriorityIds;
    private ArrayList<String> countytype;
    private ArrayList<String> countytypeIds;
    private ProgressDialog pDialog;
    private String Order_Id,clientname,stringstate,stringcounty,stringorderpriority,stringproducttype,stringordertask,stringcountytype,stringstatus,
            ordernumber,stateid;
    private static String TAG = VendorEditOrderInfoFragment.class.getSimpleName();
    private String clientID,State,State_ID,County,County_ID,Order_Type_ID,Order_Type,Order_Status_ID,Order_Status,Order_Progress_Id,Progress_Status,
            Order_Priority_Id,Order_Priority,Sub_Client_Id,Order_Assign_Type_Id,Borrowername,Ordernum,Apn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.vendor_fragment_editorderinfo, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        sp = getActivity().getSharedPreferences(
                VENDORLOGINPREFS_NAME, 0);

        clintlogin= getActivity().getSharedPreferences(
                "LoginActivity", 0);
      showDialog();
        vendor = getActivity().getSharedPreferences(VENDORORDER, Context.MODE_PRIVATE);
        inputLayoutborrowername = (TextInputLayout) view.findViewById(R.id.vendor_input_layout_editborrowername);
        inputLayoutapn = (TextInputLayout) view.findViewById(R.id.vendor_input_layout_editapn);
        inputLayoutordernum = (TextInputLayout) view.findViewById(R.id.vendor_input_layout_editordernumber);
        inputOrdernum = (EditText) view.findViewById(R.id.vendor_input_editordernumber);
        inputCity = (EditText) view.findViewById(R.id.vendor_input_editcity);
        inputZip = (EditText) view.findViewById(R.id.vendor_input_editzip);
        inputAddress = (EditText) view.findViewById(R.id.vendor_input_editaddress);
        inputBorrowername = (EditText) view.findViewById(R.id.vendor_input_editborrowername);
        inputApn = (EditText) view.findViewById(R.id.vendor_input_editapn);
        inputDate = (EditText) view.findViewById(R.id.vendor_input_editdate);
        spinner1 = (Spinner) view.findViewById(R.id.vendor_customer_editspinner);
        spinner2 = (Spinner) view.findViewById(R.id.vendor_product_editspinner);
        spinner3 = (Spinner) view.findViewById(R.id.vendor_state_editspinner);
        spinner4 = (Spinner) view.findViewById(R.id.vendor_county_editspinner);
        spinner5 = (Spinner) view.findViewById(R.id.vendor_ordertask_editspinner);
        spinner6 = (Spinner) view.findViewById(R.id.vendor_status_editspinner);
        spinner7 = (Spinner) view.findViewById(R.id.vendor_orderpriority_editspinner);
        spinner8 = (Spinner) view.findViewById(R.id.vendor_countytype_editspinner);
        states = new ArrayList<String>();
        stateIds = new ArrayList<>();
        product = new ArrayList<String>();
        productIds = new ArrayList<>();
        county = new ArrayList<String>();
        countyIds = new ArrayList<String>();
        ordertask = new ArrayList<String>();
        orderttaskIds = new ArrayList<>();
        status = new ArrayList<String>();
        statusIds = new ArrayList<>();
        orderpriority = new ArrayList<String>();
        orderpriorityIds = new ArrayList<>();
        customer = new ArrayList<String>();
        customerIds = new ArrayList<>();
        countytype = new ArrayList<String>();
        countytypeIds = new ArrayList<>();

        inputBorrowername.addTextChangedListener(new MyTextWatcher(inputBorrowername));
        inputOrdernum.addTextChangedListener(new MyTextWatcher(inputOrdernum));
        inputApn.addTextChangedListener(new MyTextWatcher(inputApn));
        checkInternetConnection();
        clientID=clintlogin.getString("Client_Id","");
        stateid=vendor.getString("State","");
        stringstate=vendor.getString("State_Name","");
        stringcounty=vendor.getString("County_Name","");
        stringproducttype=vendor.getString("Order_Type","");
        stringordertask=vendor.getString("Order_Status","");
        stringorderpriority=vendor.getString("Order_Priority","");
        stringcountytype=vendor.getString("Order_Assign_Type","");
        stringstatus=vendor.getString("Progress_Status","");
        clientname=vendor.getString("Sub_Client_Name","");

        Logger.getInstance().Log("countyiii iiii : "+stringstatus);
        submit=(Button)view.findViewById(R.id.vendor_button_editordersubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();   // checking internet connection
                Borrowername = inputBorrowername.getText().toString().trim();
                Ordernum = inputOrdernum.getText().toString().trim();
                Apn = inputApn.getText().toString().trim();
                checkInternetConnection();

                if (!validateOrdernum() || !validateBorrowername() || !validateApn())
                {
                    Toasty.error(getActivity(), "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }

                else{
                    SubmitEditOrder();

                }}
        });
        Logger.getInstance().Log("hi id....."+getorderID());
        getCustomer();
        getState();
        getProduct();
        getOrdertask();
        getStatus();
        getOrderPriority();
        getcountyType();
        getDate();
        getOrderdetails();
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            inputDate.setText(  String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth)
                    + "/" + String.valueOf(year));
        }
    };

    private void getCustomer(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIENTORDER_URL+clientID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        customer.add(details.getString("Sub_Client_Name"));
                        customerIds.add(details.getString("Sub_Client_Id"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, customer));
                spinner1.setSelection(customer.indexOf(clientname));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getState() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.STATE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        states.add(details.getString("State"));
                        stateIds.add(details.getString("State_ID"));
//                        num1=details.getString("State_ID");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner3.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, states));
                spinner3.setSelection(states.indexOf(stringstate));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public String getStateId() {


        return sp.getString("State_ID", "");
    }
    public String getorderID() {


        return vendor.getString("Order_Id", "");
    }
    public String getCountyId() {


        return sp.getString("County_ID", "");

    }
    public String getVendorId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Vendor_Id", "");


    }
    private void getCounty() {

        Log.e("State_ID", getStateId());
        Log.e("State", getStateId());


        Log.e("County_Name", getCountyId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTY_URL + State_ID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");

                    county.clear();
                    countyIds.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        county.add(jsonObject.getString("County"));

                        countyIds.add(jsonObject.getString("County_ID"));
//                        num2=jsonObject.getString("County_ID");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spinner4.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, county));
                spinner4.setSelection(county.indexOf(stringcounty));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getProduct() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.PRODUCTTYPE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        product.add(details.getString("Order_Type"));
                        productIds.add(details.getString("Order_Type_ID"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, product));
                spinner2.setSelection(product.indexOf(stringproducttype));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getOrdertask() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERTASK_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        ordertask.add(details.getString("Order_Status"));
                        orderttaskIds.add(details.getString("Order_Status_ID"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner5.setEnabled(false);
                spinner5.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ordertask));
                spinner5.setSelection(ordertask.indexOf(stringordertask));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getStatus(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERSTATUS_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        status.add(details.getString("Progress_Status"));
                        statusIds.add(details.getString("Order_Progress_Id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner6.setEnabled(false);
                spinner6.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, status));
                spinner6.setSelection(status.indexOf(stringstatus));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getOrderPriority(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config. ORDERPRIORITY_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        orderpriority.add(details.getString("Order_Priority"));
                        orderpriorityIds.add(details.getString("Order_Priority_Id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner7.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, orderpriority));
                spinner7.setSelection(orderpriority.indexOf(stringorderpriority));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getcountyType(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTYTYPE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        countytype.add(details.getString("Order_Assign_Type"));
                        countytypeIds.add(details.getString("Order_Assign_Type_Id"));

                    }
                       hideDialog();
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }
                spinner8.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countytype));
                spinner8.setSelection(countytype.indexOf(stringcountytype));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onResume() {
        super.onResume();
//        statespinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State_ID = stateIds.get(position);
                State = states.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected state id : " + State_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("State", State_ID);
                editor.commit();


                getCounty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        countyspinner
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                County_ID = countyIds.get(position);
                County = county.get(position);

                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected county id : " + County_ID);

//                Logger.getInstance().Log("selected county  : " + countyname);
                //editor.putString("State_Name", State_Name);
                editor.putString("County",County_ID);

                getCountyId();
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
//        producttype
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Type_ID = productIds.get(position);
                Order_Type = product.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected product id : " + Order_Type_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Type_ID", Order_Type_ID);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        ordertask
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Status_ID = orderttaskIds.get(position);
                Order_Status = ordertask.get(position);




                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected order task id : " + Order_Status_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Status_ID", Order_Status_ID);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
        //Order Staus
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Progress_Id = statusIds.get(position);
                Progress_Status = status.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected status id : " + Order_Progress_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Progress_Id", Order_Progress_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        orderpriority
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Priority_Id = orderpriorityIds.get(position);
                Order_Priority = orderpriority.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected ordered priority id : " + Order_Priority_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Priority_Id", Order_Priority_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        customername
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sub_Client_Id = customerIds.get(position);


                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected cuctomer id : " + Sub_Client_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Sub_Client_Id", Sub_Client_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        countytype
        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Assign_Type_Id = countytypeIds.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected countytype id : " + Order_Assign_Type_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Assign_Type_Id", Order_Assign_Type_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
    }
    public void getDate(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.DATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Logger.getInstance().Log("in response");


                try {
                    JSONObject jObj = new JSONObject(response);
                    String date=jObj.getString("Order_Date");
                    inputDate.setText(date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public void  getOrderdetails() {
        Logger.getInstance().Log("in getorder " );
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORORDERDETAILS_URL+getorderID() , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Order_Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Order_Number=details.getString("Order_Number");

                        String Sub_Client_Name=details.getString("Sub_Client_Name");
                        inputOrdernum.setText(Order_Number);
                        String Order_Date=details.getString("Order_Date");
                        inputDate.setText(Order_Date);
                        String City=details.getString("City");
                        inputCity.setText(City);
                        String Zip_Code=details.getString("Zip_Code");
                        inputZip.setText(Zip_Code);
                        String Property_Address=details.getString("Property_Address");
                        inputAddress.setText(Property_Address);
                        String Barrower_Name=details.getString("Barrower_Name");
                        inputBorrowername.setText(Barrower_Name);
                        String APN=details.getString("APN");
                        inputApn.setText(APN);
                        Logger.getInstance().Log("set order no " + Order_Number);
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


    public void SubmitEditOrder(){
        showDialog();
        final String Order_Number = inputOrdernum.getText().toString().trim();

        final String  City = inputCity.getText().toString().trim();
        final String  Zip_Code = inputZip.getText().toString().trim();
        final String  Property_Address = inputAddress.getText().toString().trim();
        final String  Barrower_Name = inputBorrowername.getText().toString().trim();
        final String  APN = inputApn.getText().toString().trim();
        final String  Order_Date = inputDate.getText().toString().trim();
        pDialog.setMessage("Updating ...");
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.VENDOREDITORDERSUBMIT_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");

                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error)
                    {
//                        goToNavigationActivity();
                        Toasty.success( getActivity().getApplicationContext(),"Updated  Sucessfully...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }else {
                        Toasty.success( getActivity().getApplicationContext(),"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Order_Id",getorderID());

                params.put("Vendor_Id",getVendorId());

                params.put("Sub_Client_Id",Sub_Client_Id);
                params.put("State",State_ID);

                params.put("State_Name",State);

                params.put("County_Name",County);

                params.put("County",County_ID);
                params.put("Order_Type_ID",Order_Type_ID);

                params.put("Order_Type",Order_Type);
                params.put("Order_Status",Order_Status);
                params.put("Order_Status_ID",Order_Status_ID);
                params.put("Order_Progress_Id",Order_Progress_Id);
                params.put("Status",Progress_Status);
                params.put("Order_Priority_Id",Order_Priority_Id);
                params.put("Order_Priority",Order_Priority);

                params.put("Order_Assign_Type_Id",Order_Assign_Type_Id);


                params.put("Order_Number",Order_Number);
                params.put("City",City);
                params.put("Zip_Code",Zip_Code);
                params.put("Property_Address",Property_Address);
                params.put("Barrower_Name",Barrower_Name);
                params.put("APN",APN);
                params.put("Order_Date",Order_Date);

                Logger.getInstance().Log("edit info vendor"+params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    private boolean validateBorrowername() {
        if (inputBorrowername.getText().toString().trim().isEmpty()) {
            inputLayoutborrowername.setError(getString(R.string.err_msg_borrowername));
            requestFocus(inputBorrowername);
            return false;
        } else {
            inputLayoutborrowername.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateOrdernum() {
        if (inputOrdernum.getText().toString().trim().isEmpty()) {
            inputLayoutordernum.setError(getString(R.string.err_msg_orderno));
            requestFocus(inputOrdernum);
            return false;
        } else {
            inputLayoutordernum.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateApn() {
        if (inputApn.getText().toString().trim().isEmpty()) {
            inputLayoutapn.setError(getString(R.string.err_msg_apn));
            requestFocus(inputApn);
            return false;
        } else {
            inputLayoutapn.setErrorEnabled(false);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.vendor_input_editborrowername:
                    validateBorrowername();
                    break;

                case R.id.vendor_input_editordernumber:
                    validateOrdernum();
                    break;

                case R.id.vendor_input_editapn:
                    validateApn();
                    break;
            }
        }
    }



    //         check internet connection
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
//    private void goToNavigationActivity()
//    {
//        Intent intent = new Intent(getActivity(), VendorNavigationActivity.class);
//        intent.putExtra("refresh","yes");
//        intent.putExtra("position",2);
//        getActivity().setResult(1006,intent);
//        getActivity().finish();
//    }
}


