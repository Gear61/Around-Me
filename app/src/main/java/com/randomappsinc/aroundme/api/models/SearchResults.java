package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {

    private static final double METER_TO_MILES = 0.000621371;

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses;

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

        @SerializedName("rating")
        @Expose
        private double rating;

        @SerializedName("review_count")
        @Expose
        private int reviewCount;

        @SerializedName("is_closed")
        @Expose
        private boolean isClosed;

        @SerializedName("display_phone")
        @Expose
        private String phoneNumber;

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

            @SerializedName("display_address")
            @Expose
            private List<String> displayAddress;

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
                StringBuilder address = new StringBuilder();
                for (int i = 0; i < displayAddress.size(); i++) {
                    if (i > 0) {
                        address.append(", ");
                    }
                    address.append(displayAddress.get(i));
                }
                return address.toString();
            }
        }

        // Distance in meters from the search location
        @SerializedName("distance")
        @Expose
        private double distance;

        Place toPlace() {
            Place place = new Place();
            place.setId(id);
            place.setName(name);
            place.setImageUrl(imageUrl);
            place.setRating(rating);
            place.setReviewCount(reviewCount);
            place.setIsClosed(isClosed);
            place.setPhoneNumber(phoneNumber);
            place.setCity(location.getCity());
            place.setZipCode(location.getZipCode());
            place.setState(location.getState());
            place.setCountry(location.getCountry());
            place.setAddress(location.getAddress());
            place.setLatitude(coordinates.getLatitude());
            place.setLongitude(coordinates.getLongitude());
            place.setDistance(distance * METER_TO_MILES);
            return place;
        }
    }

    public List<Place> getPlaces() {
        List<Place> restaurants = new ArrayList<>();
        for (Business business : businesses) {
            restaurants.add(business.toPlace());
        }
        return restaurants;
    }
}
