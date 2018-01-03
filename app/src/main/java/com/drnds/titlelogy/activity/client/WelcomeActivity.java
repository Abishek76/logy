package com.drnds.titlelogy.activity.client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.VendorLoginActivity;
import com.drnds.titlelogy.activity.vendor.VendorNavigationActivity;
import com.drnds.titlelogy.activity.vendor.gridactivity.GridViewVendorActivity;
import com.drnds.titlelogy.adapter.client.SlidingImage_Adapter;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.drnds.titlelogy.util.SessionManager;
import com.drnds.titlelogy.util.SessionVendor;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.VolleyLog.TAG;
import static com.drnds.titlelogy.util.Config.Order_Filter;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SlidingImage_Adapter myViewPagerAdapter;
    private Button btnUserLogin,btnVendorLogin;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.splash_1,R.drawable.splash_2,R.drawable.splash_3,R.drawable.splash_4,R.drawable.splash_5,R.drawable.splash_6};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private SessionManager session;
    private SessionVendor sessionVendor;
    private SharedPreferences sp;
    public static final String TOKEN_NAME = "Welcomeactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity

            Intent i = new Intent(WelcomeActivity.this, NavigationActivity.class);
            startActivity(i);

            // close this activity
            finish();

        }
        sessionVendor = new SessionVendor(getApplicationContext());
        if (sessionVendor.isLoggedIn()) {
            // User is already logged in. Take him to main activity

            Intent i = new Intent(WelcomeActivity.this, VendorNavigationActivity.class);
            startActivity(i);

            // close this activity
            finish();

        }

        btnUserLogin = (Button) findViewById(R.id.button_userlogin);
        btnVendorLogin = (Button) findViewById(R.id.button_vendorlogin);
        init();
        changeStatusBarColor();

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkToken();
                launchLoginScreen();
            }
        });
        btnVendorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchVendorLoginScreen();
            }
        });

    }
    private void launchVendorLoginScreen() {
        startActivity(new Intent(WelcomeActivity.this, VendorLoginActivity.class));
        finish();
    }
    private void launchLoginScreen() {

        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }
    private void init() {
        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        viewPager = (ViewPager) findViewById(R.id.pager_welcome);


        viewPager.setAdapter(new SlidingImage_Adapter(WelcomeActivity.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
public void checkToken(){
    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Config.TOKEN_REQUEST, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.d(TAG, response.toString());
//                 hideDialog();



            JSONObject jObj = null;
            try {
                jObj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, response.toString());
            boolean  error = false;
            try {
                error = jObj.getBoolean("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Logger.getInstance().Log("in error response"+response);
            // Check for error node in json
            if (!error) {

                sp = getApplicationContext().getSharedPreferences(
                        "Welcomeactivity", 0);


                SharedPreferences.Editor editor = sp.edit();
//                editor.putString("Client_User_Id", Client_User_Id);

                editor.commit();





            }






        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d(TAG, "Error: " + error.getMessage());

        }
    })

    {
        @Override
        public String getBodyContentType() {
            return "application/x-www-form-urlencoded; charset=UTF-8";
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
//            params.put("username", etUname.getText().toString().trim());

            return params;
        }

    };



    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(stringRequest);
}

}



