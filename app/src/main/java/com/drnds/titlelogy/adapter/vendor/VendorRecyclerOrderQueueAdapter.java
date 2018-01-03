package com.drnds.titlelogy.adapter.vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.orderqueueactivity.EditOrderVendorActivity;
import com.drnds.titlelogy.model.vendor.VendorOrderQueue;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorRecyclerOrderQueueAdapter extends RecyclerView.Adapter<VendorRecyclerOrderQueueAdapter.MyViewHolder>implements Filterable {

    private List<VendorOrderQueue> vendorList;
    private List<VendorOrderQueue> mFilteredList;
    private Activity activity;
    private Context ctx;
    public static final String VENDORORDER = "Vendor";
    private int lastPosition = -1;

    public SharedPreferences sp;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient, orderno, status, producttype, borrowername, statename, countyname, propertyaddress,date;
        private List<VendorOrderQueue> vendorList = new ArrayList<VendorOrderQueue>();
        Context ctx;
        private  ItemClickListener itemClickListener;
        public MyViewHolder(View view, Context ctx, List<VendorOrderQueue> vendorList) {
            super(view);
            this.vendorList = vendorList;
//            this.ctx=ctx;
            date = (TextView) view.findViewById(R.id.text_vdate);
            subclient = (TextView) view.findViewById(R.id.vendor_text_subclient);
            orderno = (TextView) view.findViewById(R.id.vendor_text_orderno);
            status = (TextView) view.findViewById(R.id.vendor_status);
            producttype = (TextView) view.findViewById(R.id.vendor_producttype);
            borrowername = (TextView) view.findViewById(R.id.vendor_borrowername);
            statename = (TextView) view.findViewById(R.id.vendor_statename);
            countyname = (TextView) view.findViewById(R.id.vendor_countyname);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_property_address);


            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            orderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            statename.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            countyname.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            borrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
    }

    public VendorRecyclerOrderQueueAdapter(ArrayList<VendorOrderQueue> vendorList,Context context) {
        this.vendorList = vendorList;
        this.mFilteredList = vendorList;
        this.ctx = context;
    }

    @Override
    public VendorRecyclerOrderQueueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_orderqueue_row, parent, false);

        return new VendorRecyclerOrderQueueAdapter.MyViewHolder(itemView, ctx, vendorList);
    }

    @Override
    public void onBindViewHolder(VendorRecyclerOrderQueueAdapter.MyViewHolder holder, int position) {
        VendorOrderQueue vendor = vendorList.get(position);
        holder.date.setText(vendorList.get(position).getDate());
        holder.subclient.setText(mFilteredList.get(position).getSubclient());
        holder.orderno.setText(mFilteredList.get(position).getOderno());
        holder.status.setText(mFilteredList.get(position).getStatus());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.borrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.statename.setText(mFilteredList.get(position).getState());
        holder.countyname.setText(mFilteredList.get(position).getCounty());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());
        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                VendorOrderQueue vendororderQueue = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent = new Intent(context, EditOrderVendorActivity.class);
                sp = context.getApplicationContext().getSharedPreferences(
                        VENDORORDER, 0);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Order_Id", vendororderQueue.getOrder_Id());
                editor.putString("Sub_Client_Name", vendororderQueue.getSubclient());
                editor.putString("State_Name", vendororderQueue.getState());
                editor.putString("County_Name", vendororderQueue.getCounty());
                editor.putString("State", vendororderQueue.getStateID());
                editor.putString("Order_Type", vendororderQueue.getProducttype());
                editor.putString("Order_Status", vendororderQueue.getOrdertask());
                editor.putString("Order_Priority", vendororderQueue.getOrderPriority());
                editor.putString("Order_Assign_Type", vendororderQueue.getCountytype());
                editor.putString("Progress_Status", vendororderQueue.getStatus());
                editor.putString("Sub_Client_Id", vendororderQueue.getSubId());
                editor.putString("Order_Number", vendororderQueue.getOderno());
                editor.putString("Clinet_Id", vendororderQueue.getClintId());
                editor.commit();
                Logger.getInstance().Log("gfdfg"+vendororderQueue.getSubclient());
                ((Activity) ctx).startActivityForResult(intent,1005);
//                context.startActivity(intent);
            }
        });
    }

    //    for animation


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = vendorList;
                } else {

                    ArrayList<VendorOrderQueue> filteredList = new ArrayList<>();

                    for (VendorOrderQueue vendororderQueue : vendorList) {

                        if (vendororderQueue.getSubclient().toLowerCase().contains(charString) ||
                                vendororderQueue.getOderno().toLowerCase().contains(charString) ||
                                vendororderQueue.getProducttype().toLowerCase().contains(charString) ||
                                vendororderQueue.getPropertyaddress().toLowerCase().contains(charString) ||
                                vendororderQueue.getState().toLowerCase().contains(charString) ||
                                vendororderQueue.getCounty().toLowerCase().contains(charString) ||
                                vendororderQueue.getBarrowername().toLowerCase().contains(charString) ||
                                vendororderQueue.getStatus().toLowerCase().contains(charString)) {

                            filteredList.add(vendororderQueue);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<VendorOrderQueue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }
}