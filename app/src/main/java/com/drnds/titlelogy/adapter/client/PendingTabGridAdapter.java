package com.drnds.titlelogy.adapter.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.client.PendingTab;

import java.util.ArrayList;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class PendingTabGridAdapter extends BaseAdapter {
    Context c;
    ArrayList<PendingTab> pendingTabArrayList;
    public PendingTabGridAdapter(Context c, ArrayList<PendingTab>  pendingTabArrayList) {
        this.c = c;
        this. pendingTabArrayList =  pendingTabArrayList;
    }
    @Override
    public int getCount() {
        return  pendingTabArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return  pendingTabArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.tabpending_row,viewGroup,false);
        }
        final PendingTab s= (PendingTab) this.getItem(i);

        TextView orderNo= (TextView) view.findViewById(R.id.orderno_pentab);
        TextView title= (TextView) view.findViewById(R.id.title_pentab);

        orderNo.setText(s.getPenorderno());
        title.setText(s.getPentitle());

        return view;
    }
}
