package com.drnds.titlelogy.activity.vendor;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.chartactivity.PiechartActivity;
import com.drnds.titlelogy.fragments.client.MyValueFormatter;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VendorPiechartActivity extends AppCompatActivity {
    TextView textView1,textView2,textView3,textView4,textView5,
            textView6,textView7,textView8,textView9,textView10;
    private RelativeLayout mainLayout;
    private PieChart mChart;
    SharedPreferences sp;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,
            imageView6,imageView7,imageView8,imageView9,imageView10;
    //    private PieChart chartContainer;
    private FrameLayout chartContainer;
    //we are going to display the pie chart for smartphone market shares
    private Toolbar toolbar;

    ArrayList<String> xVals = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_piechart);
        toolbar = (Toolbar) findViewById(R.id.vendor_toolbar_piechart);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Pie Chart");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        sp =getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        mChart = new PieChart(this);
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.animateXY(2000,2000);
        textView1=(TextView)findViewById(R.id.text1);
        textView2=(TextView)findViewById(R.id.text2);
        textView3=(TextView)findViewById(R.id.text3);
        textView4=(TextView)findViewById(R.id.text4);
        textView5=(TextView)findViewById(R.id.text5);
        textView6=(TextView)findViewById(R.id.text6);
        textView7=(TextView)findViewById(R.id.text7);
        textView8=(TextView)findViewById(R.id.text8);
        textView9=(TextView)findViewById(R.id.text9);
        textView10=(TextView)findViewById(R.id.text10);
        imageView1=(ImageView)findViewById(R.id.btn_capture_photo1) ;
        imageView2=(ImageView)findViewById(R.id.btn_capture_photo2) ;
        imageView3=(ImageView)findViewById(R.id.btn_capture_photo3) ;
        imageView4=(ImageView)findViewById(R.id.btn_capture_photo4) ;
        imageView5=(ImageView)findViewById(R.id.btn_capture_photo5) ;
        imageView6=(ImageView)findViewById(R.id.btn_capture_photo6) ;
        imageView7=(ImageView)findViewById(R.id.btn_capture_photo7) ;
        imageView8=(ImageView)findViewById(R.id.btn_capture_photo8) ;
        imageView9=(ImageView)findViewById(R.id.btn_capture_photo9) ;
        imageView10=(ImageView)findViewById(R.id.btn_capture_photo10) ;


        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);




        //mainLayout.setBackgroundColor(Color.LTGRAY);
        //  mainLayout.setBackgroundColor(Color.parseColor("#55656C"));

        xVals = new ArrayList<String>();


        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(60);
        mChart.setCenterText("Orders");
        mChart.setCenterTextColor(Color.RED);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleRadius(5);
        mChart.setBackgroundColor(Color.WHITE);

        // enable rotation of the chart by touch
        mChart.setRotation(0);
        mChart.setRotationEnabled(true);

        //set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //display message when value selected
                if (e == null)
                    return;

                Toast.makeText(VendorPiechartActivity.this, xVals.toArray()[e.getXIndex()]+" = "+e.getVal()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // add Data
        addData();
        //addDataa();


        //customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        l.setTextColor(Color.BLUE);
        l.setYEntrySpace(20f);
        l.setFormSize(10f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setEnabled(false);

    }

    private void addData(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.VENDORDASHBOARD_URL+getVendorId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Logger.getInstance().Log("in response");


                try {
                    JSONObject jObj = new JSONObject(response);
                    {

                        JSONArray jsonArray=jObj.getJSONArray("ScoreBoard");

                        ArrayList<Entry> yvalues = new ArrayList<Entry>();
                        ArrayList Y= new ArrayList();
                        int Value=0;
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Value = jsonObject.getInt("Value");

                            String No_Of_Orders = jsonObject.getString("No_Of_Orders");
                            yvalues.add(new Entry(Value, i));

                            xVals.add(No_Of_Orders);
                            Y.add(Value);



                        }
                        textView1.setText(Y.get(9)+"");
                        textView2.setText(Y.get(8)+"");
                        textView3.setText(Y.get(7)+"");
                        textView4.setText(Y.get(6)+"");
                        textView5.setText(Y.get(5)+"");
                        textView6.setText(Y.get(4)+"");
                        textView7.setText(Y.get(3)+"");
                        textView8.setText(Y.get(2)+"");
                        textView9.setText(Y.get(1)+"");
                        textView10.setText(Y.get(0)+"");
                        Logger.getInstance().Log("valueof"+Y.get(0));
                        Logger.getInstance().Log("valueof"+Y.get(1));
                        Logger.getInstance().Log("valueof"+Y.get(2));
                        Logger.getInstance().Log("valueof"+Y.get(3));
                        Logger.getInstance().Log("valueof"+Y.get(4));
                        Logger.getInstance().Log("valueof"+Y.get(5));
                        Logger.getInstance().Log("valueof"+Y.get(6));
                        Logger.getInstance().Log("valueof"+Y.get(7));
                        Logger.getInstance().Log("valueof"+Y.get(8));
                        Logger.getInstance().Log("valueof"+Y.get(9));
                        PieDataSet dataSet = new PieDataSet(yvalues, "");
                        dataSet.setSelectionShift(5);
//        dataSet.setSliceSpace(2);
                        dataSet.setDrawValues(false);



                        // add many colors
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

                        colors.add(ColorTemplate.getHoloBlue());
                        dataSet.setColors(colors);

                        imageView1.setBackgroundColor(colors.get(9));
                        imageView2.setBackgroundColor(colors.get(8));
                        imageView3.setBackgroundColor(colors.get(7));
                        imageView4.setBackgroundColor(colors.get(6));
                        imageView5.setBackgroundColor(colors.get(5));
                        imageView6.setBackgroundColor(colors.get(4));
                        imageView7.setBackgroundColor(colors.get(3));
                        imageView8.setBackgroundColor(colors.get(2));
                        imageView9.setBackgroundColor(colors.get(1));
                        imageView10.setBackgroundColor(colors.get(0));
                        //instantiate pie data object now
                        PieData data = new PieData(xVals, dataSet);
                        data.setValueFormatter(new MyValueFormatter());
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.BLACK);
//        data.setValueFormatter(new PercentFormatter());
                        dataSet.setDrawValues(false);
                        mChart.setData(data);
                        mChart.setDrawSliceText(false);
                        //undo all higlights
                        mChart.highlightValues(null);

                        // update pie chart
                        mChart.invalidate();

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
    public String getVendorId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Vendor_Id", "");


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//
}
