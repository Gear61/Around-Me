package com.randomappsinc.aroundme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("rating")
    @Expose
    private double rating;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("time_created")
    @Expose
    private String timeCreated;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("user")
    @Expose
    private User user;

    public class User {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public double getRating() {
        return rating;
    }

    public String getText() {
        return "\"" + text + "\"";
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getUrl() {
        return url;
    }

    public User getUser() {
        return user;
    }
}
