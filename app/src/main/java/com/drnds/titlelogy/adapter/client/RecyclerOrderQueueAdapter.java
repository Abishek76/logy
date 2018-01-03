package com.drnds.titlelogy.adapter.client;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.EditOrderInfoFragment;
import com.drnds.titlelogy.fragments.client.orderqueuefragment.OrderQueueFragment;

import com.drnds.titlelogy.model.client.OrderQueue;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ajithkumar on 6/28/2017.
 */
public class RecyclerOrderQueueAdapter extends RecyclerView.Adapter<RecyclerOrderQueueAdapter.MyViewHolder>implements Filterable {

    private ArrayList<OrderQueue> orderQueueList;
    private ArrayList<OrderQueue> mFilteredList;
    private Context ctx;
    private Activity activity;
    SharedPreferences sp;
    public static final String ORDERQUEUE = "orderadpter";


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status,barrowername,date;
        private List<OrderQueue> orderQueueList=new ArrayList<OrderQueue>();
      private  ItemClickListener itemClickListener;
        public MyViewHolder(View view,Context ctx,List<OrderQueue> orderQueueList) {
            super(view);

            this.orderQueueList=orderQueueList;
            subclient = (TextView) view.findViewById(R.id.text_subclient);
            oderno = (TextView) view.findViewById(R.id.text_orderno);
            producttype = (TextView) view.findViewById(R.id.producttype);
            state = (TextView) view.findViewById(R.id.statename);
            county = (TextView) view.findViewById(R.id.countyname);
            propertyaddress = (TextView) view.findViewById(R.id.property_address);
            status = (TextView) view.findViewById(R.id.status);
            barrowername = (TextView) view.findViewById(R.id.borrowername);
            date=(TextView) view.findViewById(R.id.text_date);

            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            oderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            county.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            barrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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


    public RecyclerOrderQueueAdapter(ArrayList<OrderQueue>orderQueueList) {
        this. orderQueueList=   orderQueueList;
        this. mFilteredList=   orderQueueList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderqueue_row, parent, false);

        return new MyViewHolder(itemView,ctx,orderQueueList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.subclient.setText(mFilteredList.get(position).getSubclient());
        holder.oderno.setText(mFilteredList.get(position).getOderno());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());
        holder.date.setText(orderQueueList.get(position).getDate());
        holder.state.setText(mFilteredList.get(position).getState());
        holder.county.setText(mFilteredList.get(position).getCounty());
        holder.barrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.status.setText(mFilteredList.get(position).getStatus());




        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                OrderQueue orderQueue = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent=new Intent(context, EditOrderActivity.class);
                sp = context.getSharedPreferences(ORDERQUEUE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id",orderQueue.getOrder_Id());
                editor.putString("Sub_Client_Name",orderQueue.getSubclient());
                editor.putString("Order_Number",orderQueue.getOderno());
                editor.putString("State",orderQueue.getState());
                editor.putString("County",orderQueue.getCounty());
                editor.putString("Order_Priority",orderQueue.getOrderPriority());
                editor.putString("Product_Type",orderQueue.getProducttype());
                editor.putString("Order_Status",orderQueue.getOrdertask());
                editor.putString("Order_Assign_Type",orderQueue.getCountytype());
                editor.putString("Progress_Status",orderQueue.getStatus());
                editor.putString("Barrower_Name",orderQueue.getStatus());
                editor.putString("Sub_Client_Id",orderQueue.getSubId());
//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());
                editor.commit();

                context.startActivity(intent);
            }
        });
    }

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

                    mFilteredList = orderQueueList;
                } else {

                    ArrayList<OrderQueue> filteredList = new ArrayList<>();

                    for (OrderQueue orderQueue : orderQueueList) {

                        if (orderQueue.getSubclient().toLowerCase().contains(charString) ||
                                orderQueue.getOderno().toLowerCase().contains(charString) ||
                                orderQueue.getProducttype().toLowerCase().contains(charString)||
                                orderQueue.getPropertyaddress().toLowerCase().contains(charString)||
                                orderQueue.getState().toLowerCase().contains(charString)||
                                orderQueue.getCounty().toLowerCase().contains(charString)||

                                orderQueue.getBarrowername().toLowerCase().contains(charString)||
                                orderQueue.getStatus().toLowerCase().contains(charString))
                        {

                            filteredList.add(orderQueue);
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
                mFilteredList = (ArrayList<OrderQueue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }

}