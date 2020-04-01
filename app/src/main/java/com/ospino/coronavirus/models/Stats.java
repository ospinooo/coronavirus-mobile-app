
package com.ospino.coronavirus.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats implements Serializable {

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
    @SerializedName("history")
    @Expose
    private List<History> history = null;
    @SerializedName("breakdowns")
    @Expose
    private List<Breakdown> breakdowns = null;

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

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public List<Breakdown> getBreakdowns() {
        return breakdowns;
    }

    public void setBreakdowns(List<Breakdown> breakdowns) {
        this.breakdowns = breakdowns;
    }

}
