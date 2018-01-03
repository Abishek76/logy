package com.drnds.titlelogy.activity.client;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.drnds.titlelogy.util.SessionManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public static final String LOGINPREFS_NAME = "LoginActivity";
    public  SharedPreferences sp;
    String Email,Password;
    TextInputLayout  inputLayoutEmail, inputLayoutPassword;
    EditText userEmail,password;
    private static String TAG = LoginActivity.class.getSimpleName();
    Button btnLogin;
    SessionManager session;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        builder=new AlertDialog.Builder(LoginActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login as Client");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        session = new SessionManager(getApplicationContext());
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        userEmail=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);

        btnLogin=(Button)findViewById(R.id.btn_loginscreen);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Email=userEmail.getText().toString();
                Password=password.getText().toString();

                checkInternetConnection();   // checking internet connection

                if (!validateEmail()) {   //email validation
                    return;
                }

                if (!validatePassword()) {   //password validation
                    return;
                }

                if(Email.equals("")||Password.equals("")){
                    builder.setTitle("Some thing went wrong");
                    displayAlert("Enter a  valid Email name and password");
                }

                else {
                    pDialog.setMessage("Logging in ...");
                    showDialog();
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Logger.getInstance().Log("in response");
                            hideDialog();

                            try {
                                JSONObject jObj = new JSONObject(response);
                                Log.e(TAG, response.toString());
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
                                        String Client_User_Id = jsonObject.getString("Client_User_Id");
                                        String Client_Id = jsonObject.getString("Client_Id");



                                        sp = getApplicationContext().getSharedPreferences(
                                                "LoginActivity", 0); // 0 for private mode

                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("Client_User_Id", Client_User_Id);
                                        editor.putString("Client_Id", Client_Id);
                                        editor.putString("Email", Email);
                                        editor.putString("Password", Password);
                                        editor.commit();

                                    }
                                    // Inserting row in users table

                                    session.createLoginSession(Email,Password);

                                    // Staring MainActivity
                                    startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                                    finish();
                                    // Launch main activity
//                                    Intent intent = new Intent( LoginActivity.this,
//                                            NavigationActivity.class);
//                                             startActivity(intent);
//                                                  finish();

                                }else{
                                    Logger.getInstance().Log("in error ");
                                    TastyToast.makeText( LoginActivity.this,"Enter Valid Credentials...",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
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

                            Logger.getInstance().Log("login details"+params);

                            return params;
                        }


                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppController.getInstance().addToRequestQueue(stringRequest);
                }

            }

//       email validation
            private boolean validateEmail() {
                String inemail = userEmail.getText().toString().trim();

                if (inemail.isEmpty() || !isValidEmail(inemail)) {
                    inputLayoutEmail.setError(getString(R.string.err_msg_email));
                    requestFocus(userEmail);
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
                if (password.getText().toString().trim().isEmpty()) {
                    inputLayoutPassword.setError(getString(R.string.err_msg_password));
                    requestFocus(password);
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
                    TastyToast.makeText( LoginActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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

        });


    }


    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userEmail.setText("");
               password.setText("");
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
