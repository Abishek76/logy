package com.drnds.titlelogy.fragments.client.scoreboardfragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.CompletedActivity;
import com.drnds.titlelogy.activity.client.ReportActivity;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ajith on 11/27/2017.
 */

public class CompletedTabFragments extends Fragment {
    EditText frmDate, toDate;
    private TextInputLayout inputLayoutFromdate, inputLayoutTodate;
    Button submit;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog pDialog;
    SharedPreferences sp;
    String  FromDate,ToDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completedtab, container, false);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        frmDate = (EditText) view.findViewById(R.id.com_frm_date);
        toDate = (EditText) view.findViewById(R.id.com_to_date);
        inputLayoutFromdate = (TextInputLayout) view.findViewById(R.id.comtab_input_layout_frmdate);
        inputLayoutTodate = (TextInputLayout) view.findViewById(R.id.comtab_input_layout_todate);
        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
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
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
        submit=(Button)view.findViewById(R.id.com_submittab);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
               // checking internet connection

               FromDate = frmDate.getText().toString().trim();
               ToDate = toDate.getText().toString().trim();

                // find the radiobutton by returned id
                if (!validateFromdate() || !validateTodate())
                {
                    stopDialog();
                    Toasty.error(getActivity(), "Please select Dates!", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                else {
                    Intent intent = new Intent(getActivity(), CompletedActivity.class);


                    intent.putExtra("Report_Type", "2");
                    intent.putExtra("From_Date1", FromDate);
                    intent.putExtra("To_Date1", ToDate);
                    intent.putExtra("Client_Id1",getClientId());

                    startActivity(intent);
                    hideDialog();
                }
                // print the value of selected super star

            }
        });
        return view;
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
    public String getClientId() {

        return sp.getString("Client_Id", "");
    }
    public void fireApi(){
        pDialog.setMessage("Fetching Data ...");

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
                params.put("Report_Type", "2");
                params.put("From_Date", FromDate );
                params.put("To_Date",ToDate);
                Logger.getInstance().Log("Report_Type"+params);

                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
