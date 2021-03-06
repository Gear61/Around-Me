package com.randomappsinc.aroundme.persistence.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PlaceTypeDO extends RealmObject {

    @PrimaryKey
    private int placeTypeId;

    private String text;
    private long timeLastUpdated;

    public int getPlaceTypeId() {
        return placeTypeId;
    }

    public void setPlaceTypeId(int placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimeLastUpdated() {
        return timeLastUpdated;
    }

    public void setTimeLastUpdated(long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }
}
