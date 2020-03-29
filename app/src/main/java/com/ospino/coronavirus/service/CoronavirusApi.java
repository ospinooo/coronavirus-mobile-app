package com.ospino.coronavirus.service;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ospino.coronavirus.MainActivity;
import com.ospino.coronavirus.adapters.MainListAdapter;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Global;
import com.ospino.coronavirus.repositories.CoronavirusRepository;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CoronavirusApi {

    private static final String ENDPOINT = "https://api.smartable.ai/coronavirus/stats/";

    private static final String KEY = "4f4b38ae34654181b80276626bc6b548";

    private static CoronavirusApi instance = null;

    private OkHttpClient client = null;

    /**
     * Constructor
     */
    private CoronavirusApi(){
         client = new OkHttpClient();
    }

    /**
     * Singleton
     * @return
     */
    public static CoronavirusApi getInstance(){
        if (instance == null){
            instance = new CoronavirusApi();
        }
        return instance;
    }

    /**
     * Get global data
     * Get Raw Global object from the api.
     * @return
     */
    public Global getGlobalData() throws IOException {
        final Request request = new Request.Builder()
                .url(getGlobalURI())
                .method("GET", null)
                .addHeader("Subscription-Key", getKey())
                .build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        Global global = gson.fromJson(response.body().string(), new TypeToken<Global>() {}.getType());
        return global;
    }

    /**
     * getGlobalDataFilteredNotNullIso
     * Filter countries whose iso is not null
     * @return
     * @throws IOException
     */
    public Global getGlobalDataFilteredNotNullIso() throws IOException {
        Global global = this.getGlobalData();
        global.getStats().setBreakdowns(global.getStats().getBreakdowns().stream()
                .filter(breakdown -> breakdown.getLocation().getIsoCode() != null).collect(Collectors.toList()));
        return global;
    }


    public String getKey () {
        return this.KEY;
    }

    public String getCountryURI(String isoCountryName) {
        return this.ENDPOINT + isoCountryName;
    }

    public String getGlobalURI() {
        return this.ENDPOINT + "global";
    }
}
