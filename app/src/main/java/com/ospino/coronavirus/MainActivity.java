package com.ospino.coronavirus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassifier;
import android.widget.ListView;
import android.widget.TextView;

import com.ospino.coronavirus.adapters.MainListAdapter;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.service.CoronavirusApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private CoronavirusApi coronavirusApi = null;
    private MainListAdapter mainListAdapter = null;
    private SwipeRefreshLayout swipeLayout = null;
    private ListView mainListView = null;
    private ListHeaders listHeaders = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListAdapter = new MainListAdapter(this, new ArrayList<>());

        // Set Adapter to list view
        mainListView = findViewById(R.id.main_list_view);
        mainListView.setAdapter(mainListAdapter);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> new AsyncCaller().execute());

        setSortingListeners();
        // Get all data
        new AsyncCaller().execute();
    }

    static class ListHeaders {
        protected TextView textCountry;
        protected TextView textConfirmed;
        protected TextView textRecovered;
        protected TextView textTodayNewCases;
        protected TextView textDeaths;

        public void resetAllNotBold() {
            textCountry.setTypeface(null, Typeface.NORMAL);
            textConfirmed.setTypeface(null, Typeface.NORMAL);
            textRecovered.setTypeface(null, Typeface.NORMAL);
            textDeaths.setTypeface(null, Typeface.NORMAL);
            textTodayNewCases.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setSortingListeners() {

        ConstraintLayout headers = findViewById(R.id.headers);
        listHeaders = new ListHeaders();
        listHeaders.textCountry = headers.findViewById(R.id.text_country);
        listHeaders.textConfirmed = headers.findViewById(R.id.text_confirmed);
        listHeaders.textRecovered = headers.findViewById(R.id.text_recovered);
        listHeaders.textDeaths = headers.findViewById(R.id.text_deaths);
        listHeaders.textTodayNewCases = headers.findViewById(R.id.text_new_cases);

        listHeaders.textCountry.setOnClickListener(view -> {
            mainListAdapter.sort((breakdown, t1) -> breakdown.getLocation().getIsoCode().compareTo(t1.getLocation().getIsoCode()));
            listHeaders.resetAllNotBold();
            listHeaders.textCountry.setTypeface(null, Typeface.BOLD);
        });

        listHeaders.textConfirmed.setOnClickListener(view -> {
            mainListAdapter.sort((breakdown, t1) -> t1.getTotalConfirmedCases() - breakdown.getTotalConfirmedCases());
            listHeaders.resetAllNotBold();
            listHeaders.textConfirmed.setTypeface(null, Typeface.BOLD);
        });

        listHeaders.textDeaths.setOnClickListener(view -> {
            mainListAdapter.sort((breakdown, t1) -> t1.getTotalDeaths() - breakdown.getTotalDeaths());
            listHeaders.resetAllNotBold();
            listHeaders.textDeaths.setTypeface(null, Typeface.BOLD);
        });

        listHeaders.textRecovered.setOnClickListener(view -> {
            mainListAdapter.sort((breakdown, t1) -> t1.getTotalRecoveredCases() - breakdown.getTotalRecoveredCases());
            listHeaders.resetAllNotBold();
            listHeaders.textRecovered.setTypeface(null, Typeface.BOLD);
        });

        listHeaders.textTodayNewCases.setOnClickListener(view -> {
            mainListAdapter.sort((breakdown, t1) -> t1.getNewlyConfirmedCases() - breakdown.getNewlyConfirmedCases());
            listHeaders.resetAllNotBold();
            listHeaders.textTodayNewCases.setTypeface(null, Typeface.BOLD);
        });
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        Global global = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                global = CoronavirusApi.getInstance().getGlobalDataFilteredNotNullIso();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            mainListAdapter.clear();
            mainListAdapter.addAll(global.getStats().getBreakdowns());
            listHeaders.resetAllNotBold();
            mainListAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        }
    }


}
