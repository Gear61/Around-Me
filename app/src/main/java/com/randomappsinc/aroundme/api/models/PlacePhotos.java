package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacePhotos {

    @SerializedName("photos")
    @Expose
    private List<String> photoUrls;

    public List<String> getPhotoUrls() {
        return photoUrls;
    }
}
