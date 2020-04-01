
package com.ospino.coronavirus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Observable;

public class Global extends Observable implements Serializable {

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

    public String getUpdatedDateTime() {
        return updatedDateTime;
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
