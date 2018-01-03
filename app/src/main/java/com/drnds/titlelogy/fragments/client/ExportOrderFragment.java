package com.drnds.titlelogy.fragments.client;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class ExportOrderFragment extends Fragment {
    Button write, read;
    static String TAG = "ExelLog";
    private String filename = "myExcel.xls";
    private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";
    SharedPreferences sp;
    JSONObject jsonObject;
    private DownloadManager downloadManager;
    private ProgressDialog pDialog;
    String Data="Hello!!";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export, null);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        write = (Button) view.findViewById(R.id.write);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        prepareProcessingOrder();

        return view;
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
    }

    private void prepareProcessingOrder() {
    }


}

