package com.drnds.titlelogy.adapter.vendor;

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
import com.drnds.titlelogy.adapter.client.RecyclerGridUploadAdapter;
import com.drnds.titlelogy.model.client.GridUpload;
import com.drnds.titlelogy.model.vendor.VendorGridUpload;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Ajith on 11/6/2017.
 */

public class VendorRecyclerGridUploadAdapter extends RecyclerView.Adapter< VendorRecyclerGridUploadAdapter.MyViewHolder>  {
    private List<VendorGridUpload> uploadList;

    private Context ctx;
    private DownloadManager downloadManager;
    private long File_DownloadId;
    private ProgressDialog pDialog;

    String url;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView documentytype,uploadeddate,description;
        private ImageView download;

        private List<VendorGridUpload> uploadList=new ArrayList<VendorGridUpload>();
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<VendorGridUpload>  uploadList) {
            super(view);
            this.uploadList=uploadList;
            this.ctx=ctx;
            download=(ImageView)view.findViewById(R.id.vendorgrid_imgdocdownload);
            documentytype = (TextView) view.findViewById(R.id.vendorgrid_doumenttype);
            uploadeddate= (TextView) view.findViewById(R.id.vendorgrid_uploadeddate);
            description= (TextView) view.findViewById(R.id.vendorgrid_description);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    VendorGridUpload gridUpload=uploadList.get(position);
                    Context context = v.getContext();
                    url=gridUpload.getDoumentpath();
                    Uri file_uri = Uri.parse(url);




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
    public VendorRecyclerGridUploadAdapter(List<VendorGridUpload>gridUploadList,Context context) {
        this. uploadList=   gridUploadList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;
    }

    @Override
    public VendorRecyclerGridUploadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendorupload_gridrow, parent, false);

        return new VendorRecyclerGridUploadAdapter.MyViewHolder(itemView,ctx,uploadList);
    }



    @Override
    public void onBindViewHolder(VendorRecyclerGridUploadAdapter.MyViewHolder holder, int position) {
        VendorGridUpload gridUpload = uploadList.get(position);
        holder.documentytype.setText(  gridUpload .getDocumentType());
        holder.uploadeddate.setText( gridUpload .getUploadedDate());
        holder.description.setText( gridUpload .getDescription());





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
