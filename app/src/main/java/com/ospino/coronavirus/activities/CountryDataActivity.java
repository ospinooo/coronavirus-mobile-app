package com.ospino.coronavirus.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ospino.coronavirus.R;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Country;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.models.History;
import com.ospino.coronavirus.service.CoronavirusApi;
import com.ospino.coronavirus.utils.GraphDateFormater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CountryDataActivity extends Activity {

    private static String TAG = "ActivityCountryData";
    private Breakdown country;
    private Country country_data;
    protected ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);
        getIncomingIntent();

        new AsyncCaller().execute();
    }

    protected String getCountryIso() {
        return this.country.getLocation().getIsoCode();
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        Global global = null;

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                country_data = CoronavirusApi.getInstance().getCountryData(getCountryIso());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            List<Entry> dataSet = country_data.getStats().getHistory().stream()
                    .map(point -> new Entry(point.getTimeStamp(), point.getConfirmed()))
                    .collect(Collectors.toList());

            List<Entry> dataSetConfirmed = new ArrayList<Entry>();
            List<Entry> dataSetDeaths = new ArrayList<Entry>();
            List<Entry> dataSetRecovered = new ArrayList<Entry>();

            for (History point: country_data.getStats().getHistory()) {
                Long ts = point.getTimeStamp();
                dataSetConfirmed.add(new Entry(ts, point.getConfirmed()));
                dataSetDeaths.add(new Entry(ts, point.getDeaths()));
                dataSetRecovered.add(new Entry(ts, point.getRecovered()));
            }

            ArrayList colors = new ArrayList<Color>();
            colors.add(Color.BLUE);
            colors.add(Color.RED);
            colors.add(Color.GREEN);

            LineData lineChartData = new LineData();
            lineChartData.setValueTextColors(colors);

            lineChartData.addDataSet(new LineDataSet(dataSetConfirmed, "Confirmed Cases"));
            lineChartData.addDataSet(new LineDataSet(dataSetDeaths, "Deaths Cases"));
            lineChartData.addDataSet(new LineDataSet(dataSetRecovered, "Recovered Cases"));

            viewHolder.lineChartAll.setData(lineChartData);

            viewHolder.lineChartProgressBar.setVisibility(View.INVISIBLE);
            viewHolder.lineChartAll.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder {
        protected TextView textCountry;
        protected ImageView imageViewFlag;
        protected TextView textTotalConfirmedCases;
        protected TextView textTotalDeaths;
        protected TextView textTotalRecovered;
        protected TextView textTodayConfirmedCases;
        protected TextView textTodayDeaths;
        protected TextView textTodayRecovered;
        protected TextView textActiveCases;
        protected LineChart lineChartAll;
        protected LineChart lineChartConfirmed;
        protected LineChart lineChartDeaths;
        protected LineChart lineChartRecovered;
        protected ProgressBar lineChartProgressBar;
    }

    private void getIncomingIntent(){
        Log.v(TAG, "getIncomingIntent: checking for incoming intents");
        if (getIntent().hasExtra("country")) {
            Log.v(TAG, "getIncomingIntent: found extras");

            country = (Breakdown) getIntent().getExtras().get("country");
            inflateActivityView(country);
        }
    }

    private void inflateActivityView(Breakdown country) {
        viewHolder = new ViewHolder();

        viewHolder.textCountry =  findViewById(R.id.text_country);
        viewHolder.imageViewFlag = findViewById(R.id.image_flag);
        // Total
        viewHolder.textTotalConfirmedCases = findViewById(R.id.text_total_confirmed_cases);
        viewHolder.textTotalDeaths = findViewById(R.id.text_total_deaths);
        viewHolder.textTotalRecovered = findViewById(R.id.text_total_recovered);
        // Today
        viewHolder.textTodayConfirmedCases = findViewById(R.id.text_today_confirmed_cases);
        viewHolder.textTodayDeaths = findViewById(R.id.text_today_deaths);
        viewHolder.textTodayRecovered = findViewById(R.id.text_today_recovered);
        // Active Cases
        viewHolder.textActiveCases = findViewById(R.id.text_active_cases_placeholder);
        // Chart
        viewHolder.lineChartAll = findViewById(R.id.chart);
        viewHolder.lineChartAll.getXAxis().setValueFormatter(new GraphDateFormater());
        viewHolder.lineChartProgressBar = findViewById(R.id.line_chart_progress_bar);

        viewHolder.textCountry.setText(country.getLocation().getCountryOrRegion());
        viewHolder.imageViewFlag.setImageResource(getResources()
                        .getIdentifier(
                                "drawable/ic_list_country_" + country.getLocation().getIsoCode().toLowerCase(),
                                null,
                                getPackageName()));

        viewHolder.textTotalConfirmedCases.setText(String.valueOf(country.getTotalConfirmedCases()));
        viewHolder.textTotalDeaths.setText(String.valueOf(country.getTotalDeaths()));
        viewHolder.textTotalRecovered.setText(String.valueOf(country.getTotalRecoveredCases()));

        viewHolder.textTodayConfirmedCases.setText(String.valueOf(country.getNewlyConfirmedCases()));
        viewHolder.textTodayDeaths.setText(String.valueOf(country.getNewDeaths()));
        viewHolder.textTodayRecovered.setText(String.valueOf(country.getNewlyRecoveredCases()));

        viewHolder.textActiveCases.setText(String.valueOf(country.getActiveCases()));
    }
}


