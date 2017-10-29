package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResults {

    @SerializedName("events")
    @Expose
    private List<EventResult> businesses;

    public class EventResult {
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("attending_count")
        @Expose
        private int numAttending;

        @SerializedName("cost")
        @Expose
        private double cost;

        @SerializedName("cost_max")
        @Expose
        private double costMax;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("event_site_url")
        @Expose
        private String url;

        @SerializedName("interested_count")
        @Expose
        private int numInterested;

        @SerializedName("is_canceled")
        @Expose
        private boolean isCanceled;

        @SerializedName("is_free")
        @Expose
        private boolean isFree;

        @SerializedName("tickets_url")
        @Expose
        private String ticketsUrl;

        @SerializedName("time_start")
        @Expose
        private String timeStart;

        @SerializedName("time_end")
        @Expose
        private String timeEnd;

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
    }
}
