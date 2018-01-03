package com.drnds.titlelogy.fragments.vendor.myaccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ajith on 9/25/2017.
 */

public class CompanyInfoTabFragmentVendor extends Fragment {
    SharedPreferences sp;
    private ProgressDialog pDialog;
    Spinner stateSpinner, countySpinner;
    private Button buttonSaveCompany;
    private ArrayList<String> states;
    private ArrayList<String> stateIds;
    private ArrayList<String> county;
    private ArrayList<String> countyIds;
    private ArrayList statesedit;
    private ArrayList<String> stateIt;
    private ArrayList<String> countyedit;
    String  vendor_id;
    String state,countyname;
    private static String TAG = CompanyInfoTabFragmentVendor.class.getSimpleName();
    private EditText inputCompanyname,inputvendorid , inputCompanydefaultemail, inputCity, inputZip, inputAddress, inputPhno, inputaFaxno;
    private TextInputLayout inputLayoutCompanyname, inputLayoutCompanydefaultemail, inputLayoutCity, inputLayoutZip,
            inputLayoutAddress, inputLayoutPhno, inputLayoutFaxno,inputLayoutvendorid ;



    private String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }


    private String getUserId(){
        return sp.getString("Vendor_User_Id", "");
    }



    public void getVendorData() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDOR_GET_URL + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    boolean error = response.getBoolean("error");
                    if (!error) {
                        JSONArray users = response.getJSONArray("Users");

                        for (int i = 0; i < users.length(); i++) {
                            JSONObject data = users.getJSONObject(i);
                            Logger.getInstance().Log("data1111 : "+data);
                            String Company_Name = data.getString("Company_Name");

                            String Company_Email=data.getString("Company_Email");
//                             String State_Name = data.getString("State_Name");
                            countyname = data.getString("County_Name");

                            String Vendor_Id = data.getString("Vendor_Id");
                            String Address = data.getString("Address");
                            String ZipCode = data.getString("ZipCode");
                            String Phone_No = data.getString("Phone_No");
                            String Fax_No = data.getString("Fax_No");
                            state = data.getString("State_Name");
                            String County = data.getString("County");
                            stateIt.add(data.getString("State_Name"));
                            statesedit.add(data.getString("State"));
                            countyedit.add(data.getString("County"));
                            Logger.getInstance().Log("County : "+County);


                            // states.add(data.getString("State_Name"));
                            inputCompanydefaultemail.setText(Company_Email);
                            inputCompanyname.setText(Company_Name);
                            inputAddress.setText(Address);
                            inputZip.setText(ZipCode);
                            inputPhno.setText(Phone_No);
                            inputaFaxno.setText(Fax_No);


                            Logger.getInstance().Log("getting county name : "+countyname);
                            Logger.getInstance().Log("getting state name : "+state);

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext().getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }



            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vendor_company_infotab, container, false);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0); // 0 for private mode

        inputLayoutCompanyname = (TextInputLayout) view.findViewById(R.id.input_layout_companyname_vendor);
        inputCompanyname = (EditText) view.findViewById(R.id.input_companyname_vendor);
        inputLayoutCompanydefaultemail = (TextInputLayout) view.findViewById(R.id.input_layout_groupemail_vendor);
        inputCompanydefaultemail = (EditText) view.findViewById(R.id.input_groupemail_vendor);

        inputLayoutZip = (TextInputLayout) view.findViewById(R.id.input_layout_zip_vendor);
        inputZip = (EditText) view.findViewById(R.id.input_zip_vendor);
        inputLayoutAddress = (TextInputLayout) view.findViewById(R.id.input_layout_comaddress_vendor);
        inputAddress = (EditText) view.findViewById(R.id.input_comaddress_vendor);
        inputLayoutPhno = (TextInputLayout) view.findViewById(R.id.input_layout_phno_vendor);
        inputPhno = (EditText) view.findViewById(R.id.input_phno_vendor);
        inputLayoutFaxno = (TextInputLayout) view.findViewById(R.id.input_layout_fxno_vendor);
        inputaFaxno = (EditText) view.findViewById(R.id.input_fxno_vendor);
        buttonSaveCompany = (Button) view.findViewById(R.id.button_companysave_vendor);
        stateSpinner = (Spinner) view.findViewById(R.id.company_state_spinner_vendor);
        countySpinner = (Spinner) view.findViewById(R.id.company_county_spinner_vendor);
        states = new ArrayList<String>();
        stateIds = new ArrayList<>();
        county = new ArrayList<String>();
        countyIds = new ArrayList<String>();
        statesedit = new ArrayList<>();
        countyedit = new ArrayList<String>();
        stateIt = new ArrayList<String>();

        //validation
        inputCompanyname.addTextChangedListener(new MyTextWatcher(inputCompanyname));
        inputCompanydefaultemail.addTextChangedListener(new MyTextWatcher(inputCompanydefaultemail));

        //
        checkInternetConnection();

        buttonSaveCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();
                //validation
                if (!validateCompanyname()|| !validateCompanyemail())
                {
                    Toasty.error(getActivity(), "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                //
                else{
                    updateVendor();
                }
            }});


        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String County_ID = countyIds.get(position);

                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

                Logger.getInstance().Log("selected county id : " + County_ID);

                Logger.getInstance().Log("selected county  : " + countyname);
                //editor.putString("State_Name", State_Name);
                editor.putString("County_ID",County_ID);

                getCountyId();
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        getVendorData();
        getState();
        String vendor=sp.getString("Vendor_Id","");
        Log.e("vendor detail",vendor);
        return view;

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
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stateSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, states));
                stateSpinner.setSelection(states.indexOf(state));
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
    private void getCounty() {

        Log.e("State_ID", getStateId());
        Log.e("State", getStateId());


        Log.e("County_Name", getCountyId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTY_URL + getStateId(), null,

                new Response.Listener<JSONObject>() {

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
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        countySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, county));
                        countySpinner.setSelection(county.indexOf(countyname));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void updateVendor(){
        Logger.getInstance().Log("in update vendor id");
        showDialog();
        final String Company_Name = inputCompanyname.getText().toString().trim();
        final String  Company_Email = inputCompanydefaultemail.getText().toString().trim();
        final String  Address = inputAddress.getText().toString().trim();
        final String  ZipCode = inputZip.getText().toString().trim();
        final String  Phone_No = inputPhno.getText().toString().trim();
        final String  Fax_No = inputaFaxno.getText().toString().trim();

        pDialog.setMessage("Updating ...");
        Map<String, String> map = new HashMap<>();
        map.put("Vendor_Id",getVendorId());
        map.put("State",getStateId());
        map.put("County",getCountyId());
        map.put("Company_Name",Company_Name);
        map.put("Company_Email",Company_Email);
        map.put("Address",Address);
        map.put("ZipCode",ZipCode);
        map.put("Phone_No",Phone_No);
        map.put("Fax_No",Fax_No);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.VENDOR_EDIT_URL+getVendorId(),map, new Response.Listener<JSONObject>() {
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

                        Toasty.success(getActivity().getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else {
                        Toasty.error(getActivity().getApplicationContext(), "Update Not Successful", Toast.LENGTH_SHORT, true).show();
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
        });

        AppController.getInstance().addToRequestQueue(customRequest);
    }

    //validation

    private boolean validateCompanyname() {
        if (inputCompanyname.getText().toString().trim().isEmpty()) {
            inputLayoutCompanyname.setError(getString(R.string.err_msg_companyname));
            requestFocus(inputCompanyname);
            return false;
        } else {
            inputLayoutCompanyname.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateCompanyemail() {
        String companyemail = inputCompanydefaultemail.getText().toString().trim();

        if (companyemail.isEmpty() || !isValidCompanyemail(companyemail)) {
            inputLayoutCompanydefaultemail.setError(getString(R.string.err_msg_companyemail));
            requestFocus(inputCompanydefaultemail);
            return false;
        } else {
            inputLayoutCompanydefaultemail.setErrorEnabled(false);
        }

        return true;
    }

    private  boolean isValidCompanyemail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

                case R.id.input_companyname_vendor:
                    validateCompanyname();
                    break;

                case R.id.input_groupemail_vendor:
                    validateCompanyemail();
                    break;

            }
        }
    }


//

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
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String State_ID = stateIds.get(position);



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

}



