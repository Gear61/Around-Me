package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class BusinessApiError {

    @SerializedName("code")
    @Expose
    private String code;

    String getCode() {
        return code;
    }
}
