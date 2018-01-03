package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.vendor.VendorReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajith on 9/25/2017.
 */

public class VendorRecyclerViewReportAdapter extends RecyclerView.Adapter<VendorRecyclerViewReportAdapter.MyViewHolder> {
    private List<VendorReport> reportList;
    private Activity activity;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
        private List<VendorReport> reportList=new ArrayList<VendorReport>();
        Context ctx;
        public MyViewHolder(View view, Context ctx, List<VendorReport> reportList) {
            super(view);
            this.reportList=reportList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.vendor_report_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_report_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_report_producttype);
            state = (TextView) view.findViewById(R.id.vendor_report_state);
            county = (TextView) view.findViewById(R.id.vendor_report_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_report_address);
            status = (TextView) view.findViewById(R.id.vendor_report_status);
            borrowername= (TextView) view.findViewById(R.id.vendor_reportborrowername);


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
    public VendorRecyclerViewReportAdapter(List<VendorReport>reportList) {
        this. reportList=   reportList;
        this.ctx=ctx;
    }

    @Override
    public VendorRecyclerViewReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_report_row, parent, false);

        return new VendorRecyclerViewReportAdapter.MyViewHolder(itemView,ctx,reportList);
    }

    @Override
    public void onBindViewHolder(VendorRecyclerViewReportAdapter.MyViewHolder holder, int position) {
        VendorReport report = reportList.get(position);
        holder.subclient.setText(  report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getBorrowername());




    }
    @Override
    public int getItemCount() {

        return  reportList.size();
    }
}
