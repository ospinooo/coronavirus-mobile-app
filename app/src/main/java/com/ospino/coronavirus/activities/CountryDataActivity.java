package com.ospino.coronavirus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ospino.coronavirus.R;
import com.ospino.coronavirus.models.Breakdown;


public class CountryDataActivity extends Activity {

    private static String TAG = "ActivityCountryData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);
        getIncomingIntent();
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
    }

    private void getIncomingIntent(){
        Log.v(TAG, "getIncomingIntent: checking for incoming intents");
        if (getIntent().hasExtra("country")) {
            Log.v(TAG, "getIncomingIntent: found extras");

            Breakdown country = (Breakdown) getIntent().getExtras().get("country");
            inflateActivityView(country);
        }
    }

    private void inflateActivityView(Breakdown country) {
        final ViewHolder viewHolder = new ViewHolder();

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

        viewHolder.textActiveCases = findViewById(R.id.text_active_cases_placeholder);

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


