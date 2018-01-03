package com.drnds.titlelogy.activity.vendor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class EditSubclientVendorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText subclientcode, invoicecontactname, address;
    private TextInputLayout inputsubclientcode, inputinvoicecontactname, inputaddress;
    private ProgressDialog pDialog;
    private Button submit;
    SharedPreferences pref;
    private String subclientcodeu, addressu, invoicecontactnameu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subclient_vendor);
        pref = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        pref = getApplicationContext().getSharedPreferences(
                "Subclient", 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editsubclient);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Subclient");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        subclientcode = (EditText) findViewById(R.id.input_subclientcode);
        address = (EditText) findViewById(R.id.input_addressvendor);
        invoicecontactname = (EditText) findViewById(R.id.input_invoicecontactname);
        inputsubclientcode = (TextInputLayout) findViewById(R.id.input_layout_subclientcode);
        inputaddress = (TextInputLayout) findViewById(R.id.input_layout_addressvendor);
        inputinvoicecontactname = (TextInputLayout) findViewById(R.id.input_layout_invoicecontactname);
        submit = (Button) findViewById(R.id.button_updatesubclientvendor);

        Intent intent = getIntent();

        Logger.getInstance().Log("in Id...."+getSubClientId());
        subclientcodeu = intent.getStringExtra("Sub_Process_Code");
        invoicecontactnameu = intent.getStringExtra("Invoice_Contact_Name");
        addressu = intent.getStringExtra("Address");
        checkInternetConnection();

        subclientcode.setText(subclientcodeu);
        invoicecontactname.setText(invoicecontactnameu);
        address.setText(addressu);
        checkInternetConnection();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();   // checking internet connection


                submitVClient();

//                if (!validateSubprocesscode() || !validateInvoicename() || !validateAddress()) {
//                    Toasty.error(EditSubclientActivityVendor.this, "Please enter all credentials!", Toast.LENGTH_SHORT, true).show();
//                    return;
//                } else {
//                    submitClient();

            }
        });
    }

    public String getSubClientId() {
        Intent intent = getIntent();
        return intent.getStringExtra("Subclientvendor");
        // return pref.getString("Sub_Client_Id", "");
    }

    public void  submitVClient(){
        Logger.getInstance().Log("in update vendor id");
        showDialog();
        final String Sub_Process_Code = subclientcode.getText().toString().trim();
        final String Address = address.getText().toString().trim();
        final String Invoice_Contact_Name = invoicecontactname.getText().toString().trim();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITVENDORSUBCLIENT_URL + getSubClientId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    boolean  error = response.getBoolean("error");
                    if (!error){
                        Toasty.success(EditSubclientVendorActivity.this, "Subclient Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        goToNavigationActivity();
                        hideDialog();
                    }else{
                        Toasty.error(EditSubclientVendorActivity.this, "Update Not Successful", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json



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
                Logger.getInstance().Log("Sub_Client_Id : " + getSubClientId());
                params.put("Sub_Process_Code", Sub_Process_Code);
                params.put("Address", Address);
                params.put("Invoice_Contact_Name", Invoice_Contact_Name);

//                Logger.getInstance().Log("Sub_Process_Code : " + Sub_Process_Code);
                Logger.getInstance().Log("Address :" + Address);
                Logger.getInstance().Log("Invoice_Contact_Name :" + Invoice_Contact_Name);


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

        return pref.getString("Vendor_Id", "");

    }


    public String getUserId() {

        return pref.getString("Vendor_User_Id", "");
    }



    //validation




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditSubclientVendorActivity.this, VendorNavigationActivity.class);
                        intent.putExtra("refresh","yes");
                        intent.putExtra("position",3);
                        setResult(1001,intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


//    private void goToActivity()
//    {
//        Intent intent = new Intent(EditSubclientVendorActivity.this, VendorNavigationActivity.class);
//        startActivity(intent);
//        finish();
//    }

    private void goToNavigationActivity()
    {
        Intent intent = new Intent(EditSubclientVendorActivity.this, VendorNavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",3);
        setResult(1001,intent);
        finish();
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
            TastyToast.makeText( EditSubclientVendorActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }
}


