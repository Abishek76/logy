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
import com.drnds.titlelogy.model.client.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ajith on 9/14/2017.
 */

public class ResultRecyclerviewAdapter extends RecyclerView.Adapter<ResultRecyclerviewAdapter.MyViewHolder>  {
    private List<Search> searchList;
    private Activity activity;
    private Context ctx;
    private int lastPosition = -1;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
        private List<Search> searchList=new ArrayList<Search>();
        Context ctx;
        public MyViewHolder(View view, Context ctx, List<Search> searchList) {
            super(view);
            this.searchList=searchList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.result_subclient);
            oderno = (TextView) view.findViewById(R.id.result_orderno);
            producttype = (TextView) view.findViewById(R.id.result_producttype);
            state = (TextView) view.findViewById(R.id.result_state);
            county = (TextView) view.findViewById(R.id.result_county);
            propertyaddress = (TextView) view.findViewById(R.id.result_address);
            status = (TextView) view.findViewById(R.id.result_status);
            borrowername = (TextView) view.findViewById(R.id.result_borrowername);


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
    public ResultRecyclerviewAdapter(List<Search>searchList) {
        this. searchList=   searchList;
        this.ctx=ctx;
    }

    @Override
    public ResultRecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);

        return new ResultRecyclerviewAdapter.MyViewHolder(itemView,ctx,searchList);
    }

    @Override
    public void onBindViewHolder(ResultRecyclerviewAdapter.MyViewHolder holder, int position) {
        Search report = searchList.get(position);
        holder.subclient.setText(  report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getBarrowername());

        setAnimation(holder.itemView, position);


    }
    @Override
    public int getItemCount() {

        return  searchList.size();
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
