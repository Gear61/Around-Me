package com.randomappsinc.aroundme.api.models;

import android.text.TextUtils;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class PlaceLocation {

    @SerializedName("address1")
    @Expose
    private String address1;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("zip_code")
    @Expose
    private String zipCode;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("state")
    @Expose
    private String state;

    String getCity() {
        return city;
    }

    String getZipCode() {
        return zipCode;
    }

    String getCountry() {
        return country;
    }

    String getState() {
        return state;
    }

    String getAddress() {
        return address1 + ", " + city;
    }

    String getPlaceSearchAddress() {
        StringBuilder address = new StringBuilder();
        if (!TextUtils.isEmpty(address1)) {
            address.append(address1).append(", ");
        }
        address.append(city);
        return address.toString();
    }
}
