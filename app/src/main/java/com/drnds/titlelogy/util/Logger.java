package com.drnds.titlelogy.util;

import android.util.Log;

/**
 * Created by Ajithkumar on 5/31/2017.
 */

public class Logger {
    private boolean flag = true;
    private static String Tag = "DRN";
    private static Logger mInstance;
    private Logger()
    {

    }

    public static synchronized Logger getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new Logger();
        }
        return mInstance;
    }

    public void Log(String log)
    {
        if(flag){
            Log.e(Tag," : "+log);
        }
    }
}

