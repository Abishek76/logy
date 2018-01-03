package com.drnds.titlelogy.activity.client.chartactivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.fragments.client.MyValueFormatter;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LinechartActivity extends AppCompatActivity {
    SharedPreferences sp;
    private Toolbar toolbar;
    ArrayList<String> xVals = new ArrayList<String>();
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
         lineChart = (LineChart) findViewById(R.id.chart);
        toolbar = (Toolbar) findViewById(R.id.toolbar_linechart);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Line Chart");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sp =getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        lineChart.setViewPortOffsets(60, 0, 50, 60);
        lineChart.setTouchEnabled(false);
         xAxis.getSpaceBetweenLabels();
        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        xVals = new ArrayList<String>();
        addData();
    }
    private void addData(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.DASHBOARD_URL+getClientId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Logger.getInstance().Log("in response");


                try {
                    JSONObject jObj = new JSONObject(response);
                    {

                        JSONArray jsonArray=jObj.getJSONArray("ScoreBoard");

                        ArrayList<Entry> yvalues = new ArrayList<Entry>();

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int Value = jsonObject.getInt("Value");
                            String No_Of_Orders = jsonObject.getString("No_Of_Orders");
                            yvalues.add(new Entry(Value, i));

                            xVals.add(No_Of_Orders);


                            Logger.getInstance().Log("in yvalues"+yvalues);
                            Logger.getInstance().Log("in xvalues"+xVals);
                        }



                        LineDataSet dataset = new LineDataSet(yvalues, "orders");




                        LineData data = new LineData(xVals, dataset);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
                        dataset.setDrawCubic(true);
                        dataset.setDrawFilled(true);

                        lineChart.setData(data);
                        lineChart.animateY(5000);
                        data.setValueFormatter(new MyValueFormatter());


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },                         new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    }




