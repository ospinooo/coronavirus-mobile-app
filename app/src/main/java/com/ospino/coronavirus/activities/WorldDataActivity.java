package com.ospino.coronavirus.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.ospino.coronavirus.R;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WorldDataActivity extends AppCompatActivity {

    protected String TAG = "WorldDataActivity";
    protected Global global = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    }

    private void inflateActivityView(Global global) {
        ViewHolder viewHolder = new ViewHolder();


    }
}
