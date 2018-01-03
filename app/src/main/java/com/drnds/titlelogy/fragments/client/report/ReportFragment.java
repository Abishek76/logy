package com.drnds.titlelogy.fragments.client.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.ReportActivity;
import com.drnds.titlelogy.activity.client.gridactivity.GridviewActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ajithkumar on 8/17/2017.
 */

public class ReportFragment extends Fragment {
    EditText frmDate, toDate;
    RadioGroup radioGroup;
    private TextInputLayout inputLayoutFromdate, inputLayoutTodate;
    RadioButton pendingorder,completedorder,allorder;
    Button submit;
    String Report_Type;
    SharedPreferences sp;
    private ProgressDialog pDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.fragment_report, null);
        radioGroup=(RadioGroup)view.findViewById(R.id.myradiogroup);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        frmDate=(EditText)view.findViewById(R.id.frm_date);
        toDate=(EditText)view.findViewById(R.id.to_date);
        inputLayoutFromdate = (TextInputLayout) view.findViewById(R.id.input_layout_frmdate);
        inputLayoutTodate = (TextInputLayout) view.findViewById(R.id.input_layout_todate);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        frmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();



                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                frmDate.setText(dateFormatter.format(newDate.getTime()));


                            }
                        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

        toDate.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Calendar newCalendar = Calendar.getInstance();


                                          DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                                  new DatePickerDialog.OnDateSetListener() {




                                                      @Override
                                                      public void onDateSet(DatePicker view, int year,
                                                                            int monthOfYear, int dayOfMonth) {
                                                          Calendar newDate = Calendar.getInstance();
                                                          newDate.set(year, monthOfYear, dayOfMonth);
                                                          toDate.setText(dateFormatter.format(newDate.getTime()));



                                                      }







                                                  }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                                          datePickerDialog.show();





                                      }

        });




        pendingorder = (RadioButton) view.findViewById(R.id.pendingorder);
        completedorder = (RadioButton) view.findViewById(R.id.completedorder);
        allorder = (RadioButton) view.findViewById(R.id.allorder);
        submit=(Button)view.findViewById(R.id.submitreport);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                checkInternetConnection();   // checking internet connection

                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                if (!validateFromdate() || !validateTodate())
                {
                    stopDialog();
                    Toasty.error(getActivity(), "Please select Dates!", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                else if (selectedId== R.id.pendingorder) {
                    Report_Type="1";
                    fireReport();
                } else if (selectedId== R.id.completedorder) {
                    Report_Type="2";
                    fireReport();

                } else if (selectedId== R.id.allorder) {
                    Report_Type="3";
                    fireReport();

                }
                // print the value of selected super star

            }
        });
//        saveExcelFile(getActivity(), filename);

        return view;
    }
    public void fireReport(){
        pDialog.setMessage("Fetching Data ...");
        final String  FromDate = frmDate.getText().toString().trim();
        final String  ToDate = toDate.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.REPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    boolean  error = jObj.getBoolean("error");

                    Logger.getInstance().Log("in error response"+response);
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray=jObj.getJSONArray("Orders");
                        if(jsonArray.isNull(0)){
                            stopDialog();
                            TastyToast.makeText( getActivity().getApplicationContext(),"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                        }else {
                            stopDialog();
                            Intent intent = new Intent(getActivity(),ReportActivity.class);

                            intent.putExtra("JsonReport", response.toString());
                            startActivity(intent);
                        }
//
                    }else

                    {
                        TastyToast.makeText( getActivity().getApplicationContext(),"Some thing went wrong",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

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
                params.put("Client_Id",getClientId());
                params.put("Report_Type", Report_Type);
                params.put("From_Date", FromDate );
                params.put("To_Date",ToDate);
                Logger.getInstance().Log("Report_Type"+params);

                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }
    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private boolean validateFromdate() {
        if (frmDate.getText().toString().trim().isEmpty()) {
            inputLayoutFromdate.setError(getString(R.string.err_msg_frmdate));
            requestFocus(frmDate);
            return false;
        } else {
            inputLayoutFromdate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateTodate() {
        if (toDate.getText().toString().trim().isEmpty()) {
            inputLayoutTodate.setError(getString(R.string.err_msg_todate));
            requestFocus(toDate);
            return false;
        } else {
            inputLayoutTodate.setErrorEnabled(false);
        }

        return true;
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

                case R.id.frm_date:
                    validateFromdate();
                    break;

                case R.id.to_date:
                    validateTodate();
                    break;

            }
        }
    }




    //         check internet connection
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

}