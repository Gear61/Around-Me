package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class ApiPlaceCategory {

    @SerializedName("alias")
    @Expose
    private String alias;

    @SerializedName("title")
    @Expose
    private String title;

    String getAlias() {
        return alias;
    }

    String getTitle() {
        return title;
    }
}