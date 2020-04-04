
package com.ospino.coronavirus.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Country {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("updatedDateTime")
    @Expose
    private String updatedDateTime;
    @SerializedName("stats")
    @Expose
    private Stats stats;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(updatedDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    public String getUpdatedDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

}
