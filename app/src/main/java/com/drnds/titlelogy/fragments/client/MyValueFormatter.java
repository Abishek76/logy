package com.drnds.titlelogy.fragments.client;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by ajithkumar on 8/9/2017.
 */

public class MyValueFormatter implements ValueFormatter {





    @Override
    public String getFormattedValue(float value) {
        return Math.round(value)+"";
    }
}