package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class EventLocation {

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

    @SerializedName("display_address")
    @Expose
    private List<String> displayAddress;

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
        StringBuilder address = new StringBuilder();
        for (int i = 0; i < displayAddress.size(); i++) {
            if (i > 0) {
                address.append(", ");
            }
            address.append(displayAddress.get(i));
        }
        return address.toString();
    }
}