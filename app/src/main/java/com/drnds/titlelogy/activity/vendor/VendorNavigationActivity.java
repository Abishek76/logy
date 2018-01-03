package com.drnds.titlelogy.activity.vendor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.fragments.client.scoreboardfragment.ScoreBoardFragment;
import com.drnds.titlelogy.fragments.vendor.DashboardFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.scoreboardfragmentvendor.ScoreBoardFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.orderquefragment.VendorOrderQueueFragment;
import com.drnds.titlelogy.fragments.vendor.VendorReportFragment;
import com.drnds.titlelogy.fragments.vendor.ViewClientFragmentVendor;
import com.drnds.titlelogy.fragments.vendor.ViewSubclientFragmentVendor;
import com.drnds.titlelogy.util.Logger;
import com.drnds.titlelogy.util.SessionManager;
import com.drnds.titlelogy.util.SessionVendor;

import java.util.HashMap;

public class VendorNavigationActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    SessionVendor session;
    ViewSubclientFragmentVendor viewSubclientFragmentVendor;
    ViewClientFragmentVendor viewClientFragmentVendor;
    VendorOrderQueueFragment vendorOrderQueueFragment;

    private Toolbar toolbar;


    // urls to load navigation header background image
    // and profile image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_SCOREBOARD = "scoreboard";
    private static final String TAG_ORDERQUEUE = "orderqueue";
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_SUBCLIENT = "subclient";
    private static final String TAG_CLIENT = "client";
    private static final String TAG_REPORT = "report";

    public static String CURRENT_TAG = TAG_SCOREBOARD;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Boolean exit = false;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_vendor);

        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_vendor);
        navigationView = (NavigationView) findViewById(R.id.nav_view_vendor);
        sp = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navigationtext_vendor);
        String Email = sp.getString("Email","");

        nav_user.setText(Email);

        // Navigation view header


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_vendortitles);

        // load nav menu header data


        session = new SessionVendor(getApplicationContext());
        session.checkvendorLogin();
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.KEY_EMAIL);

        // email
        String email = user.get(SessionManager.KEY_PASSWORD);
        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SCOREBOARD;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.frame_vendor, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // process
                ScoreBoardFragmentVendor scoreBoardFragmentVendor = new ScoreBoardFragmentVendor();
                return scoreBoardFragmentVendor;
            case 1:

                DashboardFragmentVendor dashboardFragmentVendor = new DashboardFragmentVendor();
                return dashboardFragmentVendor;
            case 2:
                // order fragment
                vendorOrderQueueFragment = new VendorOrderQueueFragment();
                return  vendorOrderQueueFragment ;

            case 3:
                // settings fragment
                 viewSubclientFragmentVendor = new ViewSubclientFragmentVendor();
                return viewSubclientFragmentVendor;
            case 4:
                // settings fragment
                viewClientFragmentVendor= new ViewClientFragmentVendor();
                return viewClientFragmentVendor;
            case 5:
                // settings fragment
                VendorReportFragment vendorReportFragment = new VendorReportFragment();
                return vendorReportFragment;



            default:
                return new ScoreBoardFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_scoreboard_vendor:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_SCOREBOARD;
                        break;

                    case R.id.nav_dashboard_vendor:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.nav_orderqueue_vendor:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ORDERQUEUE;
                        break;
                    case R.id.nav_subclient_vendor:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SUBCLIENT;
                        break;
                    case R.id.nav_user_vendor:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_CLIENT;
                        break;
                    case R.id.nav_report_vendor:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_REPORT;
                        break;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_SCOREBOARD;
                loadHomeFragment();
                return;
            }

        }
        AlertDialog dlg = new AlertDialog.Builder(VendorNavigationActivity.this, R.style.MyAlertDialogStyle).create();
        dlg.setCancelable(false);
        dlg.setMessage("Are you sure want to close?");
        dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dlg.setButton(DialogInterface.BUTTON_POSITIVE, "CLOSE APP", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();


            }
        });

        dlg.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected

        getMenuInflater().inflate(R.menu.logout_vendor, menu);

        return true;
        // when fragment is notifications, load the menu created for notifications


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Logger.getInstance().Log("requestCode : "+requestCode);
        Logger.getInstance().Log("resultCode : "+resultCode);


        if(data != null)
        {
            int position = data.getIntExtra("position",3);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1001 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof ViewSubclientFragmentVendor) {
                    viewSubclientFragmentVendor.refreshSublient();
                }
            }

        }

        if(data != null)
        {
            int position = data.getIntExtra("position",4);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1009 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof ViewClientFragmentVendor) {
                    viewClientFragmentVendor.refreshClient();
                }
            }

        }
        if(data != null)
        {
            int position = data.getIntExtra("position",2);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1005 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof VendorOrderQueueFragment) {
                    vendorOrderQueueFragment.refreshOrder();

                }
            }


        }

    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Fragment fragment = null;
        String title = "";

        switch (item.getItemId()){
            case R.id.nav_myaccount_vendor:
                Intent intent=new Intent(VendorNavigationActivity.this,MyaccountVendorActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_logout_vendor:
                logout();
                return true;


            default:
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void logout() {

        session.logoutUser();
        finish();
    }



}


