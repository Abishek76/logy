package com.drnds.titlelogy.fragments.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorPiechartActivity;
import com.drnds.titlelogy.fragments.client.MyValueFormatter;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ajith on 9/23/2017.
 */

public class DashboardFragmentVendor extends Fragment {
    private ProgressDialog pd;

    ImageView imageView;

    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues=new ArrayList<>();
    ArrayList<String> xAxis1=  new ArrayList<String>();
    BarEntry values ;
    HorizontalBarChart chart;
    SharedPreferences sp;
    BarData data;
    String No_Of_Orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vendor_fragment_dashboard, container, false);


        pd = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pd.setMessage("loading");

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        // Log.d("array",Arrays.toString(fullData));
//        chart.setTouchEnabled(false);


        // Capture button clicks
        imageView=(ImageView)view.findViewById(R.id.vendorswitch_pie);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VendorPiechartActivity.class);
                startActivity(intent);
            }
        });


        chart = (HorizontalBarChart) view.findViewById(R.id.vendorchart);
        chart.setDrawBarShadow(false);
        chart.setPinchZoom(false);
        chart.setDrawValueAboveBar(true);
        chart.getXAxis().setEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);
        chart.setHighlightEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        xl.setDrawGridLines(false);

        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
//


        // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);


        l.setXEntrySpace(5f);

        checkInternetConnection();   // checking internet connection

        getPiechart();

        return view;
    }

    public void getPiechart(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.VENDORDASHBOARD_URL+getVendorId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Logger.getInstance().Log("in response");

                pd.show();

                xAxis1 = new ArrayList<>();
                yAxis = null;
                yValues = new ArrayList<>();

                try {
                    JSONObject jObj = new JSONObject(response);



                    {

                        ArrayList<String> xEntrys = new ArrayList<>();
                        JSONArray jsonArray=jObj.getJSONArray("ScoreBoard");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int Value = jsonObject.getInt("Value");
                            No_Of_Orders = jsonObject.getString("No_Of_Orders");







                            values = new BarEntry(Integer.valueOf(Value),i);
                            yValues.add(values);


                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BarDataSet barDataSet1 = new BarDataSet(yValues, "Orders");
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c: ColorTemplate.VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c: ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c: ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);
                for (int c: ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);
                for (int c: ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                barDataSet1.setColors(colors);
//                barDataSet1.setColor(Color.WHITE);
                xAxis1.add("Hold");
                xAxis1.add("Clarification");
                xAxis1.add("Cancelled");
                xAxis1.add("Taxcertificate");
                xAxis1.add("FinalReview");
                xAxis1.add("PropertytypingQc");
                xAxis1.add("Propertytyping");
                xAxis1.add("TitleSearchQc");
                xAxis1.add("TitleSearch");
                xAxis1.add("NewOrders");
                xAxis1.size();
                yAxis = new ArrayList<>();
                yAxis.add(barDataSet1);


//                String No_Of_Orders[]= xAxis1.toArray(new String[xAxis1.size()]);
                data = new BarData(xAxis1,yAxis);
                data.setValueFormatter(new MyValueFormatter());
                chart.setData(data);
                chart.setDescription("");
                chart.animateXY(3000, 3000);
                chart.invalidate();
                barDataSet1.setValueTextSize(12f);
                pd.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public String getVendorId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Vendor_Id", "");


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



