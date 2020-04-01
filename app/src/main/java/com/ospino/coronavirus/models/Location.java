
package com.ospino.coronavirus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {

    @SerializedName("long")
    @Expose
    private Object _long;
    @SerializedName("countryOrRegion")
    @Expose
    private Object countryOrRegion;
    @SerializedName("provinceOrState")
    @Expose
    private Object provinceOrState;
    @SerializedName("county")
    @Expose
    private Object county;
    @SerializedName("isoCode")
    @Expose
    private Object isoCode;
    @SerializedName("lat")
    @Expose
    private Object lat;

    public Object getLong() {
        return _long;
    }

    public void setLong(Object _long) {
        this._long = _long;
    }

    public Object getCountryOrRegion() {
        return countryOrRegion;
    }

    public void setCountryOrRegion(Object countryOrRegion) {
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

    public Object getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(Object isoCode) {
        this.isoCode = isoCode;
    }

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

}
