package com.drnds.titlelogy.fragments.client;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by ajithkumar on 8/9/2017.
 */

public interface ValueFormatter {
    String getFormattedValue(float value);
}
