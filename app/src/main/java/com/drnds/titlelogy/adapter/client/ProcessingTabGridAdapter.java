package com.drnds.titlelogy.adapter.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.drnds.titlelogy.R;
import com.drnds.titlelogy.model.client.ProcessTab;

import java.util.ArrayList;

/**
 * Created by ajithkumar on 6/26/2017.
 */

public class ProcessingTabGridAdapter extends BaseAdapter {
    Context c;
    ArrayList<ProcessTab> processTablist;
    public ProcessingTabGridAdapter(Context c, ArrayList<ProcessTab>  processTablist) {
        this.c = c;
        this. processTablist =  processTablist;
    }
    @Override
    public int getCount() {
        return  processTablist.size();
    }
    @Override
    public Object getItem(int i) {
        return  processTablist.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.tabprocessing_row,viewGroup,false);
        }
        final ProcessTab s= (ProcessTab) this.getItem(i);

        TextView orderNo= (TextView) view.findViewById(R.id.orderno_prtab);
        TextView title= (TextView) view.findViewById(R.id.title_prtab);
        //BIND
        orderNo.setText(s.getOrderno());
        title.setText(s.getTitle());

        return view;
    }
}