package com.ospino.coronavirus.utils;

import android.content.res.Resources;
import android.content.res.Resources.Theme;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ospino.coronavirus.R;
import com.ospino.coronavirus.models.History;

import java.util.ArrayList;
import java.util.List;

public class LineGraphHistoryGenerator {

    public static LineData generateLineChartDataFromHistoricalData(List<History> history, Resources resource, Theme theme) {

        List<Entry> dataSetConfirmed = new ArrayList<Entry>();
        List<Entry> dataSetDeaths = new ArrayList<Entry>();
        List<Entry> dataSetRecovered = new ArrayList<Entry>();
        List<Entry> dataSetActive = new ArrayList<Entry>();

        for (History point: history) {
            Long ts = point.getTimeStamp();
            dataSetConfirmed.add(new Entry(ts, point.getConfirmed()));
            dataSetDeaths.add(new Entry(ts, point.getDeaths()));
            dataSetRecovered.add(new Entry(ts, point.getRecovered()));
            dataSetActive.add(new Entry(ts, point.getConfirmed()- (point.getDeaths() + point.getRecovered())));
        }

        LineData lineChartData = new LineData();

        LineDataSet confirmedDataSet = new LineDataSet(dataSetConfirmed, "Confirmed");
        confirmedDataSet.setCircleColor(resource.getColor(R.color.blue, theme));
        confirmedDataSet.setColor(resource.getColor(R.color.blue, theme));
        lineChartData.addDataSet(confirmedDataSet);

        LineDataSet deathsDataSet = new LineDataSet(dataSetDeaths, "Deaths");
        deathsDataSet.setCircleColor(resource.getColor(R.color.red, theme));
        deathsDataSet.setColor(resource.getColor(R.color.red, theme));
        lineChartData.addDataSet(deathsDataSet);

        LineDataSet recoveredDataSet = new LineDataSet(dataSetRecovered, "Recovered");
        recoveredDataSet.setCircleColor(resource.getColor(R.color.green, theme));
        recoveredDataSet.setColor(resource.getColor(R.color.green, theme));
        lineChartData.addDataSet(recoveredDataSet);

        LineDataSet activeDataSet = new LineDataSet(dataSetActive, "Active");
        activeDataSet.setCircleColor(resource.getColor(R.color.yellow, theme));
        activeDataSet.setColor(resource.getColor(R.color.yellow, theme));
        lineChartData.addDataSet(activeDataSet);

        return lineChartData;
    }
}
