package com.drnds.titlelogy.fragments.vendor.myaccount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.drnds.titlelogy.util.AppController.TAG;

/**
 * Created by Ajith on 9/25/2017.
 */

public class UserProfileTabFragmentVendor extends Fragment {
    SharedPreferences sp;
    private EditText inputEmail, inputAlternateemail, inputFirstname, inputLastname;
    private TextInputLayout inputLayoutEmail, inputLayoutAlternateemail, inputLayoutFirstname, inputLayoutLastname;
    private ProgressDialog pDialog;
    private Button buttonSaveuser;
    AlertDialog.Builder builder;
    String Email,Alternative_Email,First_Name,Last_Name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.vendor_user_profiletab, null);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        builder=new AlertDialog.Builder(getActivity());
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email_uservendor);
        inputEmail = (EditText) view.findViewById(R.id.input_email_uservendor);
        inputLayoutAlternateemail = (TextInputLayout) view.findViewById(R.id.input_layout_alternateemail_uservendor);
        inputAlternateemail = (EditText) view.findViewById(R.id.input_alternateemail_uservendor);
        inputLayoutFirstname = (TextInputLayout) view.findViewById(R.id.input_layout_firstname_uservendor);
        inputFirstname= (EditText) view.findViewById(R.id.input_firstname_uservendor);
        inputLayoutLastname = (TextInputLayout) view.findViewById(R.id.input_layout_lastname_uservendor);
        inputLastname= (EditText) view.findViewById(R.id.input_lastname_uservendor);
        buttonSaveuser=(Button)view.findViewById(R.id.button_saveuserinfo_vendor);

//        checkInternetConnection();
        //validation
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputFirstname.addTextChangedListener(new MyTextWatcher(inputFirstname));

        buttonSaveuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = inputEmail.getText().toString().trim();
                Alternative_Email = inputAlternateemail.getText().toString().trim();
                First_Name = inputFirstname.getText().toString().trim();
                Last_Name = inputLastname.getText().toString().trim();
                checkInternetConnection();
                // validation
                if (!validateEmail() || !validateFirstName())
                {
                    Toasty.error(getActivity(), "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }


                if (!validatealtemail())
                {
//                    Toasty.error(getActivity(), "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }

//                if (checkInternetConnection()){
//                    return;
//                }
                //
                else {
                    updateUserInfo();
                }
            }

        });

        getUserInfo();
        return view;
    }
    private String getVendorUserIdUser() {

        return sp.getString("Vendor_User_Id", "");

    }
    private void getUserInfo(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VENDORGETUSERINFO_URL+ getVendorUserIdUser(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Email=details.getString("Email");
                        String Alternative_Email=details.getString("Alternative_Email");
                        String First_Name=details.getString("First_Name");
                        String Last_Name=details.getString("Last_Name");
                        inputEmail.setText(Email);
                        inputAlternateemail.setText(Alternative_Email);
                        inputFirstname.setText(First_Name);
                        inputLastname.setText(Last_Name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void   updateUserInfo(){
        Logger.getInstance().Log("in update vendor id");




        pDialog.setMessage("Updating ...");
        showDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,Config.VENDOREDITUSERINFO_URL+ getVendorUserIdUser(),new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Logger.getInstance().Log("sucess string");
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean  error = jObj.getBoolean("error");

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params= new HashMap<String, String>();

                params.put("Vendor_User_Id", getVendorUserIdUser());
                params.put("Email",Email);
                params.put("Alternative_Email",Alternative_Email);
                params.put("First_Name",First_Name);
                params.put("Last_Name",Last_Name);

                Logger.getInstance().Log("Email : " +Email);





                return params;
            }




        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    // validation

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatealtemail() {

        String email = inputEmail.getText().toString();
        String altemail = inputAlternateemail.getText().toString();
        if (!email.equals("") && !altemail.equals("") && email.equals(altemail)) {
            Toasty.warning(getContext(),"Choose Different Alternate Email", Toast.LENGTH_SHORT).show();
            return false;
        }
//       else {
//            inputLayoutCurrent.setErrorEnabled(false);
        return true;
//        }
    }



    private boolean validateFirstName() {
        if (inputFirstname.getText().toString().trim().isEmpty()) {
            inputLayoutFirstname.setError(getString(R.string.err_msg_firstname));
            requestFocus(inputFirstname);
            return false;
        } else {
            inputLayoutFirstname.setErrorEnabled(false);
        }

        return true;
    }


    private  boolean isValidEmail(String email) {
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

                case R.id.input_email_uservendor:
                    validateEmail();
                    break;

                case R.id.input_firstname_uservendor:
                    validateFirstName();
                    break;

            }
        }
    }
    //

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

    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputEmail.setText("");
                inputAlternateemail.setText("");
                inputFirstname.setText("");
                inputLastname.setText("");
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}





