package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.EditSubclientVendorActivity;
import com.drnds.titlelogy.model.vendor.VendorSubclient;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ajith on 9/25/2017.
 */

public class VendorSubclientAdapter extends RecyclerView.Adapter< VendorSubclientAdapter.MyViewHolder>  {
    private List<VendorSubclient> vendorSubclientList;
    private Activity activity;
    private Context ctx;
    private ImageView imgedit;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView clientname,subprocessname,subcode,state,county,adress,invoicename;
        private List<VendorSubclient> vendorSubclientList=new ArrayList<VendorSubclient>();
        Context ctx;
        public MyViewHolder(View view, final Context ctx, final List<VendorSubclient> vendorSubclientList) {
            super(view);
            this.vendorSubclientList=vendorSubclientList;
            this.ctx=ctx;

            clientname = (TextView) view.findViewById(R.id.clientnamevendor);
            subprocessname= (TextView) view.findViewById(R.id.subprocess_name_vendor);
            subcode= (TextView) view.findViewById(R.id.subprocess_code_vendor);
            state= (TextView) view.findViewById(R.id.state_vendor);
            county= (TextView) view.findViewById(R.id.county_vendor);
            adress= (TextView) view.findViewById(R.id.address_vendor);
            invoicename= (TextView) view.findViewById(R.id.invoice_contact_name_vendor);

            imgedit= (ImageView) view.findViewById(R.id.imageView_subedit_vendor);
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    VendorSubclient vendorSubclient=vendorSubclientList.get(position);
                    Context context = v.getContext();


                    Intent intent=new Intent(context, EditSubclientVendorActivity.class);
                    intent.putExtra("Sub_Process_Code",vendorSubclient.getSubprocesscode());
                    intent.putExtra("Invoice_Contact_Name",vendorSubclient.getInvoicename());
                    intent.putExtra("Address",vendorSubclient.getAddress());
                    intent.putExtra("Subclientvendor",vendorSubclient.getSubId());
                    Logger.getInstance().Log("Subclientvendor"+vendorSubclient.getSubId());
                    ((Activity) ctx).startActivityForResult(intent,1001);
//                    context.startActivity(intent);
                }
            });


        }

    }
    public VendorSubclientAdapter(List<VendorSubclient>vendorSubclientList,Context context) {
        this. vendorSubclientList=   vendorSubclientList;
        this.ctx=context;
    }

    @Override
    public VendorSubclientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_subclients_row, parent, false);
        ctx = parent.getContext();

        return new VendorSubclientAdapter.MyViewHolder(itemView,ctx,vendorSubclientList);
    }

    @Override
    public void onBindViewHolder(VendorSubclientAdapter.MyViewHolder holder, int position) {
        VendorSubclient vendor = vendorSubclientList.get(position);
        holder.clientname.setText(  vendor .getClientname());
        holder.subprocessname.setText(  vendor .getSubprocessname());
        holder.subcode.setText(  vendor .getSubprocesscode());
        holder.state.setText(  vendor .getState());
        holder.county.setText(  vendor .getCounty());
        holder.adress.setText(  vendor .getAddress());
        holder.invoicename.setText(  vendor .getInvoicename());

//        Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
//        holder.itemView.startAnimation(animation);



    }

    //    for animation

    @Override
    public int getItemCount() {

        return  vendorSubclientList.size();
    }
}


