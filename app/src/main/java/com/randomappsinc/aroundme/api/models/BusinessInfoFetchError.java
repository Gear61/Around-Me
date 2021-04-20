package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessInfoFetchError {

    @SerializedName("error")
    @Expose
    private BusinessApiError error;

    public String getCode() {
        return error.getCode();
    }
}
