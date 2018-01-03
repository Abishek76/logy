package com.drnds.titlelogy.adapter.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.vendor.VendorPendingTab;

import java.util.ArrayList;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorPendingTabGridAdapter extends BaseAdapter {
    Context c;
    ArrayList<VendorPendingTab> vendorpendingTabArrayList;
    public VendorPendingTabGridAdapter(Context c, ArrayList<VendorPendingTab>  vendorpendingTabArrayList) {
        this.c = c;
        this. vendorpendingTabArrayList =  vendorpendingTabArrayList;
    }
    @Override
    public int getCount() {
        return  vendorpendingTabArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return  vendorpendingTabArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.vendor_tabpending_row,viewGroup,false);
        }
        final VendorPendingTab s= (VendorPendingTab) this.getItem(i);

        TextView orderNo= (TextView) view.findViewById(R.id.orderno_pentab_vendor);
        TextView title= (TextView) view.findViewById(R.id.title_pentab_vendor);

        orderNo.setText(s.getPenorderno());
        title.setText(s.getPentitle());

        return view;
    }
}
