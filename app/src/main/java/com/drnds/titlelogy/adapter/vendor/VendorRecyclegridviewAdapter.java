package com.drnds.titlelogy.adapter.vendor;

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
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.activity.vendor.gridactivity.EditGridViewVendorActivity;
import com.drnds.titlelogy.model.vendor.VendorGridItem;
import com.drnds.titlelogy.util.Logger;

import java.util.ArrayList;
import java.util.Random;
import android.util.SparseBooleanArray;
import static com.drnds.titlelogy.R.id.chkSelected;
import static com.drnds.titlelogy.util.Config.NewOrderFlag;
import static com.drnds.titlelogy.util.Config.Order_Filter;
import static com.drnds.titlelogy.util.Config.selectedArrays;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorRecyclegridviewAdapter extends RecyclerView.Adapter< VendorRecyclegridviewAdapter.MyViewHolder>implements Filterable {
    private ArrayList<VendorGridItem> vendorgridItemList;
    private ArrayList<VendorGridItem> mFilteredList;
    ArrayList<String> selectd = new ArrayList<String>();
    ArrayList<String> selectselection = new ArrayList<String>();

    public  LinearLayout chkbox;
    private Context context;

    SharedPreferences sp;
    public static final String VENDORGRID = "vendorgridadpter";

    //    for animation
    private int lastPosition = -1;


    public ArrayList<String> getArrayList(){
        return selectd;
    }


    public ArrayList<String> getSelectedArray(){
        return selectselection;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status, barrowername,date;
        public CheckBox chkSelected;
        private  ItemClickListener itemClickListener;
        public MyViewHolder(View view) {
            super(view);

            subclient = (TextView) view.findViewById(R.id.vendor_grid_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_grid_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_gridview_producttype);
            state = (TextView) view.findViewById(R.id.vendor_gridview_state);
            county = (TextView) view.findViewById(R.id.vendor_gridview_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_gridview_address);
            status = (TextView) view.findViewById(R.id.vendor_grid_status);
            barrowername = (TextView) view.findViewById(R.id.vendor_gridview_borrowername);
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
            chkbox=(LinearLayout) view.findViewById(R.id.linear_chk);

            date = (TextView) view.findViewById(R.id.text_vgdate);

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
    public VendorRecyclegridviewAdapter(ArrayList<VendorGridItem>vendorgridItemList) {
        this. vendorgridItemList=   vendorgridItemList;
        this. mFilteredList=   vendorgridItemList;

    }

    @Override
    public VendorRecyclegridviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorRecyclegridviewAdapter.MyViewHolder holder, final int position) {
        final int pos = position;
        VendorGridItem vendorgridItem = vendorgridItemList.get(position);
        System.out.println("Orderfilter"+Order_Filter);
        if(Order_Filter=="NEW_ORDERS"){
            chkbox.setVisibility(View.VISIBLE);
        }
        holder.date.setText(mFilteredList.get(position).getDate());
        holder.subclient.setText (mFilteredList.get(position).getSubclient());
        holder.oderno.setText( mFilteredList.get(position).getOderno());
        holder.status.setText(mFilteredList.get(position).getStatus());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.barrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.state.setText(mFilteredList.get(position).getState());
        holder.county.setText(mFilteredList.get(position).getCounty());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());



        holder.chkSelected.setChecked(vendorgridItemList.get(position).isSelected());

        holder.chkSelected.setTag(vendorgridItemList.get(position));



        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                VendorGridItem contact = (VendorGridItem) cb.getTag();

                contact.setSelected(cb.isChecked());
                vendorgridItemList.get(pos).setSelected(cb.isChecked());

                System.out.println("selectedOrdershiiii" + mFilteredList.get(pos).getOrderId());

                if (cb.isChecked()) {

//                     if (!selectd.isEmpty()){
//                         cb.setEnabled(false);
//                     }
//                     else {
//                         cb.setClickable(true);
//                     }

                    selectedArrays.add(mFilteredList.get(pos).getOrderId());

                    System.out.println("selected = " + selectedArrays);
                }else{
                    String removedOrderID = (String)mFilteredList.get(pos).getOrderId();
                    selectedArrays.remove(removedOrderID);
                    System.out.println("selected removed = " + removedOrderID);
                }


                System.out.println("final="+selectedArrays);


                Toast.makeText(
                        v.getContext(),
                        "Clicked on Checkbox: " + cb.getText() + " is "
                                + selectedArrays, Toast.LENGTH_SHORT).show();
            }
        });
//        for animation
        holder.setItemClickListener(new ItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                VendorGridItem vendorgridItem = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent = new Intent(context,EditGridViewVendorActivity.class);
                sp = context.getApplicationContext().getSharedPreferences(
                        "vendorgridadpter", 0);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id",vendorgridItem.getOrderId());
                Logger.getInstance().Log("orderidadapter "+vendorgridItem.getOrderId());
                editor.putString("Sub_Client_Name", vendorgridItem.getSubclient());
                editor.putString("Order_Type", vendorgridItem.getProducttype());
                editor.putString("State_Name", vendorgridItem.getState());
                editor.putString("County_Name", vendorgridItem.getCounty());
                editor.putString("State", vendorgridItem.getStateid());
                editor.putString("Order_Priority", vendorgridItem.getOrderpriority());
                editor.putString("Order_Assign_Type", vendorgridItem.getCountytype());
                editor.putString("Progress_Status", vendorgridItem.getStatus());
                editor.putString("Barrower_Name", vendorgridItem.getBarrowername());
                editor.putString("Order_Number", vendorgridItem.getOderno());
                editor.putString("Order_Status", vendorgridItem.getOrdertask());
                Logger.getInstance().Log("Order_Status of " + vendorgridItem.getOrdertask());

                editor.putString("Order_Status", vendorgridItem.getOrdertask());

                editor.commit();
                Logger.getInstance().Log("status888 " + vendorgridItem.getStatus());
                context.startActivity(intent);
            }
        });

    }

    //    for animation




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

                    mFilteredList = vendorgridItemList;
                } else {

                    ArrayList<VendorGridItem> filteredList = new ArrayList<>();

                    for (VendorGridItem vendorgridItem : vendorgridItemList) {

                        if (vendorgridItem.getSubclient().toLowerCase().contains(charString) ||
                                vendorgridItem.getOderno().toLowerCase().contains(charString) ||
                                vendorgridItem.getProducttype().toLowerCase().contains(charString)||
                                vendorgridItem.getPropertyaddress().toLowerCase().contains(charString)||
                                vendorgridItem.getState().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getCounty().toLowerCase().contains(charString)||
                                vendorgridItem.getBarrowername().toLowerCase().contains(charString))

                        {

                            filteredList.add(vendorgridItem);
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
                mFilteredList = (ArrayList<VendorGridItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }
}


