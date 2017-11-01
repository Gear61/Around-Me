package com.randomappsinc.aroundme.models;

import com.randomappsinc.aroundme.persistence.models.PlaceTypeDO;

public class PlaceType {

    private int mPlaceTypeId;
    private String mText;
    private long mTimeLastUpdated;

    public int getId() {
        return mPlaceTypeId;
    }

    public void setPlaceTypeId(int placeTypeId) {
        mPlaceTypeId = placeTypeId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public long getTimeLastUpdated() {
        return mTimeLastUpdated;
    }

    public void setTimeLastUpdated(long timeLastUpdated) {
        mTimeLastUpdated = timeLastUpdated;
    }

    public PlaceTypeDO toPlaceTypeDO() {
        PlaceTypeDO placeTypeDO = new PlaceTypeDO();
        placeTypeDO.setPlaceTypeId(mPlaceTypeId);
        placeTypeDO.setText(mText);
        placeTypeDO.setTimeLastUpdated(mTimeLastUpdated);
        return placeTypeDO;
    }
}
