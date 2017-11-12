package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessInfoFetchError {

    @SerializedName("error")
    @Expose
    private Error error;

    class Error {
        @SerializedName("code")
        @Expose
        private String code;

        String getCode() {
            return code;
        }
    }

    public String getCode() {
        return error.getCode();
    }
}
