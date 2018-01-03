package com.drnds.titlelogy.adapter.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.client.gridactivity.EditGridViewActivity;
import com.drnds.titlelogy.activity.client.orderqueueactivity.EditOrderActivity;
import com.drnds.titlelogy.model.client.GridItem;
import com.drnds.titlelogy.model.client.OrderQueue;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ajithkumar on 6/27/2017.
 */

public  class RecyclergridviewAdapter extends RecyclerView.Adapter< RecyclergridviewAdapter.MyViewHolder>implements Filterable {
    private ArrayList<GridItem> gridItemList;
    private ArrayList<GridItem> mFilteredList;

    private Context ctx;

    SharedPreferences sharedpreferences;
    public static final String GRID = "gridadpter";
    RecyclerView mRecyclerView;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status, barrowername,date;
        private List<GridItem> gridItemList=new ArrayList<GridItem>();
        private   ItemClickListener itemClickListener;
        public MyViewHolder(View view,Context ctx,List<GridItem> gridItemList) {
            super(view);
            this.gridItemList=gridItemList;
            subclient = (TextView) view.findViewById(R.id.grid_subclient);
            oderno = (TextView) view.findViewById(R.id.grid_orderno);
            producttype = (TextView) view.findViewById(R.id.gridview_producttype);
            state = (TextView) view.findViewById(R.id.gridview_state);
            county = (TextView) view.findViewById(R.id.gridview_county);
            propertyaddress = (TextView) view.findViewById(R.id.gridview_address);
            status = (TextView) view.findViewById(R.id.grid_status);
            barrowername = (TextView) view.findViewById(R.id.gridview_borrowername);
            date= (TextView) view.findViewById(R.id.text_gdate);
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
    public RecyclergridviewAdapter(ArrayList<GridItem>gridItemList) {
        this. gridItemList=   gridItemList;
        this. mFilteredList=   gridItemList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_row, parent, false);

        return new MyViewHolder(itemView,ctx,gridItemList);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.date.setText (gridItemList.get(position).getDate());

        holder.subclient.setText (mFilteredList.get(position).getSubclient());

        holder.oderno.setText( mFilteredList.get(position).getOderno());
        holder.producttype.setText( mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText( mFilteredList.get(position).getPropertyaddress());

        holder.state .setText( mFilteredList.get(position).getState());
        holder.county.setText( mFilteredList.get(position).getCounty());
        holder.status.setText( mFilteredList.get(position).getStatus());
        holder.barrowername.setText( mFilteredList.get(position).getBarrowername());


        holder.setItemClickListener(new RecyclergridviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                GridItem gridItem = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent=new Intent(context, EditGridViewActivity.class);

                sharedpreferences = context.getSharedPreferences(GRID, Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();

                //Adding values to editor

                editor.putString("Order_Id",gridItem.getOrderId());
                editor.putString("Sub_Client_Name", gridItem.getSubclient());
                editor.putString("Product_Type", gridItem.getProducttype());
                editor.putString("State", gridItem.getState());
                editor.putString("County", gridItem.getCounty());
                editor.putString("Order_Priority", gridItem.getOrderpriority());
                editor.putString("Order_Assign_Type", gridItem.getCountytype());
                editor.putString("Progress_Status", gridItem.getStatus());
                editor.putString("Barrower_Name", gridItem.getBarrowername());
                editor.putString("Order_Status", gridItem.getOrdertask());
                editor.putString("Sub_Client_Id", gridItem.getSubId());
                Logger.getInstance().Log("subid22 " + gridItem.getSubId());
                editor.commit();
                context.startActivity(intent);
//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());

            }
        });

    }

    @Override
    public int getItemCount() {
        return  mFilteredList.size();
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = gridItemList;
                } else {

                    ArrayList<GridItem> filteredList = new ArrayList<>();

                    for (GridItem gridItem : gridItemList) {

                        if (gridItem.getSubclient().toLowerCase().contains(charString) ||
                                gridItem.getOderno().toLowerCase().contains(charString) ||
                                gridItem.getProducttype().toLowerCase().contains(charString)||
                                gridItem.getPropertyaddress().toLowerCase().contains(charString)||
                                gridItem.getState().toLowerCase().contains(charString)||
                                gridItem.getCounty().toLowerCase().contains(charString)||
                                gridItem.getCounty().toLowerCase().contains(charString)||
                                gridItem.getBarrowername().toLowerCase().contains(charString))
                        {

                            filteredList.add(gridItem);
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
                mFilteredList = (ArrayList<GridItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }



}

