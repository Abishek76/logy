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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.NavigationActivity;
import com.drnds.titlelogy.activity.client.subclientactivity.EditSubclientActivity;
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

import static com.drnds.titlelogy.util.AppController.TAG;

public class EditCreateUserActivity extends AppCompatActivity {
    private EditText fname,lname,email,altemail;
    private TextInputLayout inputlayoutemail,inputlayoutfirstname;
    private Button submit;
    private ProgressDialog pDialog;
    private ProgressBar spinner;
    SharedPreferences pref,user;
    private String firname,lasname,useremail,useraltemail;
    private static String TAG = EditCreateUserActivity.class.getSimpleName();
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_create_user);
        pref = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar_updatecreateuser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit User");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        fname = (EditText) findViewById(R.id.input_userfname);
        lname = (EditText) findViewById(R.id.input_userlastname);
        email = (EditText) findViewById(R.id.input_useremail);
        altemail = (EditText) findViewById(R.id.input_alternativeemail);
        submit = (Button) findViewById(R.id.button_updatecreateuser);
        inputlayoutemail= (TextInputLayout)findViewById(R.id.input_layout_useremail);
        inputlayoutfirstname= (TextInputLayout)findViewById(R.id.input_layout_userfname);
        Intent intent = getIntent();

        firname = intent.getStringExtra("First_Name");
        lasname = intent.getStringExtra("Last_Name");
        useremail = intent.getStringExtra("Email");
        useraltemail = intent.getStringExtra("Alternative_Email");
        fname.setText(firname);
        lname.setText(lasname);
        email.setText(useremail);
        altemail.setText(useraltemail);
        checkInternetConnection();
        //validation
        email.addTextChangedListener(new MyTextWatcher(email));
        fname.addTextChangedListener(new MyTextWatcher(fname));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();   // checking internet connection
                if (!validateEmail() || !validateFirstName())
                {
                    Toasty.error(EditCreateUserActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }

                String Email = email.getText().toString();
                String Altemail = altemail.getText().toString();
                if (!Email.equals("") && !Altemail.equals("") && Email.equals(Altemail)) {
                    Toasty.warning(EditCreateUserActivity.this,"Choose Different Alternate Email ", Toast.LENGTH_SHORT).show();
                    return ;
                }

                else{

                    createUserSubmit();
                }
            }
        });
    }
    public String getUserId() {
        Intent intent = getIntent();
        return intent.getStringExtra("Client_User_Id");
    }
    public void createUserSubmit(){
        Logger.getInstance().Log("in update client id");
        showDialog();
        final String First_Name = fname.getText().toString().trim();
        final String Last_Name = lname.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        final String Alternative_Email = altemail.getText().toString().trim();


        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITCLIENTUSER_URL + getUserId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error&&!duplicate){
                        Toasty.success(EditCreateUserActivity.this, "User Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        goToNavigationActivity();
                        hideDialog();
                    }else if(duplicate){
                        Toasty.error(EditCreateUserActivity.this, "Email already exists", Toast.LENGTH_SHORT, true).show();
                        hideDialog();
                    }else{
                        Toasty.success(EditCreateUserActivity.this,"Not updated...",Toast.LENGTH_SHORT).show();
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

                params.put("Client_User_Id", getUserId());


                params.put("First_Name", First_Name);
                params.put("Last_Name", Last_Name);
                params.put("Email", Email);
                params.put("Alternative_Email", Alternative_Email);
                final String email_check = email.getText().toString().trim();
                if (useremail.equals(email_check))
                {
                    params.put("Duplicate_Check","0");
                }else {
                    params.put("Duplicate_Check","1");
                }

                Logger.getInstance().Log("parabola: " + params);



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

        return pref.getString("Client_Id", "");

    }


    //validation

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

                case R.id.input_useremail:
                    validateEmail();
                    break;

                case R.id.input_userfname:
                    validateFirstName();
                    break;




            }
        }
    }

    //
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
            TastyToast.makeText( EditCreateUserActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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
        Intent intent = new Intent(EditCreateUserActivity.this, NavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",6);
        setResult(1004,intent);
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
                        Intent intent = new Intent(EditCreateUserActivity.this, NavigationActivity.class);
                        intent.putExtra("refresh","no");
                        intent.putExtra("position",6);
                        setResult(1004,intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
