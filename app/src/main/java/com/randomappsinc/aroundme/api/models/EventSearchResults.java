package com.randomappsinc.aroundme.api.models;

import androidx.annotation.Keep;

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

@Keep
public class EventSearchResults {

    @SerializedName("events")
    @Expose
    private List<EventResult> eventResults;

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
