package com.drnds.titlelogy.fragments.vendor.myaccount;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajith on 9/25/2017.
 */

public class ResetPasswordFragmentVendor extends Fragment {
    SharedPreferences spreset;
    private EditText inputCurrent, inputNew, inputReenter;
    private TextInputLayout inputLayoutCurrent, inputLayoutinputNew, inputLayoutReenter;
    private ProgressDialog pDialog;
    private Button buttonSavePassword;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendor_reset_password, container, false);

        spreset = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        inputLayoutCurrent = (TextInputLayout) view.findViewById(R.id.input_layout_currentpassword_vendor);
        inputCurrent = (EditText) view.findViewById(R.id.input_currentpassword_vendor);
        inputLayoutinputNew = (TextInputLayout) view.findViewById(R.id.input_layout_newpassword_vendor);
        inputNew = (EditText) view.findViewById(R.id.input_newpassword_vendor);
        inputLayoutReenter = (TextInputLayout) view.findViewById(R.id.input_layout_reenternewpassword_vendor);
        inputReenter = (EditText) view.findViewById(R.id.input_reenternewpassword_vendor);
        buttonSavePassword=(Button)view.findViewById(R.id.button_savepassword_vendor);
        inputCurrent.addTextChangedListener(new MyTextWatcher(inputCurrent));
        inputNew.addTextChangedListener(new MyTextWatcher(inputNew));
        inputReenter.addTextChangedListener(new MyTextWatcher(inputReenter));
        checkInternetConnection();

        buttonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkInternetConnection();   // checking internet connection
                if (!validateCurrentPassword() || !validatenewpassword() || !validatereenterpassword())
                {
                    Toasty.error(getActivity(), "Please enter valid credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }
//                if (checkInternetConnection()){
//                    return;
//                }
                else {
                    updatePassword();


                }}
        });



        return view;
    }
    public String getVendorUserId() {

        return spreset.getString("Vendor_User_Id", "");

    }

    public void   updatePassword(){
        Logger.getInstance().Log("in update vendor id");
        pDialog.setMessage("Updating ...");
        showDialog();
        final String Current_Password = inputCurrent.getText().toString().trim();
        final String  New_Password = inputNew.getText().toString().trim();
        final String  Confirm_NewPassword = inputReenter.getText().toString().trim();
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.VENDOR_PASSWORD_URL+getVendorUserId(),null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
                    if (!error){
                        Toasty.success( getActivity().getApplicationContext(),"  Password Updated Successfully...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }else{
                        Toasty.error( getActivity().getApplicationContext(),"  Enter Valid Credentials",Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("Current_Password",Current_Password);
                params.put("New_Password",New_Password);
                params.put("Confirm_NewPassword",Confirm_NewPassword);


                params.put("Vendor_User_Id", getVendorUserId());
                Logger.getInstance().Log("Vendor_User_Id : " + getVendorUserId());
                Logger.getInstance().Log("Current_Password : " + Current_Password);
                Logger.getInstance().Log("New_Password : " + New_Password);
                Logger.getInstance().Log("Confirm_NewPassword : " + Confirm_NewPassword);


                return params;
            }


        };
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    private boolean validateCurrentPassword() {
        if (inputCurrent.getText().toString().trim().isEmpty()) {
            inputLayoutCurrent.setError(getString(R.string.err_msg_currentpassword));
            requestFocus(inputCurrent);
            return false;
        } else {
            inputLayoutCurrent.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private boolean validatenewpassword() {

        String oldPass = inputCurrent.getText().toString();
        String newPass = inputNew.getText().toString();
        if (!oldPass.equals("") && !newPass.equals("") && oldPass.equals(newPass)) {
            Toasty.warning(getContext(),"Choose Different Password than Old Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (inputNew.getText().toString().trim().isEmpty()) {
            inputLayoutinputNew.setError(getString(R.string.err_msg_newpassword));
            requestFocus(inputNew);
            return false;
        } else {
            inputLayoutCurrent.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validatereenterpassword(){

        String newPass = inputNew.getText().toString();
        String confirmPass = inputReenter.getText().toString();
        if (!newPass.equals("") && !confirmPass.equals("") && !newPass.equals(confirmPass) && newPass.length()==confirmPass.length())
        {
            Toasty.warning(getContext(),"Choose same as New Password", Toast.LENGTH_SHORT).show();
        }
        if (inputReenter.getText().toString().trim().isEmpty()) {
            inputLayoutReenter.setError(getString(R.string.err_msg_reenterpassword));
            requestFocus(inputReenter);
            return false;
        } else {
            inputLayoutCurrent.setErrorEnabled(false);
            return true;
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

                case R.id.input_currentpassword_vendor:
                    validateCurrentPassword();
                    break;

                case R.id.input_newpassword_vendor:
                    validatenewpassword();
                    break;

                case R.id.input_reenternewpassword_vendor:
                    validatereenterpassword();
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



}



