package com.drnds.titlelogy.activity.client.orderqueueactivity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridUploadActivity;
import com.drnds.titlelogy.adapter.client.RecyclerUploadAdapter;
import com.drnds.titlelogy.model.client.Upload;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.CustomRequest;

import com.drnds.titlelogy.util.ImageLoaderProfile;
import com.drnds.titlelogy.util.Logger;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.R.attr.statusBarColor;
import static com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

public class OrderUploadActivity extends AppCompatActivity implements View.OnClickListener ,AlertDialogHelper.AlertDialogListener{
    private Toolbar toolbar;
    private EditText inputDescript;
    Menu context_menu;
    Spinner spinner;
    TextInputLayout descriptionlayout;
    private ArrayList<String> document;
    private ArrayList<String> documentIds;
    private ProgressDialog pDialog;

    private LinearLayout attachmentLayout;
    private boolean isHidden = true;
    private ArrayList<Upload> uploadArrayList = new ArrayList<>();
    ArrayList<Upload> multiselect_list = new ArrayList<>();

    AlertDialogHelper alertDialogHelper;
    private RecyclerView recyclerView;
    private RecyclerUploadAdapter mAdapter;
    String Client_User_Id, Order_Id, clientId, subId, docId, documents, orderNum, Descript;
    boolean isMultiSelect = false;
    Boolean flag = false;

    Boolean flagaction = false;

    SharedPreferences sp, pref;
    ImageView attach, share,priview;
    String path;
    LinearLayout linearLayout;
    private ActionMode mActionMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_upload);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        alertDialogHelper =new AlertDialogHelper(this);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycle_orderupload);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        spinner = (Spinner) findViewById(R.id.document_orderspinner);
        sp = getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pref = getApplicationContext().getSharedPreferences(
                ORDERQUEUE, 0);
        Client_User_Id = sp.getString("Client_User_Id", "");




//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBodyText = path;
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
//            }
//        });

//        linearLayout = (LinearLayout) findViewById(R.id.spinnerlay);

        Order_Id = pref.getString("Order_Id", "");
        clientId = sp.getString("Client_Id", "");
        subId = pref.getString("Sub_Client_Id", "");
        orderNum = pref.getString("Order_Number", "");
//        attach=(ImageView)findViewById(R.id.file_attach);
//        scanner=(ImageView)findViewById(R.id.cam_scanner);
//        scanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
//                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.intsig.camscanner&hl=en"));
//                startActivity(i);
//            }
//        });
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
//                return;
//            }
//        }

        attach = (ImageView) findViewById(R.id.upload_attach);
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                linearLayout.setVisibility(view.INVISIBLE);
                showMenu();
                //
            }
        });


        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        showDialog();
        document = new ArrayList<String>();
        documentIds = new ArrayList<>();

        getDocument();
        checkInternetConnection();


        attachmentLayout = (LinearLayout) findViewById(R.id.menu_attachments);

        ImageButton btnDocument = (ImageButton) findViewById(R.id.menu_attachment_document);
        ImageButton btnCamera = (ImageButton) findViewById(R.id.menu_attachment_camera);
        ImageButton btnGallery = (ImageButton) findViewById(R.id.menu_attachment_gallery);


        btnDocument.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);

        viewDocuments();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docId = documentIds.get(position);
                documents = document.get(position);

                //String State_Name = states.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
//                else
//                    Toast.makeText(getApplicationContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Upload>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));

    }


    @Override
    public void onClick(View v) {
        hideMenu();

        switch (v.getId()) {
            case R.id.menu_attachment_document:
                UploadDocument();
                break;
            case R.id.menu_attachment_camera:
                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intent);
                break;
            case R.id.menu_attachment_gallery:
                Intent intenta = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "content://media/internal/images/media"));
                startActivity(intenta);
                break;

        }
    }


    private void getDocument() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.DOCUMENT_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        document.add(details.getString("Document_Type"));
                        documentIds.add(details.getString("Document_Type_Id"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner.setAdapter(new ArrayAdapter<String>(OrderUploadActivity.this, android.R.layout.simple_spinner_dropdown_item, document));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private boolean validateDescription() {
        if (inputDescript.getText().toString().trim().isEmpty()) {
            descriptionlayout.setError(getString(R.string.err_msg_description));
            requestFocus(inputDescript);
            return false;
        } else {
            descriptionlayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            TastyToast.makeText(this, "Check Internet Connection", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            return false;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void UploadDocument() {


        new MaterialFilePicker()
                .withActivity(OrderUploadActivity.this)
                .withRequestCode(10)
                .start();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            UploadDocument();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    ProgressDialog progress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == 10 && resultCode == RESULT_OK) {
//            Descript = inputDescript.getText().toString().trim();
            progress = new ProgressDialog(OrderUploadActivity.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("Order_Id", Order_Id)
                            .addFormDataPart("Client_Id", clientId)
                            .addFormDataPart("Sub_Client_Id", subId)
                            .addFormDataPart("Document_Type_Id", docId)
                            .addFormDataPart("User_Id", Client_User_Id)
                            .addFormDataPart("Order_Number", orderNum)
                            .addFormDataPart("Description", "test")
                            .addFormDataPart("Document_From", "1")


                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)

                            .build();


                    System.out.println("erqh" + request_body);

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Config.CLIENTORDER_DOCUPLOAD)
                            .post(request_body)
                            .build();
                    System.out.println(content_type + ",," + file_path + ",," + client + ",," + file_body + ",," + request_body + ",," + request);
                    try {
                        okhttp3.Response response = client.newCall(request).execute();
                        // System.out.println("response"+response.body().string());


                        if (response.isSuccessful()) {

                            Intent intent = new Intent(OrderUploadActivity.this, EditOrderActivity.class);
                            startActivity(intent);
                            finish();
                            flag = true;
                            progress.dismiss();


                        } else {
                            progress.dismiss();
                            throw new IOException("Error : " + response);
                        }


                    } catch (IOException e) {
                        progress.dismiss();
                        e.printStackTrace();
                    }


                }
            });

            t.start();


            if (flag) {
                Toast.makeText(getApplicationContext(), "some thing went wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Uploaded successfull", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }




    void showMenuBelowLollipop() {
        int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
        int cy = attachmentLayout.getTop();
        int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());

        try {
            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(300);

            if (isHidden) {
                //Log.e(getClass().getSimpleName(), "showMenuBelowLollipop");
                attachmentLayout.setVisibility(View.VISIBLE);
                animator.start();
                isHidden = false;
            } else {
                SupportAnimator animatorReverse = animator.reverse();
                animatorReverse.start();
                animatorReverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        //Log.e("MainActivity", "onAnimationEnd");
                        isHidden = true;
                        attachmentLayout.setVisibility(View.INVISIBLE);
//                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel() {
                    }

                    @Override
                    public void onAnimationRepeat() {
                    }
                });
            }
        } catch (Exception e) {
            //Log.e(getClass().getSimpleName(), "try catch");
            isHidden = true;
            attachmentLayout.setVisibility(View.INVISIBLE);
//            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void showMenu() {
        int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
        int cy = attachmentLayout.getTop();
        int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());

        if (isHidden) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
            attachmentLayout.setVisibility(View.VISIBLE);
            anim.start();
            isHidden = false;
        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    attachmentLayout.setVisibility(View.INVISIBLE);
//                    linearLayout.setVisibility(View.VISIBLE);
                    isHidden = true;
                }
            });
            anim.start();
        }
    }

    private void hideMenu() {
        attachmentLayout.setVisibility(View.GONE);
        isHidden = true;
    }

    public void viewDocuments() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.VIEWDOCUMENTS_URL + Order_Id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Upload_Documents");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);


                        Upload upload = new Upload();

                        upload.setDocumentType(details.getString("Document_Type"));
                        upload.setDescription(details.getString("Description"));
                        upload.setUploadedDate(details.getString("Inserted_Date"));
                        upload.setDoumentpath(details.getString("Document_Path"));

                        path = details.getString("Document_Path");

                        Logger.getInstance().Log("checkFile" + path);


                        uploadArrayList.add(upload);
                        mAdapter = new RecyclerUploadAdapter(uploadArrayList,multiselect_list,OrderUploadActivity.this);
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();


                        //Logger.getInstance().Log("checkFile"+uploadArrayList.);
//                        mAdapter = new RecyclerUploadAdapter(uploadArrayList,getBaseContext());
//                        recyclerView.setAdapter(mAdapter);

//                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode


//                        mWebview = (WebView) findViewById(R.id.webview);
//                        mWebview.setWebViewClient(new CustomWebViewClient());
//                        WebSettings webSetting = mWebview.getSettings();
//                        webSetting.setJavaScriptEnabled(true);
//                        webSetting.setDisplayZoomControls(true);
//                        String pdf = path;
//                        mWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

//                        try{ Thread.sleep(10000); }catch(InterruptedException e){ }
                        hideDialog();

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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(uploadArrayList.get(position))){
                if  (flagaction == false) {
                    multiselect_list.remove(uploadArrayList.get(position));
                }else{
                    multiselect_list.remove(uploadArrayList.get(position));
                }
                toolbar.setVisibility(View.VISIBLE);
                //mActionMode.finish();

            }else{
                multiselect_list.add(uploadArrayList.get(position));
            }
            if (multiselect_list.size() > 0) {
                toolbar.setVisibility(View.INVISIBLE);
                mActionMode.setTitle("" + multiselect_list.size());
            }else {
                toolbar.setVisibility(View.VISIBLE);
                mActionMode.finish();
//                mActionMode.setTitle("");

                refreshAdapter();
            }
        }
    }
    public void refreshAdapter()
    {
        mAdapter.selected_usersList=multiselect_list;
        mAdapter.uploadList=uploadArrayList;
        mAdapter.notifyDataSetChanged();
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        private int statusBarColor;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:

                    alertDialogHelper.showAlertDialog("","Delete File","DELETE","CANCEL",1,false);
//                    toolbar.setVisibility(View.VISIBLE);
                    return true;
//                case R.id.action_share:
//                    flagaction = true;
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    String shareBodyText = path;
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//                    startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
//                    alertDialogHelper.showAlertDialog("","Share File","SHARE","CANCEL",1,false);
//
//                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Upload>();
            toolbar.setVisibility(View.VISIBLE);

            refreshAdapter();
        }



    };

    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiselect_list.size()>0)
            {
                for(int i=0;i<multiselect_list.size();i++)
                    uploadArrayList.remove(multiselect_list.get(i));
//
                mAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }

            }
        }
        else if(from==2)
        {
            if (mActionMode != null) {
                mActionMode.finish();
            }
//
//            SampleModel mSample = new SampleModel("Name"+user_list.size(),"Designation"+user_list.size());
//            user_list.add(mSample);
//            multiSelectAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Logger.getInstance().Log("calling");
        toolbar.setVisibility(View.VISIBLE);
    }

}
