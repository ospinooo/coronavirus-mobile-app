
package com.ospino.coronavirus.models;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Breakdown implements Serializable {

    @SerializedName("location")
    @Expose
    private Location_ location;
    @SerializedName("totalConfirmedCases")
    @Expose
    private Integer totalConfirmedCases;
    @SerializedName("newlyConfirmedCases")
    @Expose
    private Integer newlyConfirmedCases;
    @SerializedName("totalDeaths")
    @Expose
    private Integer totalDeaths;
    @SerializedName("newDeaths")
    @Expose
    private Integer newDeaths;
    @SerializedName("totalRecoveredCases")
    @Expose
    private Integer totalRecoveredCases;
    @SerializedName("newlyRecoveredCases")
    @Expose
    private Integer newlyRecoveredCases;

    public Location_ getLocation() {
        return location;
    }

    public void setLocation(Location_ location) {
        this.location = location;
    }

    public Integer getTotalConfirmedCases() {
        return totalConfirmedCases;
    }

    public void setTotalConfirmedCases(Integer totalConfirmedCases) {
        this.totalConfirmedCases = totalConfirmedCases;
    }

    public Integer getNewlyConfirmedCases() {
        return newlyConfirmedCases;
    }

    public void setNewlyConfirmedCases(Integer newlyConfirmedCases) {
        this.newlyConfirmedCases = newlyConfirmedCases;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(Integer newDeaths) {
        this.newDeaths = newDeaths;
    }

    public Integer getTotalRecoveredCases() {
        return totalRecoveredCases;
    }

    public void setTotalRecoveredCases(Integer totalRecoveredCases) {
        this.totalRecoveredCases = totalRecoveredCases;
    }

    public Integer getNewlyRecoveredCases() {
        return newlyRecoveredCases;
    }

    public void setNewlyRecoveredCases(Integer newlyRecoveredCases) {
        this.newlyRecoveredCases = newlyRecoveredCases;
    }

    public Integer getActiveCases() {
        return getTotalConfirmedCases() - (getTotalDeaths() + getTotalRecoveredCases());
    }
}
