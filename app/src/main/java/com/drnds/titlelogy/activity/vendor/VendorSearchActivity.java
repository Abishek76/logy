package com.drnds.titlelogy.activity.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.ResultActivity;
import com.drnds.titlelogy.activity.client.SearchActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class VendorSearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button submit;
    String Search;
    private EditText editText;
    TextInputLayout inputLayoutSearch;
    SharedPreferences sp;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search_vendor);
        sp =getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search Orders");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        editText=(EditText)findViewById(R.id.input_editsearch_vendor);
        submit=(Button)findViewById(R.id.button_searchorder_vendor);
        inputLayoutSearch = (TextInputLayout) findViewById(R.id.input_layout_search_vendor);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();   // checking internet connection

                if (!validatesearch()) {
                    return;
                }

                else{
                    submitSearch();
                }}
        });
    }
    public String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }
    public void  submitSearch(){
        final String submit=editText.getText().toString();
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.VENDOR_SEARCH_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean  error = jObj.getBoolean("error");

                    Logger.getInstance().Log("search111"+response);
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray=jObj.getJSONArray("Orders");
//                        hideDialog();
                        stopDialog();


                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String Sub_Client_Name = jsonObject.getString("Sub_Client_Name");
                            String Order_Number = jsonObject.getString("Order_Number");
                            String Status = jsonObject.getString("Status");
                            String Order_Type = jsonObject.getString("Order_Type");
                            String State = jsonObject.getString("State");
                            String County = jsonObject.getString("County");
                            String Property_Address = jsonObject.getString("Property_Address");
                            String Order_Id = jsonObject.getString("Order_Id");
                            String Order_Status = jsonObject.getString("Order_Status");




                        }
                        Intent intent = new Intent(VendorSearchActivity.this,VendorResultActivity.class);

                        intent.putExtra("SEARCH", response.toString());
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
                params.put("Vendor_Id",getVendorId());
                params.put("Order_Number",submit);



                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private boolean validatesearch() {
//        if (editText.getText().toString().trim().isEmpty()) {
//            inputLayoutSearch.setError(getString(R.string.err_msg_search));
//            requestFocus(editText);
//            return false;
//        }

        if(editText.getText().toString().trim().length()<2)
        {
            inputLayoutSearch.setError(getString(R.string.err_msg_search));
            requestFocus(editText);
            return false;
        }
//        if (editText.getText().toString().trim().length()<2) {
//            inputLayoutSearch.setError(getString(R.string.err_msg_search));
//            requestFocus(editText);
//            return false;
//        }
        else {
            inputLayoutSearch.setErrorEnabled(false);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //         check internet connection
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
            TastyToast.makeText( VendorSearchActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}

}
