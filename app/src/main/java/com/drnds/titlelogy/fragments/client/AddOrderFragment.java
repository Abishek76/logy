package com.drnds.titlelogy.fragments.client;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

public class AddOrderFragment extends Fragment {
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,spinner8;
    private EditText inputOrdernum, inputCity, inputZip, inputAddress, inputBorrowername, inputApn, inputDate;
    Button submit;
    private TextInputLayout inputLayoutborrowername,inputLayoutordernum,inputLayoutapn;
    SharedPreferences sp;
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
    private ArrayList<String> countyalltype;
    private ArrayList<String> countyalltypeIds;
    private ProgressDialog pDialog;

    private String State,State_ID,County,County_ID,Order_Type_ID,Order_Type,Order_Status_ID,Order_Status,Order_Progress_Id,Progress_Status,
            Order_Priority_Id,Order_Priority,Sub_Client_Id,Order_Assign_Type_Id,Borrowername,Ordernum,Apn;
    public static  String url = "https://titlelogy.com/Final_Api/api/Get_County_Type_State_County?id=1&id1=1&id2=1";
    private SimpleDateFormat dateFormatter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addorder, container, false);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        inputLayoutborrowername = (TextInputLayout) view.findViewById(R.id.addinput_layout_borrowername);
        inputLayoutapn = (TextInputLayout) view.findViewById(R.id.addinput_layout_apn);
        inputLayoutordernum = (TextInputLayout) view.findViewById(R.id.addinput_layout_ordernumber);
        inputOrdernum = (EditText) view.findViewById(R.id.addinput_ordernumber);
        inputCity = (EditText) view.findViewById(R.id.addinput_city);
        inputZip = (EditText) view.findViewById(R.id.addinput_zip);
        inputAddress = (EditText) view.findViewById(R.id.addinput_address);
        inputBorrowername = (EditText) view.findViewById(R.id.addinput_borrowername);
        inputApn = (EditText) view.findViewById(R.id.addinput_apn);
        inputDate = (EditText) view.findViewById(R.id.addinput_date);
        spinner1 = (Spinner) view.findViewById(R.id.addcustomer_spinner);
        spinner2 = (Spinner) view.findViewById(R.id.addproduct_spinner);
        spinner3 = (Spinner) view.findViewById(R.id.addstate_spinner);
        spinner4 = (Spinner) view.findViewById(R.id.addcounty_spinner);
        spinner5 = (Spinner) view.findViewById(R.id.addordertask_spinner);
        spinner6 = (Spinner) view.findViewById(R.id.addstatus_spinner);
        spinner7 = (Spinner) view.findViewById(R.id.addorderpriority_spinner);
        spinner8 = (Spinner) view.findViewById(R.id.addcountytype_spinner);
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
        countyalltype = new ArrayList<String>();
        countyalltypeIds = new ArrayList<>();

        inputBorrowername.addTextChangedListener(new AddOrderFragment.MyTextWatcher(inputBorrowername));
        inputOrdernum.addTextChangedListener(new AddOrderFragment.MyTextWatcher(inputOrdernum));
        inputApn.addTextChangedListener(new AddOrderFragment.MyTextWatcher(inputApn));
        checkInternetConnection();
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();



                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                inputDate.setText(dateFormatter.format(newDate.getTime()));


                            }
                        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });
        submit=(Button)view.findViewById(R.id.addbutton_ordersubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    updateOrderinfo();
                }}
        });
        showDialog();
        getCustomer();
        getState();
        getProduct();
        getOrdertask();
        getStatus();
        getOrderPriority();
        getcountyType();

        getDate();

        return view;
    }

    private void getCustomer(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIENTORDER_URL+getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

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
    public String getCountyId() {


        return sp.getString("County_ID", "");

    }
    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }
    private void getCounty() {

        Log.e("State_ID", getStateId());
        Log.e("State", getStateId());


        Log.e("County_Name", getCountyId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTY_URL + getStateId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

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

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        countytype.add(details.getString("Order_Assign_Type"));
                        countytypeIds.add(details.getString("Order_Assign_Type_Id"));

                    }


                    getTireType();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner8.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countytype));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getTireType(){

        String uri = String.format(Config.GETTIRE_URL+"?id="+getStateId()+"&id1="+getCountyId()+"&id2="+getClientId());
//        String uri = String.format(Config.GETTIRE_URL+"?id=1&id1=1&id2=1");

        Logger.getInstance().Log("Tire type url : "+uri);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    String Order_Assign_Type_Id = jsonArray.getJSONObject(0).getString("Order_Assign_Type_Id");
                    String Order_Assign_Type = jsonArray.getJSONObject(0).getString("Order_Assign_Type");

                    spinner8.setSelection(countytype.indexOf(Order_Assign_Type));
                    hideDialog();
                    Logger.getInstance().Log("Tire type id is : "+Order_Assign_Type);
                } catch (JSONException e) {
                    hideDialog();
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
                editor.putString("State_ID", State_ID);
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
                editor.putString("County_ID",County_ID);

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
    public void  updateOrderinfo(){

        Logger.getInstance().Log("in update client id");
        showDialog();
        final String Order_Number = inputOrdernum.getText().toString().trim();

        final String  City = inputCity.getText().toString().trim();
        final String  Zip_Code = inputZip.getText().toString().trim();
        final String  Property_Address = inputAddress.getText().toString().trim();
        final String  Barrower_Name = inputBorrowername.getText().toString().trim();
        final String  APN = inputApn.getText().toString().trim();
        final String  Order_Date = inputDate.getText().toString().trim();
        pDialog.setMessage("Updating ...");



        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.CREATEORDER_URL+getClientId(),null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");
                    boolean  duplicate=response.getBoolean("duplicate");

                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error&&!duplicate)
                    {

                        Toasty.success(getActivity().getApplicationContext(), "Order Created Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent=new Intent(getActivity(), NavigationActivity.class);
                        startActivity(intent);
                        hideDialog();
                    }else if (!error&&duplicate)
                    {

                        Toasty.warning(getActivity().getApplicationContext(), "This order already exist ", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else {
                        Toasty.error(getActivity().getApplicationContext(), "Order Creation Failed", Toast.LENGTH_SHORT, true).show();
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


                params.put("Clinet_Id",getClientId());

                params.put("Sub_Client_Id",Sub_Client_Id);
                params.put("State_ID",State_ID);

                params.put("State",State);

                params.put("County",County);

                params.put("County_ID",County_ID);
                params.put("Order_Type_ID",Order_Type_ID);

                params.put("Product_Type",Order_Type);
                params.put("Order_Status",Order_Status);
                params.put("Order_Status_ID",Order_Status_ID);
                params.put("Order_Progress_Id",Order_Progress_Id);
                params.put("Progress_Status",Progress_Status);
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
                Logger.getInstance().Log("params"+params);
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

                case R.id.input_borrowername:
                    validateBorrowername();
                    break;

                case R.id.input_ordernumber:
                    validateOrdernum();
                    break;

                case R.id.input_apn:
                    validateApn();
                    break;
            }
        }
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
}

