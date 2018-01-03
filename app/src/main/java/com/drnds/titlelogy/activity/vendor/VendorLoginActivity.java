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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.WelcomeActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.drnds.titlelogy.util.SessionManager;
import com.drnds.titlelogy.util.SessionVendor;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VendorLoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText inputEmail, inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private Button btnLogin;
    private SessionVendor sessionVendor;
    public static final String VENDORLOGINPREFS_NAME = "VendorLoginActivity";
    public SharedPreferences sp;
    String Email,Password,Vendor_User_Id,Vendor_Id;
    private static String TAG = VendorLoginActivity.class.getSimpleName();
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vendor_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar_login_vendor);
        builder=new AlertDialog.Builder(VendorLoginActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login as Vendor");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        sessionVendor = new SessionVendor(getApplicationContext());
        inputEmail = (EditText) findViewById(R.id.input_email_vendor);
        inputPassword = (EditText) findViewById(R.id.input_password_vendor);

        btnLogin = (Button) findViewById(R.id.btn_loginscreen_vendor);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email_vendor);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password_vendor);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Email=inputEmail.getText().toString();
                Password=inputPassword.getText().toString();

                checkInternetConnection();   // checking internet connection

                if (!validateEmail()) {   //email validation
                    return;
                }

                if (!validatePassword()) {   //password validation
                    return;
                }

                if(Email.equals("")||Password.equals("")){
                    builder.setTitle("Some thing went wrong");
                    displayAlert("Enter a  valid Email and password");
                }

                else {
                    pDialog.setMessage("Logging in ...");
                    showDialog();
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.VENDORLOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Logger.getInstance().Log("in response");
                            hideDialog();

                            try {
                                JSONObject jObj = new JSONObject(response);
                                Log.d(TAG, response.toString());
                                boolean  error = jObj.getBoolean("error");

                                Logger.getInstance().Log("in error response"+error);
                                // Check for error node in json
                                if (!error)
                                {
                                    // user successfully logged in
                                    // Create login session
                                    // Now store the user in SQLite
                                    JSONArray jsonArray=jObj.getJSONArray("Users");
                                    for(int i=0;i<jsonArray.length();i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String Email = jsonObject.getString("Email");
                                        String Password = jsonObject.getString("Password");
                                        String Vendor_User_Id = jsonObject.getString("Vendor_User_Id");
                                        String Vendor_Id = jsonObject.getString("Vendor_Id");


                                        Logger.getInstance().Log("vendor id login"+Vendor_Id);
                                        sp = getApplicationContext().getSharedPreferences(
                                                "VendorLoginActivity", 0); // 0 for private mode

                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("Vendor_User_Id", Vendor_User_Id);
                                        editor.putString("Vendor_Id", Vendor_Id);
                                        editor.putString("Email", Email);
                                        editor.commit();

                                    }
                                    // Inserting row in users table

                                    sessionVendor.createLoginSession(Email,Password);
                                    // Launch main activity
                                    Intent intent = new Intent( VendorLoginActivity.this,
                                            VendorNavigationActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Logger.getInstance().Log("in error ");
                                    TastyToast.makeText( VendorLoginActivity.this,"Enter Valid Credentials...",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            error.printStackTrace();
                            hideDialog();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String>params= new HashMap<String, String>();
                            params.put("Email",Email);
                            params.put("Password",Password);
                            Logger.getInstance().Log("vendor id login"+params);

                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(stringRequest);
                }

            }
        });


    }

    //       email validation
    private boolean validateEmail() {
        String inemail = inputEmail.getText().toString().trim();

        if (inemail.isEmpty() || !isValidEmail(inemail)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private  boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //       password validation
    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
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

                case R.id.input_email_vendor:
                    validateEmail();
                    break;
                case R.id.input_password_vendor:
                    validatePassword();
                    break;
            }
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
            TastyToast.makeText( VendorLoginActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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




    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputEmail.setText("");
                inputPassword.setText("");
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {

        Intent intent=new Intent(this,WelcomeActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


}
