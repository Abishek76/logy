package com.drnds.titlelogy.activity.client.subclientactivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputLayout;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.LoginActivity;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.activity.client.createuseractivity.EditCreateUserActivity;
import com.drnds.titlelogy.fragments.client.createsubclientfrag.CreateSubclientFragment;
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

public class EditSubclientActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText custname, city, zip, addresss, email, altemail;
    private TextInputLayout inputcustname, inputcity, inputzip, inputaddress, inputemail, inputaltemail;
    private ProgressDialog pDialog;
    private Spinner state, county;
    private Button submit, delete;
    private ArrayList<String> statesclient;
    private ArrayList<String> stateclientIds;
    private ArrayList<String> countyclient;
    private ArrayList<String> countyclientIds;
    SharedPreferences sp,pref;
    private String name, cityu, zipu, addressu, emailu, altu, statname, countname;
    private static String TAG = EditSubclientActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subclient);
        sp = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pref = getApplicationContext().getSharedPreferences(
                "Subclient", 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editsubclient);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Subclient");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        custname = (EditText) findViewById(R.id.input_updatename);
        city = (EditText) findViewById(R.id.input_updatecity);
        zip = (EditText) findViewById(R.id.input_updatezip);
        addresss = (EditText) findViewById(R.id.input_updateaddress);
        email = (EditText) findViewById(R.id.input_updateemail);
        altemail = (EditText) findViewById(R.id.input_updatealtemail);
        inputcustname = (TextInputLayout) findViewById(R.id.input_layout_updatename);
        inputcity = (TextInputLayout) findViewById(R.id.input_layout_updatecity);
        inputzip = (TextInputLayout) findViewById(R.id.input_layout_updatezip);
        inputaddress = (TextInputLayout) findViewById(R.id.input_layout_updateaddress);
        inputemail = (TextInputLayout) findViewById(R.id.input_layout_updateemail);
        inputaltemail = (TextInputLayout) findViewById(R.id.input_layout_updatealtemail);
        state = (Spinner) findViewById(R.id.stateupdate_spinner);
        county = (Spinner) findViewById(R.id.countyupdate_spinner);
        statesclient = new ArrayList<String>();
        stateclientIds = new ArrayList<>();
        countyclientIds = new ArrayList<String>();
        countyclient = new ArrayList<String>();
        submit = (Button) findViewById(R.id.button_updatesubclient);

//        getting vallus
        Intent intent = getIntent();

        statname = intent.getStringExtra("State");
        countname = intent.getStringExtra("County");

        name = intent.getStringExtra("Sub_Client_Name");
        Logger.getInstance().Log("name of client"+name);
        cityu = intent.getStringExtra("City");
        zipu = intent.getStringExtra("Zip_Code");
        addressu = intent.getStringExtra("Address");
        emailu = intent.getStringExtra("Email");
        altu = intent.getStringExtra("Alternative_Email");
//       set values
        custname.setText(name);
        addresss.setText(addressu);
        city.setText(cityu);
        zip.setText(zipu);
        email.setText(emailu);
        altemail.setText(altu);
        checkInternetConnection();
        //validation
        email.addTextChangedListener(new EditSubclientActivity.MyTextWatcher(email));
        custname.addTextChangedListener(new EditSubclientActivity.MyTextWatcher(custname));


// set Fragmentclass Arguments



        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String State_ID = stateclientIds.get(position);


                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = pref.edit();

                // Logger.getInstance().Log("selected state id : " + State_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("State_ID", State_ID);
                editor.commit();
                getCounty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        county.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String County_ID = countyclientIds.get(position);

                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = pref.edit();

                // Logger.getInstance().Log("selected county id : " + County_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("County_ID", County_ID);

                getCountyId();
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();   // checking internet connection
                if (!validateEmail() || !validateCustName())
                {
                    Toasty.error(EditSubclientActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }


                String Email = email.getText().toString();
                String Altemail = altemail.getText().toString();
                if (!Email.equals("") && !Altemail.equals("") && Email.equals(Altemail)) {
                    Toasty.warning(EditSubclientActivity.this,"Choose Different Alternate Email ", Toast.LENGTH_SHORT).show();
                    return ;
                }


                else {
                    submitClient();
                }}
        });
        getStateClient();
    }
    private void getStateClient() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.STATE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        statesclient.add(details.getString("State"));
                        stateclientIds.add(details.getString("State_ID"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                state.setAdapter(new ArrayAdapter<String>(EditSubclientActivity.this, android.R.layout.simple_spinner_dropdown_item, statesclient));
                state.setSelection(statesclient.indexOf(statname));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public String getStateId() {


        return pref.getString("State_ID", "");
    }

    public String getCountyId() {


        return pref.getString("County_ID", "");
    }

    private void getCounty() {
        //Log.e("State_ID", getStateId());
        // Log.e("County", getCountyId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTY_URL + getStateId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");

                    countyclient.clear();
                    countyclientIds.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        countyclient.add(jsonObject.getString("County"));


                        countyclientIds.add(jsonObject.getString("County_ID"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                county.setAdapter(new ArrayAdapter<String>(EditSubclientActivity.this, android.R.layout.simple_spinner_dropdown_item, countyclient));
                county.setSelection(countyclient.indexOf(countname));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void submitClient() {
        Logger.getInstance().Log("in update client id");
        showDialog();
        final String Sub_Client_Name = custname.getText().toString().trim();
        final String City = city.getText().toString().trim();
        final String Address = addresss.getText().toString().trim();
        final String Zip_Code = zip.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Alternative_Email = altemail.getText().toString().trim();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITSUBCLIENT_URL + getSubClientId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    boolean  error = response.getBoolean("error");
                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error&&!duplicate){
                        Toasty.success(EditSubclientActivity.this, "Subclient Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        goToNavigationActivity();
                        hideDialog();
                    }else if(duplicate) {
                        Toasty.error(EditSubclientActivity.this, "Duplicate Name please Check", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else{
                        Toasty.success(EditSubclientActivity.this,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
                Log.d(TAG, response.toString());


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Sub_Client_Id", getSubClientId());
                params.put("Client_Id", getClientId());
                params.put("State", getStateId());
                params.put("County", getCountyId());
                params.put("Sub_Client_Name", Sub_Client_Name);
                params.put("City", City);
                params.put("Address", Address);
                params.put("Zip_Code", Zip_Code);
                params.put("Email", Email);
                params.put("Alternative_Email ", Alternative_Email);
                Logger.getInstance().Log("params454: " + params);
                Logger.getInstance().Log("name Check: " + Sub_Client_Name);
                Logger.getInstance().Log("name Check 2: " + custname.getText().toString());
                final String NAME_check = custname.getText().toString();
                if (name.equals(NAME_check))
                {
                    params.put("Duplicate_Check","0");
                }else {
                    params.put("Duplicate_Check","1");
                }

                Logger.getInstance().Log("Alternative_Email :" +params);


                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }


    public String getClientId() {

        return sp.getString("Client_Id", "");

    }

    public String getUserId() {

        return pref.getString("Client_User_Id", "");
    }

    public String getSubClientId() {
        Intent intent = getIntent();
        return intent.getStringExtra("Sub_Client_Id");
        // return pref.getString("Sub_Client_Id", "");
    }

    //validation

    private boolean validateEmail() {
        String inemail = email.getText().toString().trim();

        if (inemail.isEmpty() || !isValidEmail(inemail)) {
            inputemail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputemail.setErrorEnabled(false);
        }

        return true;
    }
    private  boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validateCustName() {
        if (custname.getText().toString().trim().isEmpty()) {
            inputcustname.setError(getString(R.string.err_msg_customername));
            requestFocus(custname);
            return false;
        } else {
            inputcustname.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

                case R.id.input_updateemail:
                    validateEmail();
                    break;

                case R.id.input_updatename:
                    validateCustName();
                    break;




            }
        }
    }




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void confirmDeleteEmployee() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);
        alertDialogBuilder.setMessage("Are you sure you want to delete ?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteEmployee() {
        Logger.getInstance().Log("in update client id");
        showDialog();


        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.DLTSUBCLIENT_URL + getSubClientId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");

                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error)
                    {

                        Toasty.error(EditSubclientActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT, true).show();

                        hideDialog();
                    }else {
                        Toasty.error( EditSubclientActivity.this,"Delete Unsuccessful",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                onBackPressed();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Sub_Client_Id", getSubClientId());



                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(customRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

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
            TastyToast.makeText( EditSubclientActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }
    private void goToNavigationActivity()
    {
        Intent intent = new Intent(EditSubclientActivity.this, NavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",5);
        setResult(1003,intent);
        finish();
    }
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Closing Activity")
//                .setMessage("Are you sure you want to close this activity?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(EditSubclientActivity.this, NavigationActivity.class);
//                        intent.putExtra("refresh","no");
//                        intent.putExtra("position",5);
//                        setResult(1003,intent);
//                        finish();
//                    }
//
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }

}













