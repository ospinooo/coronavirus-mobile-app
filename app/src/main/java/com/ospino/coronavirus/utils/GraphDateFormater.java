package com.ospino.coronavirus.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphDateFormater extends ValueFormatter {

    private static String TAG = "GraphDateFormater";

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM. dd");
        return simpleDateFormat.format(new Date((long) value));
    }
}
