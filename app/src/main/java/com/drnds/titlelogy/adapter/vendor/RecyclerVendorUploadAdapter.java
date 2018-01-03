package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.adapter.client.RecyclerUploadAdapter;
import com.drnds.titlelogy.model.client.Upload;
import com.drnds.titlelogy.model.vendor.VendorUpload;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Ajith on 11/3/2017.
 */

public class RecyclerVendorUploadAdapter extends RecyclerView.Adapter< RecyclerVendorUploadAdapter.MyViewHolder> {
    private List<VendorUpload> uploadList;
    private Activity activity;
    private Context ctx;
    private DownloadManager downloadManager;
    private long File_DownloadId;
    private ProgressDialog pDialog;
    RecyclerVendorUploadAdapter madapter;
    String url;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView documentytype,uploadeddate,description;
        private ImageView download;

        private List<VendorUpload> uploadList=new ArrayList<VendorUpload>();
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<VendorUpload>  uploadList) {
            super(view);
            this.uploadList=uploadList;
            this.ctx=ctx;
            download=(ImageView)view.findViewById(R.id.img_docdownload);
            documentytype = (TextView) view.findViewById(R.id.doumenttype);
            uploadeddate= (TextView) view.findViewById(R.id.uploadeddate);
            description= (TextView) view.findViewById(R.id.description);



            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    VendorUpload upload=uploadList.get(position);
                    Context context = v.getContext();
                    url=upload.getDoumentpath();
                    Uri file_uri = Uri.parse(url);



                    Logger.getInstance().Log("url   : " + url);
//                    File_DownloadId = DownloadData(file_uri, v);
                    downloadManager=(DownloadManager)ctx.getSystemService(ctx.DOWNLOAD_SERVICE);
//                    Uri file_uri = Uri.parse("http://maven.apache.org/archives/maven-1.x/maven.pdf");
                    DownloadManager.Request request=new DownloadManager.Request(file_uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference=downloadManager.enqueue(request);
                }
            });
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            ctx.registerReceiver(downloadReceiver, filter);

        }


    }
    public RecyclerVendorUploadAdapter(List<VendorUpload>uploadList,Context context) {
        this. uploadList=   uploadList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;
    }

    @Override
    public RecyclerVendorUploadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_uploadrow, parent, false);

        return new RecyclerVendorUploadAdapter.MyViewHolder(itemView,ctx,uploadList);
    }

    @Override
    public void onBindViewHolder(RecyclerVendorUploadAdapter.MyViewHolder holder, int position) {
        VendorUpload upload = uploadList.get(position);
        holder.documentytype.setText(  upload .getDocumentType());
        holder.uploadeddate.setText( upload .getUploadedDate());
        holder.description.setText( upload .getDescription());





    }
    @Override
    public int getItemCount() {
        return  uploadList.size();
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private long DownloadData (Uri uri, View v) {

        long downloadReference;

        downloadManager = (DownloadManager)ctx.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        if(v.getId() == R.id.img_docdownload)
            request.setDestinationInExternalFilesDir(ctx, Environment.DIRECTORY_DOWNLOADS,"Titelogy.pdf");


        //Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);



        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(referenceId == File_DownloadId) {

                Toast toast = Toast.makeText(ctx,
                        "File Download Complete", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }


        }
    };

}
