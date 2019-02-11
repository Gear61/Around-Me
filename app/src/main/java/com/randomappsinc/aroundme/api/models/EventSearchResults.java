package com.randomappsinc.aroundme.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.aroundme.models.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EventSearchResults {

    @SerializedName("events")
    @Expose
    private List<EventResult> eventResults;

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

        @SerializedName("interested_count")
        @Expose
        private int numInterested;

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

        @SerializedName("latitude")
        @Expose
        private double latitude;

        @SerializedName("longitude")
        @Expose
        private double longitude;

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

        Event toEvent() {
            Event event = new Event();
            event.setId(id);
            event.setImageUrl(imageUrl);
            event.setName(name);
            event.setNumAttending(numAttending);
            event.setNumInterested(numInterested);
            event.setCost(cost);
            event.setCost(costMax);
            event.setDescription(description);
            event.setUrl(url);
            event.setCanceled(isCanceled);
            event.setFree(isFree);
            event.setTicketsUrl(ticketsUrl);
            event.setTimeStart(convertToUnixTime(timeStart));
            event.setTimeEnd(convertToUnixTime(timeEnd));
            event.setCity(location.getCity());
            event.setZipCode(location.getZipCode());
            event.setCountry(location.getCountry());
            event.setState(location.getState());
            event.setAddress(location.getAddress());
            event.setLatitude(latitude);
            event.setLongitude(longitude);
            return event;
        }

        // Yelp time currently looks like this: 2019-03-02T20:00:00-08:00
        private long convertToUnixTime(String originalTime) {
            if (originalTime == null) {
                return 0L;
            }

            // Remove the -HH:MM since I have no clue why that's there
            int lastIndexOfDash = originalTime.lastIndexOf("-");
            String cleanedUpTime = originalTime.substring(0, lastIndexOfDash);

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
            originalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date date;
            try {
                date = originalFormat.parse(cleanedUpTime);
            } catch (ParseException exception) {
                throw new RuntimeException("Incorrect time format: " + originalTime);
            }
            return date.getTime();
        }
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        for (EventResult eventResult : eventResults) {
            if (!eventResult.isCanceled) {
                events.add(eventResult.toEvent());
            }
        }
        return events;
    }
}
