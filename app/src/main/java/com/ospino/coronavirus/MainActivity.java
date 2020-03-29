package com.ospino.coronavirus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.ospino.coronavirus.adapters.MainListAdapter;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.service.CoronavirusApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CoronavirusApi coronavirusApi = null;
    private MainListAdapter mainListAdapter = null;
    private SwipeRefreshLayout swipeLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainListAdapter = new MainListAdapter(this, new ArrayList<>());

        // Set Adapter to list view
        ListView lv1 = findViewById(R.id.main_list_view);
        lv1.setAdapter(mainListAdapter);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(() -> new AsyncCaller().execute());

        // Get all data
        new AsyncCaller().execute();
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
            mainListAdapter.addAll(global.getStats().getBreakdowns());
            mainListAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        }
    }


}
