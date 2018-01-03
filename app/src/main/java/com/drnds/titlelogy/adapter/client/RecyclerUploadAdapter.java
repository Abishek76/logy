package com.drnds.titlelogy.adapter.client;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.drnds.titlelogy.R;

import com.drnds.titlelogy.activity.client.orderqueueactivity.PdfActivity;
import com.drnds.titlelogy.model.client.Upload;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;



/**
 * Created by Ajithkumar on 8/4/2017.
 */

public class RecyclerUploadAdapter extends RecyclerView.Adapter< RecyclerUploadAdapter.MyViewHolder>  {
    public ArrayList<Upload> uploadList;
    public ArrayList<Upload> selected_usersList=new ArrayList<>();
    private Activity activity;
    private Context ctx;
    private DownloadManager downloadManager;
    private long File_DownloadId;
    private ProgressDialog pDialog;
    RecyclerUploadAdapter madapter;
    String url;
    String uploadUri = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private List<Upload> uploadList=new ArrayList<Upload>();
        private TextView document,date;
        private ImageView download;
        public LinearLayout ll_listitem;
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<Upload>  uploadList) {
            super(view);
            this.uploadList = uploadList;
            this.ctx = ctx;
            document=(TextView)view.findViewById(R.id.doumenttype);
            date=(TextView)view.findViewById(R.id.uploadeddate);
            download=(ImageView) view.findViewById(R.id.img_docdownload);
            ll_listitem=(LinearLayout)view.findViewById(R.id.ll_listitem);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    Upload upload=uploadList.get(position);
                    Context context = view.getContext();
                    Intent intent=new Intent(context, PdfActivity.class);

                    intent.putExtra("Pdfpath",upload.getDoumentpath());

                    context.startActivity(intent);
                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    Upload upload=uploadList.get(position);
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



    public RecyclerUploadAdapter(ArrayList<Upload>uploadList,ArrayList<Upload> selectedList,Context context) {
        this. uploadList=   uploadList;
        this.selected_usersList = selectedList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;
    }

    @Override
    public RecyclerUploadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upload_row, parent, false);

        return new RecyclerUploadAdapter.MyViewHolder(itemView,ctx,uploadList);
    }

    @Override
    public void onBindViewHolder(RecyclerUploadAdapter.MyViewHolder holder, int position) {
        Upload upload = uploadList.get(position);
        uploadUri = upload.getDoumentpath();
        holder.document.setText(  upload .getDocumentType());
        holder.date.setText( upload .getUploadedDate());


        Logger.getInstance().Log("uploauri"+uploadUri);

        if(selected_usersList.contains(uploadList.get(position)))
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(ctx, R.color.list_item_selected_state));
        else
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(ctx, R.color.list_item_normal_state));



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

