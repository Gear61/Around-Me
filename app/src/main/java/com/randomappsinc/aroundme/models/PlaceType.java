package com.randomappsinc.aroundme.models;

import com.randomappsinc.aroundme.persistence.models.PlaceTypeDO;

public class PlaceType {

    private int placeTypeId;
    private String text;
    private long timeLastUpdated;

    public int getId() {
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

    public void setTimeLastUpdated(long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }

    public PlaceTypeDO toPlaceTypeDO() {
        PlaceTypeDO placeTypeDO = new PlaceTypeDO();
        placeTypeDO.setPlaceTypeId(placeTypeId);
        placeTypeDO.setText(text);
        placeTypeDO.setTimeLastUpdated(timeLastUpdated);
        return placeTypeDO;
    }
}
