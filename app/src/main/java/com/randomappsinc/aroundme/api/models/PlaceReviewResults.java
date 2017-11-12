package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.PlaceReview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PlaceReviewResults {

    @SerializedName("reviews")
    @Expose
    private List<PlaceReviewResult> reviews;

    public class PlaceReviewResult {

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

            String getName() {
                return name;
            }

            String getImageUrl() {
                return imageUrl;
            }
        }

        private long convertToUnixTime(String originalTime) {
            if (originalTime == null) {
                return 0L;
            }

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            originalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date date;
            try {
                date = originalFormat.parse(originalTime);
            } catch (ParseException exception) {
                throw new RuntimeException("Incorrect time format: " + originalTime);
            }
            return date.getTime();
        }

        PlaceReview toPlaceReview() {
            PlaceReview placeReview = new PlaceReview();
            placeReview.setRating(rating);
            placeReview.setText(text);
            placeReview.setTimeCreated(convertToUnixTime(timeCreated));
            placeReview.setUrl(url);
            placeReview.setUsername(user.getName());
            placeReview.setUserImageUrl(user.getImageUrl());
            return placeReview;
        }
    }

    public List<PlaceReview> getReviews() {
        List<PlaceReview> placeReviews = new ArrayList<>();
        for (PlaceReviewResult reviewResult : reviews) {
            placeReviews.add(reviewResult.toPlaceReview());
        }
        return placeReviews;
    }
}
