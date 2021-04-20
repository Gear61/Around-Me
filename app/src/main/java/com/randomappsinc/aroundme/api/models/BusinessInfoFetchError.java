package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class BusinessInfoFetchError {

    @SerializedName("error")
    @Expose
    private BusinessApiError error;

    public String getCode() {
        return error.getCode();
    }
}
