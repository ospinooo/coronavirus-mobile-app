package com.ospino.coronavirus.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
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

import androidx.appcompat.app.AppCompatActivity;


public class CountryDataActivity extends AppCompatActivity {

    private static String TAG = "ActivityCountryData";
    private Breakdown country;
    private Country country_data;
    protected ViewHolder viewHolder;
    private Toolbar toolbar;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_mapbox_token));
        setContentView(R.layout.activity_country_data);

        getIncomingIntent();

        // Mapbox
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        setMapBox();

        // Toolbar
        toolbar = findViewById(R.id.toolbar_country_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new AsyncCaller().execute();
    }

    private void setMapBox() {
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"));
            mapboxMap.getUiSettings().setAllGesturesEnabled(false);

            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(country.getLocation().getLat(), country.getLocation().getLong()))
                    .zoom(3)
                    .tilt(0)
                    .build();
            mapboxMap.setCameraPosition(position);

            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(country.getLocation().getLat(), country.getLocation().getLong()));
            mapboxMap.addMarker(options);
        });
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
            List<Entry> dataSetActive = new ArrayList<Entry>();

            for (History point: country_data.getStats().getHistory()) {
                Long ts = point.getTimeStamp();
                dataSetConfirmed.add(new Entry(ts, point.getConfirmed()));
                dataSetDeaths.add(new Entry(ts, point.getDeaths()));
                dataSetRecovered.add(new Entry(ts, point.getRecovered()));
                dataSetActive.add(new Entry(ts, point.getConfirmed()- (point.getDeaths() + point.getRecovered())));
            }

            LineData lineChartData = new LineData();

            LineDataSet confirmedDataSet = new LineDataSet(dataSetConfirmed, "Confirmed");
            confirmedDataSet.setCircleColor(getResources().getColor(R.color.blue, getTheme()));
            confirmedDataSet.setColor(getResources().getColor(R.color.blue, getTheme()));
            lineChartData.addDataSet(confirmedDataSet);

            LineDataSet deathsDataSet = new LineDataSet(dataSetDeaths, "Deaths");
            deathsDataSet.setCircleColor(getResources().getColor(R.color.red, getTheme()));
            deathsDataSet.setColor(getResources().getColor(R.color.red, getTheme()));
            lineChartData.addDataSet(deathsDataSet);

            LineDataSet recoveredDataSet = new LineDataSet(dataSetRecovered, "Recovered");
            recoveredDataSet.setCircleColor(getResources().getColor(R.color.green, getTheme()));
            recoveredDataSet.setColor(getResources().getColor(R.color.green, getTheme()));
            lineChartData.addDataSet(recoveredDataSet);

            LineDataSet activeDataSet = new LineDataSet(dataSetActive, "Active");
            activeDataSet.setCircleColor(getResources().getColor(R.color.yellow, getTheme()));
            activeDataSet.setColor(getResources().getColor(R.color.yellow, getTheme()));
            lineChartData.addDataSet(activeDataSet);

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

        viewHolder.textTotalConfirmedCases.setText(String.format("%,d", country.getTotalConfirmedCases()));
        viewHolder.textTotalDeaths.setText(String.format("%,d",country.getTotalDeaths()));
        viewHolder.textTotalRecovered.setText(String.format("%,d",country.getTotalRecoveredCases()));

        viewHolder.textTodayConfirmedCases.setText(String.format("%,d",country.getNewlyConfirmedCases()));
        viewHolder.textTodayDeaths.setText(String.format("%,d",country.getNewDeaths()));
        viewHolder.textTodayRecovered.setText(String.format("%,d",country.getNewlyRecoveredCases()));

        viewHolder.textActiveCases.setText(String.format("%,d",country.getActiveCases()));
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


