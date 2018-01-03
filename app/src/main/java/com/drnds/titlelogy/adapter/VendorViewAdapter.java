package com.drnds.titlelogy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.client.Vendor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajithkumar on 6/22/2017.
 */

public class VendorViewAdapter extends RecyclerView.Adapter<VendorViewAdapter.MyViewHolder>  {
    private List<Vendor> vendorList;
    private Activity activity;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vname,vstate,vemail,vcounty,vphone ;
        private List<Vendor> vendorList=new ArrayList<Vendor>();
        Context ctx;
        public MyViewHolder(View view,Context ctx,List<Vendor> vendorList) {
            super(view);
            this.vendorList=vendorList;
            this.ctx=ctx;

            vname = (TextView) view.findViewById(R.id.vendorname);
            vstate= (TextView) view.findViewById(R.id.vstate);
            vemail= (TextView) view.findViewById(R.id.vendoremail);
            vcounty= (TextView) view.findViewById(R.id.vcounty);
            vphone= (TextView) view.findViewById(R.id.vphoneno);


        }

    }
    public VendorViewAdapter(List<Vendor>vendorList) {
        this. vendorList=   vendorList;
        this.ctx=ctx;
    }

    @Override
    public VendorViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_row, parent, false);

        return new VendorViewAdapter.MyViewHolder(itemView,ctx,vendorList);
    }

    @Override
    public void onBindViewHolder(VendorViewAdapter.MyViewHolder holder, int position) {
        Vendor vendor = vendorList.get(position);
        holder.vname.setText(  vendor .getVenname());
        holder.vstate.setText(  vendor .getState());
        holder.vemail.setText(  vendor .getVemail());
        holder.vcounty.setText(  vendor .getCounty());
        holder.vphone.setText(  vendor .getPhoneno());





    }
    @Override
    public int getItemCount() {

        return  vendorList.size();
    }
}

