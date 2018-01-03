package com.drnds.titlelogy.activity.client.createuseractivity;

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
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.activity.client.subclientactivity.SubClientActivity;
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

public class CreateUserActivity extends AppCompatActivity {
    private EditText email,altemail,fname,lname,password,cnfpassword;
    private Button submit,cancel;
    private ProgressDialog pDialog;
    private TextInputLayout inputlayoutemail,inputlayoutfirstname,inputlayoutpassword,inputlayoutcnfpassword;
    SharedPreferences pref;
    private Toolbar toolbar;
    private static String TAG = CreateUserActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        pref = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar_createuser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New User");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        email=(EditText)findViewById(R.id.input_createuseremail);
        altemail=(EditText)findViewById(R.id.input_createuseraltemail);
        fname=(EditText)findViewById(R.id.input_createuserfname);
        lname=(EditText)findViewById(R.id.input_createuserlname);
        password=(EditText)findViewById(R.id.input_createuserpassword);
        cnfpassword=(EditText)findViewById(R.id.input_createuserconpassword);
        inputlayoutemail=(TextInputLayout)findViewById(R.id.input_layout_createuseremail);
        inputlayoutfirstname=(TextInputLayout)findViewById(R.id.input_layout_createuserfname);
        inputlayoutpassword=(TextInputLayout)findViewById(R.id.input_layout_createuserpassword);
        inputlayoutcnfpassword=(TextInputLayout)findViewById(R.id.input_layout_createuserconpassword);
        submit=(Button)findViewById(R.id.button_createusersubmit);
        cancel=(Button)findViewById(R.id.button_createusercancel);

        email.addTextChangedListener(new MyTextWatcher(email));
        fname.addTextChangedListener(new MyTextWatcher(fname));
        password.addTextChangedListener(new MyTextWatcher(password));
        cnfpassword.addTextChangedListener(new MyTextWatcher(cnfpassword));
        checkInternetConnection();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();   // checking internet connection
                if (!validateEmail() || !validateFirstName() || !validatePassword() || !validateCnfPassword())
                {
                    Toasty.error(CreateUserActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }

                String newPass = password.getText().toString();
                String confirmPass = cnfpassword.getText().toString();
                if (!newPass.equals("") && !confirmPass.equals("") && !newPass.equals(confirmPass) && newPass.length()==confirmPass.length())
                {
                    Toasty.warning(CreateUserActivity.this, "Choose same as Entered Password", Toast.LENGTH_SHORT).show();

                    return;
                }

                String Email = email.getText().toString();
                String Altemail = altemail.getText().toString();
                if (!Email.equals("") && !Altemail.equals("") && Email.equals(Altemail)) {
                    Toasty.warning(CreateUserActivity.this,"Choose Different Alternate Email ", Toast.LENGTH_SHORT).show();
                    return ;
                }

                else{

                    submitCreateUser();
                }}
        });
        return;
    }
    public void   submitCreateUser(){
        Logger.getInstance().Log("in update client id");
        showDialog();
        final String Email = email.getText().toString().trim();
        final String  Alternative_Email = altemail.getText().toString().trim();
        final String  First_Name =  fname.getText().toString().trim();
        final String  Last_Name =  lname.getText().toString().trim();
        final String  Password = password.getText().toString().trim();
        final String  CnfPassword = cnfpassword.getText().toString().trim();


        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.CREATEUSER_URL+getClientId(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    boolean  error = response.getBoolean("error");
                    boolean  duplicate=response.getBoolean("duplicate");

                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error&&!duplicate)
                    {

                        Toasty.success(CreateUserActivity.this, "User Created Successfully", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                        goToNavigationActivity();
                    }else if (!error&&duplicate)
                    {

                        Toasty.error(CreateUserActivity.this, "This User already exist ", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else {
                        Toasty.error(CreateUserActivity.this, "User Creation Failed", Toast.LENGTH_SHORT, true).show();
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
                Map<String,String>params= new HashMap<String, String>();

                params.put("Client_Id",getClientId());
                params.put("First_Name",First_Name);
                params.put("Last_Name",Last_Name);
                params.put("Email",Email);
                params.put("Alternative_Email",Alternative_Email);
                params.put("Password",Password);
                params.put("CnfPassword",CnfPassword);



                Logger.getInstance().Log("in update email"+Email);



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

        return pref.getString("Client_Id","");

    }
    public String getUserId(){

        return pref.getString("Client_User_Id", "");
    }

// validation

    private boolean validateEmail() {
        String inemail = email.getText().toString().trim();

        if (inemail.isEmpty() || !isValidEmail(inemail)) {
            inputlayoutemail.setError(getString(R.string.err_msg_email));
            requestFocus(email);
            return false;
        } else {
            inputlayoutemail.setErrorEnabled(false);
        }

        return true;
    }
    private  boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean validateFirstName() {
        if (fname.getText().toString().trim().isEmpty()) {
            inputlayoutfirstname.setError(getString(R.string.err_msg_firstname));
            requestFocus(fname);
            return false;
        } else {
            inputlayoutfirstname.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputlayoutpassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            inputlayoutpassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCnfPassword(){

        String newPass = password.getText().toString();
        String confirmPass = cnfpassword.getText().toString();
        if (!newPass.equals("") && !confirmPass.equals("") && !newPass.equals(confirmPass) && newPass.length()==confirmPass.length())
        {
            Toasty.warning(CreateUserActivity.this,"Choose same as New Password", Toast.LENGTH_SHORT).show();
        }
        if (cnfpassword.getText().toString().trim().isEmpty()) {
            inputlayoutcnfpassword.setError(getString(R.string.err_msg_cnfpassword));
            requestFocus(cnfpassword);
            return false;
        } else {
            inputlayoutcnfpassword.setErrorEnabled(false);

            return true;

        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class MyTextWatcher implements TextWatcher {

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

                case R.id.input_createuseremail:
                    validateEmail();
                    break;

                case R.id.input_createuserfname:
                    validateFirstName();
                    break;

                case R.id.input_createuserpassword:
                    validatePassword();
                    break;

                case R.id.input_createuserconpassword:
                    validateCnfPassword();
                    break;
            }
        }
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
            TastyToast.makeText( CreateUserActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void goToNavigationActivity()
    {
        Intent intent = new Intent(CreateUserActivity.this, NavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",6);
        setResult(1002,intent);
        finish();
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateUserActivity.this, NavigationActivity.class);
                        intent.putExtra("refresh","no");
                        intent.putExtra("position",6);
                        setResult(1002,intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
//        Intent intent = new Intent(CreateUserActivity.this, NavigationActivity.class);
//        intent.putExtra("refresh","no");
//        intent.putExtra("position",6);
//        setResult(1002,intent);
//        finish();
    }


}
