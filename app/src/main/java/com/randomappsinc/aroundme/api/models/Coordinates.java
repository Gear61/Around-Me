package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Coordinates {

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }
}
