package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Review;

import java.util.List;

public class PlaceReviews {

    @SerializedName("reviews")
    @Expose
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }
}
