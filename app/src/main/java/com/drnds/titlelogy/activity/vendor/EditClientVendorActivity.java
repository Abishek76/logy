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
import com.drnds.titlelogy.activity.client.createuseractivity.EditCreateUserActivity;
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

public class EditClientVendorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText clientcode;
    private TextInputLayout inputclientcode;
    private ProgressDialog pDialog;
    private Button submit;
    SharedPreferences pref;
    private String clientcodeu,clientid;
    private static String TAG = EditClientVendorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_vendor);
        pref = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        pref = getApplicationContext().getSharedPreferences(
                "Client", 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editclientvendor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Client");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        clientcode = (EditText) findViewById(R.id.input_clientcode);
        inputclientcode = (TextInputLayout) findViewById(R.id.input_layout_clientcode);
        submit = (Button) findViewById(R.id.button_updatesubclientvendor);

        //        getting vallus
        Intent intent = getIntent();


        clientcodeu = intent.getStringExtra("Client_Code");
        clientid= intent.getStringExtra("Client_Id");
//       set values
        clientcode.setText(clientcodeu);
        checkInternetConnection();




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                {
                    checkInternetConnection();

                    submitClient();
                }}
        });
    }



    public void submitClient() {
        Logger.getInstance().Log("in update vendor id");
        showDialog();
        final String Client_Code = clientcode.getText().toString().trim();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITVENDORCLIENT_URL +clientid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    boolean  error = response.getBoolean("error");
                    if (!error){
                        Toasty.success(EditClientVendorActivity.this, "Client Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        goToNavigationActivity();
                        hideDialog();
                    }else{
                        Toasty.error(EditClientVendorActivity.this, "Update Not Successful", Toast.LENGTH_SHORT, true).show();
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

                params.put("Client_Id", clientid);
                params.put("Client_Code", Client_Code);





                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }


    public String getVendorId() {

        return pref.getString("Vendor_Id", "");

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
                        Intent intent = new Intent(EditClientVendorActivity.this, VendorNavigationActivity.class);
                        intent.putExtra("refresh","yes");
                        intent.putExtra("position",4);
                        setResult(1009,intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    private void goToNavigationActivity()
    {
        Intent intent = new Intent(EditClientVendorActivity.this, VendorNavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",4);
        setResult(1009,intent);
        finish();
    }

//    private void goToActivity()
//    {
//        Intent intent = new Intent(EditClientVendorActivity.this, VendorNavigationActivity.class);
//        startActivity(intent);
//        finish();
//    }


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
            TastyToast.makeText( EditClientVendorActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }

}







