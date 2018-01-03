package com.drnds.titlelogy.fragments.client.orderqueuefragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.GridUploadActivity;
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.activity.client.orderqueueactivity.OrderUploadActivity;
import com.drnds.titlelogy.adapter.client.RecyclerUploadAdapter;
import com.drnds.titlelogy.model.client.Upload;
import com.drnds.titlelogy.util.AppController;
import com.drnds.titlelogy.util.Config;
import com.drnds.titlelogy.util.Logger;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.drnds.titlelogy.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

/**
 * Created by ajithkumar on 6/29/2017.
 */

public class EditUploadDocFragment extends Fragment implements View.OnClickListener{
    private List<Upload> uploadArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerUploadAdapter mAdapter;
    private FloatingActionButton fab;
    private ArrayList<String> document;
    private ArrayList<String> documentIds;
    private ProgressDialog pDialog;
    String value;
    AlertDialog.Builder builder;
    TextInputLayout descriptionlayout;
    private EditText inputDescript;
    Spinner spinner;
    String Client_User_Id, Order_Id, clientId, subId, docId, documents, orderNum, Descript;
    Uri audioFileUri;
    Boolean flag = false;
    TextView textView;
    SharedPreferences sp, pref;
    ImageView attach,scanner;
    private LinearLayout attachmentLayout;
    private boolean isHidden = true;
    ImageButton btnDocument,btnCamera,btnGallery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.fragment_upload, null);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyle_upload);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        Bundle bundle = getArguments();
//        Order_Id = bundle.getString("Order_Id");
//        Logger.getInstance().Log("in update order201888"+Order_Id);
//        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
//        pDialog.setCancelable(false);
//        checkInternetConnection();
//        spinner = (Spinner) view.findViewById(R.id.document_orderspinner);
//        builder = new AlertDialog.Builder(getActivity());
//        sp = getActivity().getApplicationContext().getSharedPreferences(
//                "LoginActivity", 0);
//
//        attach=(ImageView)view.findViewById(R.id.clientfile_attach);
//        attach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int cx = attachmentLayout.getRight();
//                int cy =  attachmentLayout.getBottom();
//                showMenu(attachmentLayout,cx,cy);
//                //
//            }
//        });
//        pref = getActivity().getApplicationContext().getSharedPreferences(
//                ORDERQUEUE, 0);
//        Order_Id = bundle.getString("Order_Id");
//        descriptionlayout = (TextInputLayout) view.findViewById(R.id.input_layout_description_order);
//        Client_User_Id = sp.getString("Client_User_Id", "");
//        document = new ArrayList<String>();
//        documentIds = new ArrayList<>();
//        getDocument();
//        inputDescript = (EditText) view.findViewById(R.id.input_discription);
//        Order_Id = pref.getString("Order_Id", "");
//        clientId = sp.getString("Client_Id", "");
//        subId = pref.getString("Sub_Client_Id", "");
//        orderNum = pref.getString("Order_Number", "");
//        viewDocuments();
//        attachmentLayout = (LinearLayout) view.findViewById(R.id.menu_attachments);
//         btnDocument = (ImageButton) view.findViewById(R.id.menu_attachment_document);
//        btnCamera = (ImageButton) view.findViewById(R.id.menu_attachment_camera);
//         btnGallery = (ImageButton) view.findViewById(R.id.menu_attachment_gallery);
//        btnDocument.setOnClickListener(this);
//        btnCamera.setOnClickListener(this);
//        btnGallery.setOnClickListener(this);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                docId = documentIds.get(position);
//                documents = document.get(position);
//
//                //String State_Name = states.get(position);
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//
//        });

        return view;
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
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





                        uploadArrayList.add(upload);
//                        mAdapter = new RecyclerUploadAdapter(uploadArrayList,getContext());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

                        // 0 for private mode




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
//            Snackbar snackbar = Snackbar.make(snackbarCoordinatorLayout, "Check Internet Connection..!", Snackbar.LENGTH_LONG);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            snackbar.show();
            return false;
        }
        return false;
    }






    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void refreshClient() {
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);


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
                spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, document));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void UploadDocument() {
//        attach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
        {

            checkInternetConnection();   // checking internet connection
            if (!validateDescription())
            {
                return;
            }


            else{
                new MaterialFilePicker()
                        .withActivity(getActivity())

                        .withRequestCode(10)
                        .start();

            }}
//        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            UploadDocument();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    ProgressDialog progress;
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if(requestCode == 10 && resultCode == RESULT_OK){
            Descript = inputDescript.getText().toString().trim();
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            . addFormDataPart("Order_Id",Order_Id)
                            . addFormDataPart("Client_Id",clientId)
                            . addFormDataPart("Sub_Client_Id",subId)
                            . addFormDataPart("Document_Type_Id",docId)
                            . addFormDataPart("User_Id",Client_User_Id)
                            . addFormDataPart("Order_Number",orderNum)
                            . addFormDataPart("Description",Descript)
                            . addFormDataPart("Document_From","1")



                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)

                            .build();



                    System.out.println("erqh"+request_body);

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(Config.CLIENTORDER_DOCUPLOAD)
                            .post(request_body)
                            .build();
                    System.out.println(content_type+",,"+file_path+",,"+client+",,"+file_body+",,"+request_body+",,"+request);
                    try {
                        okhttp3.Response response = client.newCall(request).execute();
                        // System.out.println("response"+response.body().string());



                        if(!response.isSuccessful()){

                            progress.dismiss();
                            throw new IOException("Error : "+response);
                        }else{
                            Intent intent=new Intent(getActivity(),EditOrderActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            flag = true;
                            progress.dismiss();
                        }





                    } catch (IOException e) {
                        progress.dismiss();
                        e.printStackTrace();
                    }


                }
            });

            t.start();


            if (flag){
                Toast.makeText(getActivity(),"some thing went wrong", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"Uploaded successfull", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void hideMenu() {
        attachmentLayout.setVisibility(View.GONE);
        isHidden = true;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void showMenu(final LinearLayout attachmentLayout,int cx,int cy) {

//        int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
//        int cy = attachmentLayout.getTop();
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
                    isHidden = true;
                }
            });
            anim.start();
        }
    }

    @Override
    public void onClick(View v) {
        hideMenu();

        switch (v.getId()) {
            case R.id.menu_attachment_document:
                UploadDocument();
                Logger.getInstance().Log("uploadedclickcase");
                break;
            case R.id.menu_attachment_camera:
                Intent intent=new   Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intent);
                break;
            case R.id.menu_attachment_gallery:
                Intent intenta = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "content://media/internal/images/media"));
                startActivity(intenta);
                break;

        }
    }

}

