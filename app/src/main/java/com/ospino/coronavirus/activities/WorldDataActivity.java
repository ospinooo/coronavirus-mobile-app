package com.ospino.coronavirus.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.ospino.coronavirus.R;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.models.History;
import com.ospino.coronavirus.models.Stats;
import com.ospino.coronavirus.utils.GraphDateFormater;
import com.ospino.coronavirus.utils.LineGraphHistoryGenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldDataActivity extends AppCompatActivity {

    protected String TAG = "WorldDataActivity";
    protected Global global = null;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_mapbox_token));
        setContentView(R.layout.activity_world_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(new Style.Builder().fromUri(getString(R.string.mapbox_style_custom)));
            mapboxMap.getUiSettings().setAllGesturesEnabled(false);
        });

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.v(TAG, "getIncomingIntent: checking for incoming intents");
        if (getIntent().hasExtra("country")) {
            Log.v(TAG, "getIncomingIntent: found extras");

            global = (Global) getIntent().getExtras().get("country");
            inflateActivityView(global);
        }
    }

    static class ViewHolder {
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

    private void inflateActivityView(Global global) {
        viewHolder = new ViewHolder();

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

        Stats stats = global.getStats();
        // Total
        viewHolder.textTotalConfirmedCases.setText(String.format("%,d", stats.getTotalConfirmedCases()));
        viewHolder.textTotalDeaths.setText(String.format("%,d", stats.getTotalDeaths()));
        viewHolder.textTotalRecovered.setText(String.format("%,d", stats.getTotalRecoveredCases()));
        // Today
        viewHolder.textTodayConfirmedCases.setText(String.format("%,d", stats.getNewlyConfirmedCases()));
        viewHolder.textTodayDeaths.setText(String.format("%,d", stats.getNewDeaths()));
        viewHolder.textTodayRecovered.setText(String.format("%,d", stats.getNewlyRecoveredCases()));

        viewHolder.textActiveCases.setText(String.format("%,d", stats.getTotalActiveCases()));

        LineData lineChartData = LineGraphHistoryGenerator.generateLineChartDataFromHistoricalData(
                global.getStats().getHistory(),
                getResources(),
                getTheme());

        viewHolder.lineChartAll.setData(lineChartData);
        viewHolder.lineChartProgressBar.setVisibility(View.INVISIBLE);
        viewHolder.lineChartAll.setVisibility(View.VISIBLE);
    }
}
