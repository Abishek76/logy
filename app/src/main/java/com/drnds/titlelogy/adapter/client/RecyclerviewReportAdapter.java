package com.drnds.titlelogy.adapter.client;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.client.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ajithkumar on 8/23/2017.
 */

public class RecyclerviewReportAdapter extends RecyclerView.Adapter<RecyclerviewReportAdapter.MyViewHolder> {
private List<Report>reportList;
private Activity activity;
private Context ctx;
    private int lastPosition = -1;
public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
    private List<Report> reportList=new ArrayList<Report>();
    Context ctx;
    public MyViewHolder(View view, Context ctx, List<Report> reportList) {
        super(view);
        this.reportList=reportList;
        this.ctx=ctx;

        subclient = (TextView) view.findViewById(R.id.report_subclient);
        oderno = (TextView) view.findViewById(R.id.report_orderno);
        producttype = (TextView) view.findViewById(R.id.report_producttype);
        state = (TextView) view.findViewById(R.id.report_state);
        county = (TextView) view.findViewById(R.id.report_county);
        propertyaddress = (TextView) view.findViewById(R.id.report_address);
        status = (TextView) view.findViewById(R.id.report_status);
        borrowername = (TextView) view.findViewById(R.id.report_borrowername);


        subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        oderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        county.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        borrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


    }

}
    public RecyclerviewReportAdapter(List<Report>reportList) {
        this. reportList=   reportList;
        this.ctx=ctx;
    }

    @Override
    public RecyclerviewReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_row, parent, false);

        return new RecyclerviewReportAdapter.MyViewHolder(itemView,ctx,reportList);
    }

    @Override
    public void onBindViewHolder(RecyclerviewReportAdapter.MyViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.subclient.setText(  report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getStatus());

        setAnimation(holder.itemView, position);


    }
    @Override
    public int getItemCount() {

        return  reportList.size();
    }
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ABSOLUTE, 0.3f, Animation.ABSOLUTE, 0.3f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
}
