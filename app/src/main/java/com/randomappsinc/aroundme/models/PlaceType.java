package com.randomappsinc.aroundme.models;

import com.randomappsinc.aroundme.persistence.PlaceTypeDO;

public class PlaceType {

    private int mPlaceTypeId;
    private String mText;

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

    public PlaceTypeDO toPlaceTypeDO() {
        PlaceTypeDO placeTypeDO = new PlaceTypeDO();
        placeTypeDO.setPlaceTypeId(mPlaceTypeId);
        placeTypeDO.setText(mText);
        return placeTypeDO;
    }
}
