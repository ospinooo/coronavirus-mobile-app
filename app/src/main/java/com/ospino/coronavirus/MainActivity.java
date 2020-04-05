package com.ospino.coronavirus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.textclassifier.TextClassifier;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ospino.coronavirus.activities.CountryDataActivity;
import com.ospino.coronavirus.activities.WorldDataActivity;
import com.ospino.coronavirus.adapters.MainListAdapter;
import com.ospino.coronavirus.adapters.MainListAdapter.CountrySortFields;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.service.CoronavirusApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
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
    protected ConstraintLayout headers = null;
    protected Global global = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListAdapter = new MainListAdapter(this, new ArrayList<>());

        // Set Adapter to list view
        mainListView = findViewById(R.id.main_list_view);
        mainListView.setAdapter(mainListAdapter);

        // World button
        findViewById(R.id.button_world_data).setOnClickListener(view -> {
            if (global != null){
                Intent intent = new Intent(this, WorldDataActivity.class);
                intent.putExtra("country", global);
                startActivity(intent);
            }
        });

        // Swipe Refresh
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> {
            if (isNetworkConnected()){
                // Get all data
                findViewById(R.id.no_internet_connection).setVisibility(View.GONE);
                new AsyncCaller().execute();
            } else {
                mainListAdapter.clear();
                mainListAdapter.notifyDataSetChanged();
                findViewById(R.id.no_internet_connection).setVisibility(View.VISIBLE);
                swipeLayout.setRefreshing(false);
            }
        });

        if (isNetworkConnected()){
            // Get all data
            findViewById(R.id.no_internet_connection).setVisibility(View.GONE);
            new AsyncCaller().execute();
        } else {
            findViewById(R.id.no_internet_connection).setVisibility(View.VISIBLE);
        }

        // Headers
        listHeaders = new ListHeaders();
        headers = findViewById(R.id.headers);
        setSortingListeners();
        listHeaders.resetAllNotBold();
        listHeaders.textCountry.setTypeface(null, Typeface.BOLD_ITALIC);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnSearchClickListener(view -> {
            headers.setVisibility(View.GONE);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v(TAG, "TextChange");
                mainListAdapter.getFilter().filter(s);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            // Regenerate the list adapter
            CountrySortFields sortFields = mainListAdapter.getCurrentSortField();
            mainListAdapter = new MainListAdapter(this, mainListAdapter.getList());
            mainListAdapter.setCurrentSortField(sortFields);
            mainListView.setAdapter(mainListAdapter);

            headers.setVisibility(View.VISIBLE);
            setSortingListeners();
            mainListAdapter.sortByCurrentField();
            return false;
        });



        return true;

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

    protected void setSortingListeners() {
        headers = findViewById(R.id.headers);

        listHeaders.textCountry = headers.findViewById(R.id.text_country);
        listHeaders.textConfirmed = headers.findViewById(R.id.text_confirmed);
        listHeaders.textRecovered = headers.findViewById(R.id.text_recovered);
        listHeaders.textDeaths = headers.findViewById(R.id.text_deaths);
        listHeaders.textTodayNewCases = headers.findViewById(R.id.text_new_cases);

        listHeaders.textCountry.setOnClickListener(view -> {
            mainListAdapter.sortByField(CountrySortFields.COUNTRY_ISO);
            mainListAdapter.notifyDataSetChanged();
            listHeaders.resetAllNotBold();
            listHeaders.textCountry.setTypeface(null, Typeface.BOLD_ITALIC);
        });

        listHeaders.textConfirmed.setOnClickListener(view -> {
            Log.v(TAG, "Sort by confirmed");
            mainListAdapter.sortByField(CountrySortFields.CONFIRMED);
            mainListAdapter.notifyDataSetChanged();
            listHeaders.resetAllNotBold();
            listHeaders.textConfirmed.setTypeface(null, Typeface.BOLD_ITALIC);
        });

        listHeaders.textDeaths.setOnClickListener(view -> {
            mainListAdapter.sortByField(CountrySortFields.DEATHS);
            mainListAdapter.notifyDataSetChanged();
            listHeaders.resetAllNotBold();
            listHeaders.textDeaths.setTypeface(null, Typeface.BOLD_ITALIC);
        });

        listHeaders.textRecovered.setOnClickListener(view -> {
            mainListAdapter.sortByField(CountrySortFields.RECOVERED);
            listHeaders.resetAllNotBold();
            mainListAdapter.notifyDataSetChanged();
            listHeaders.textRecovered.setTypeface(null, Typeface.BOLD_ITALIC);
        });

        listHeaders.textTodayNewCases.setOnClickListener(view -> {
            mainListAdapter.sortByField(CountrySortFields.NEW_CASES);
            listHeaders.resetAllNotBold();
            mainListAdapter.notifyDataSetChanged();
            listHeaders.textTodayNewCases.setTypeface(null, Typeface.BOLD_ITALIC);
        });
    }


    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
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
            mainListAdapter.sortByCurrentField();
            mainListAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        }
    }


    /**
     * isNetworkConnected
     * @return true if we have internet or false if we don't
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
