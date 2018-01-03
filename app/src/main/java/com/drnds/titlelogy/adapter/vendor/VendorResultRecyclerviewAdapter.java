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
import com.drnds.titlelogy.model.vendor.VendorSearch;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajith on 10/25/2017.
 */
public class VendorResultRecyclerviewAdapter extends RecyclerView.Adapter<VendorResultRecyclerviewAdapter.MyViewHolder>  {
    private List<VendorSearch> searchList;
    private Activity activity;
    private Context ctx;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
        private List<VendorSearch> searchList=new ArrayList<VendorSearch>();
        Context ctx;
        public MyViewHolder(View view, Context ctx, List<VendorSearch> searchList) {
            super(view);
            this.searchList=searchList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.vendor_result_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_result_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_result_producttype);
            state = (TextView) view.findViewById(R.id.vendor_result_state);
            county = (TextView) view.findViewById(R.id.vendor_result_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_result_address);
            status = (TextView) view.findViewById(R.id.vendor_result_status);
            borrowername = (TextView) view.findViewById(R.id.vendor_result_borrowername);


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
    public VendorResultRecyclerviewAdapter(List<VendorSearch>searchList) {
        this. searchList=   searchList;
        this.ctx=ctx;
    }

    @Override
    public VendorResultRecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_search_row, parent, false);

        return new VendorResultRecyclerviewAdapter.MyViewHolder(itemView,ctx,searchList);
    }

    @Override
    public void onBindViewHolder(VendorResultRecyclerviewAdapter.MyViewHolder holder, int position) {
        VendorSearch report = searchList.get(position);

        holder.subclient.setText(  report .getSubclient());
        Logger.getInstance().Log("2323232"+report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getBarrowername());



    }
    @Override
    public int getItemCount() {

        return  searchList.size();
    }

}

