
package com.ospino.coronavirus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location_ implements Serializable {

    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("countryOrRegion")
    @Expose
    private String countryOrRegion;
    @SerializedName("provinceOrState")
    @Expose
    private Object provinceOrState;
    @SerializedName("county")
    @Expose
    private Object county;
    @SerializedName("isoCode")
    @Expose
    private String isoCode;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public String getCountryOrRegion() {
        return countryOrRegion;
    }

    public void setCountryOrRegion(String countryOrRegion) {
        this.countryOrRegion = countryOrRegion;
    }

    public Object getProvinceOrState() {
        return provinceOrState;
    }

    public void setProvinceOrState(Object provinceOrState) {
        this.provinceOrState = provinceOrState;
    }

    public Object getCounty() {
        return county;
    }

    public void setCounty(Object county) {
        this.county = county;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
