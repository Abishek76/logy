package com.drnds.titlelogy.util;

import android.content.Context;
import android.widget.ImageView;

import com.drnds.titlelogy.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ajith on 12/26/2017.
 */

public class ImageLoaderProfile {
    // default image show in list (Before online image download)
    final int stub_id= R.drawable.logo_titleogy;

    private static ImageLoaderProfile mInstance;
    Context _mContext;

    private ImageLoaderProfile(Context context){
        this._mContext = context;
    }

    public static synchronized ImageLoaderProfile getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new ImageLoaderProfile(context);
        }
        return mInstance;
    }

    public void DisplayImage(String url, ImageView imageView)
    {
        if(url.equals(""))
        {
            imageView.setImageResource(stub_id);
        }
        else
        {
            Picasso.with(_mContext)
                    .load(url)
                    .placeholder(stub_id)
                    .error(stub_id)
                    .into(imageView);
        }
    }
}