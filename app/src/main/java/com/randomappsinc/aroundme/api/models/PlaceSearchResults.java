package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Place;

import java.util.ArrayList;
import java.util.List;

@Keep
public class PlaceSearchResults {

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses;

    public List<Place> getPlaces() {
        List<Place> restaurants = new ArrayList<>();
        for (Business business : businesses) {
            restaurants.add(business.toPlace());
        }
        return restaurants;
    }
}
