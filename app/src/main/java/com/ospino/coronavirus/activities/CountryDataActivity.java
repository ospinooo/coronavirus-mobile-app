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

        viewHolder.textCountry.setText(country.getLocation().getCountryOrRegion());
        viewHolder.imageViewFlag.setImageResource(getResources()
                        .getIdentifier(
                                "drawable/ic_list_country_" + country.getLocation().getIsoCode().toLowerCase(),
                                null,
                                getPackageName()));
    }
}


