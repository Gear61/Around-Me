package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {

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

        @SerializedName("display_phone")
        @Expose
        private String phoneNumber;

        @SerializedName("coordinates")
        @Expose
        private Coordinates coordinates;

        public class Coordinates {
            @SerializedName("latitude")
            @Expose
            private double latitude;

            @SerializedName("longitude")
            @Expose
            private double longitude;

            public double getLatitude() {
                return latitude;
            }

            public double getLongitude() {
                return longitude;
            }
        }

        @SerializedName("location")
        @Expose
        private Location location;

        public class Location {
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

            public String getCity() {
                return city;
            }

            public String getZipCode() {
                return zipCode;
            }

            public String getCountry() {
                return country;
            }

            public String getState() {
                return state;
            }

            public String getAddress() {
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

        public Place toEstablishment() {
            Place restaurant = new Place();
            restaurant.setId(id);
            restaurant.setName(name);
            restaurant.setImageUrl(imageUrl);
            restaurant.setPhoneNumber(phoneNumber);
            restaurant.setCity(location.getCity());
            restaurant.setZipCode(location.getZipCode());
            restaurant.setState(location.getState());
            restaurant.setCountry(location.getCountry());
            restaurant.setAddress(location.getAddress());
            restaurant.setLatitude(coordinates.getLatitude());
            restaurant.setLongitude(coordinates.getLongitude());
            return restaurant;
        }
    }

    public List<Place> getEstablishments() {
        List<Place> restaurants = new ArrayList<>();
        for (Business business : businesses) {
            restaurants.add(business.toEstablishment());
        }
        return restaurants;
    }
}
