package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceCategory;

import java.util.ArrayList;
import java.util.List;

public class PlacePhotos {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("rating")
    @Expose
    private double rating;

    @SerializedName("review_count")
    @Expose
    private int reviewCount;

    @SerializedName("display_phone")
    @Expose
    private String phoneNumber;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;

    class Coordinates {
        @SerializedName("latitude")
        @Expose
        private double latitude;

        @SerializedName("longitude")
        @Expose
        private double longitude;

        double getLatitude() {
            return latitude;
        }

        double getLongitude() {
            return longitude;
        }
    }

    @SerializedName("location")
    @Expose
    private Location location;

    class Location {
        @SerializedName("address1")
        @Expose
        private String address1;

        @SerializedName("city")
        @Expose
        private String city;

        @SerializedName("zip_code")
        @Expose
        private String zipCode;

        @SerializedName("country")
        @Expose
        private String country;

        @SerializedName("state")
        @Expose
        private String state;

        String getCity() {
            return city;
        }

        String getZipCode() {
            return zipCode;
        }

        String getCountry() {
            return country;
        }

        String getState() {
            return state;
        }

        String getAddress() {
            return address1 + ", " + city;
        }
    }

    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    class Category {
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

    public Place getPlace() {
        Place place = new Place();
        place.setId(id);
        place.setName(name);
        place.setImageUrl(imageUrl);
        place.setUrl(url);
        place.setRating(rating);
        place.setReviewCount(reviewCount);
        place.setPhoneNumber(phoneNumber);
        place.setPrice(price);
        place.setCity(location.getCity());
        place.setZipCode(location.getZipCode());
        place.setState(location.getState());
        place.setCountry(location.getCountry());
        place.setAddress(location.getAddress());
        place.setLatitude(coordinates.getLatitude());
        place.setLongitude(coordinates.getLongitude());
        List<PlaceCategory> placeCategories = new ArrayList<>();
        for (Category category : categories) {
            PlaceCategory placeCategory = new PlaceCategory();
            placeCategory.setAlias(category.getAlias());
            placeCategory.setTitle(category.getTitle());
            placeCategories.add(placeCategory);
        }
        place.setCategories(placeCategories);
        return place;
    }

    @SerializedName("photos")
    @Expose
    private List<String> photoUrls;

    public List<String> getPhotoUrls() {
        return photoUrls;
    }
}
