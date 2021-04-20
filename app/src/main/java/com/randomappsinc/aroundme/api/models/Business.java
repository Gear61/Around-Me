package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.constants.DistanceUnit;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceCategory;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.utils.DistanceUtils;

import java.util.ArrayList;
import java.util.List;

@Keep
public class Business {

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

    @SerializedName("location")
    @Expose
    private PlaceLocation location;

    // Distance in meters from the place location
    @SerializedName("distance")
    @Expose
    private double distance;

    @SerializedName("categories")
    @Expose
    private List<ApiPlaceCategory> categories;

    Place toPlace() {
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
        place.setAddress(location.getPlaceSearchAddress());
        place.setLatitude(coordinates.getLatitude());
        place.setLongitude(coordinates.getLongitude());
        place.setDistance(PreferencesManager.get().getDistanceUnit().equals(DistanceUnit.MILES)
                ? DistanceUtils.getMilesFromMeters(distance)
                : DistanceUtils.getKilometersFromMeters(distance));
        List<PlaceCategory> placeCategories = new ArrayList<>();
        for (ApiPlaceCategory category : categories) {
            PlaceCategory placeCategory = new PlaceCategory();
            placeCategory.setAlias(category.getAlias());
            placeCategory.setTitle(category.getTitle());
            placeCategories.add(placeCategory);
        }
        place.setCategories(placeCategories);
        return place;
    }
}