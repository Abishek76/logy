package com.drnds.titlelogy.activity.client;

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
import com.drnds.titlelogy.fragments.client.AddOrderFragment;
import com.drnds.titlelogy.fragments.client.ViewVendorFragment;
import com.drnds.titlelogy.fragments.client.createsubclientfrag.CreateSubclientFragment;
import com.drnds.titlelogy.fragments.client.createuserfrag.CreateUserFragment;
import com.drnds.titlelogy.fragments.client.DashboardFragment;
import com.drnds.titlelogy.fragments.client.ExportOrderFragment;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.OrderQueueFragment;
import com.drnds.titlelogy.fragments.client.report.ReportFragment;
import com.drnds.titlelogy.fragments.client.scoreboardfragment.ScoreBoardFragment;

import com.drnds.titlelogy.util.Logger;
import com.drnds.titlelogy.util.SessionManager;

import java.util.HashMap;


public class NavigationActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    String fragmentCalled = "";

    private Toolbar toolbar;


    // urls to load navigation header background image
    // and profile image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments


    private static final String TAG_ADDNEW = "addneworder";
    private static final String TAG_ORDERQUEUE = "orderqueue";
    private static final String TAG_SCOREBOARD = "scoreboard";
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_SUBCLIENT = "subclient";
    private static final String TAG_USER = "user";
    private static final String TAG_Vendor = "vendor";
    private static final String TAG_Report = "report";

    public static String CURRENT_TAG = TAG_SCOREBOARD;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private Boolean exit = false;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    SharedPreferences sp;
    SessionManager session;
    CreateSubclientFragment createSubclientFragment;
    CreateUserFragment createUserFragment;
    OrderQueueFragment orderQueueFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation);

        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        sp = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navigationtext);
        String Email = sp.getString("Email","");
        String Client_User_Id = sp.getString("Client_User_Id","");
        nav_user.setText(Email);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_EMAIL);

        // email
        String email = user.get(SessionManager.KEY_PASSWORD);
        // Navigation view header


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data



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

                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
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

                AddOrderFragment addOrderFragment = new AddOrderFragment();
                return addOrderFragment;

            case 1:
                // order fragment
                orderQueueFragment = new OrderQueueFragment();
                return  orderQueueFragment ;
            case 2:
                // process
                ScoreBoardFragment scoreBoardFragment = new ScoreBoardFragment();
                return scoreBoardFragment;
            case 3:

                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 4:
                // settings fragment
                createSubclientFragment = new CreateSubclientFragment();
                return createSubclientFragment;
            case 5:
                // settings fragment
                createUserFragment= new  CreateUserFragment();
                return createUserFragment;
            case 6:
                // settings fragment
                ViewVendorFragment viewVendorFragment = new ViewVendorFragment();
                return viewVendorFragment;
            case 7:
                // settings fragment
                ReportFragment reportFragment = new ReportFragment();
                return reportFragment;



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


                    case R.id.nav_addorder:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_ADDNEW;
                        break;
                    case R.id.nav_orderqueue:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ORDERQUEUE;
                        break;
                    case R.id.nav_scoreboard:
                        navItemIndex =2;
                        CURRENT_TAG = TAG_SCOREBOARD;
                        break;

                    case R.id.nav_dashboard:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;





                    case R.id.nav_subclient:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SUBCLIENT;
                        break;
                    case R.id.nav_user:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_USER;
                        break;
                    case R.id.nav_vendor:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_Vendor;
                        break;

                    case R.id.nav_report:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_Report;
                        break;
                    default:
                        navItemIndex = 2;
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
        AlertDialog dlg = new AlertDialog.Builder(NavigationActivity.this,R.style.MyAlertDialogStyle).create();
        dlg.setCancelable(false);
        dlg.setTitle("CLOSE");
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

        getMenuInflater().inflate(R.menu.logout, menu);

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
            int position = data.getIntExtra("position",0);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1001 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof CreateSubclientFragment) {
                    createSubclientFragment.refreshData();
                }
            }
            if(requestCode == 1003 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof CreateSubclientFragment) {
                    createSubclientFragment.refreshData();
                }
            }
        }

        if(data != null)
        {
            int position = data.getIntExtra("position",1);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1002 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof  CreateUserFragment) {
                    createUserFragment.refreshClient();
                }
            }
            if(requestCode == 1004 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof  CreateUserFragment) {
                    createUserFragment.refreshClient();
                }
            }

        }
        if(data != null)
        {
            int position = data.getIntExtra("position",3);
            String refresh = data.getStringExtra("refresh");

            Logger.getInstance().Log("position : "+position);
            Logger.getInstance().Log("refresh : "+refresh);

            if(requestCode == 1111 && refresh.equals("yes"))
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
                if (fragment instanceof  OrderQueueFragment) {
                    orderQueueFragment.refreshOrder();
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



            case R.id.nav_myaccount:
                Intent intent=new Intent(NavigationActivity.this,MyaccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                logout();

                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void logout() {
//
//        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
        session.logoutUser();
        finish();

    }


}




