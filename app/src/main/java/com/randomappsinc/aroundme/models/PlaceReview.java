package com.randomappsinc.aroundme.models;

import com.randomappsinc.aroundme.utils.TimeUtils;

public class PlaceReview {

    private double rating;
    private String text;
    private long timeCreated;
    private String url;
    private String username;
    private String userImageUrl;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getText() {
        return "\"" + text + "\"";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getTimeCreatedText() {
        return TimeUtils.getReviewDateTime(timeCreated);
    }
}
