package com.randomappsinc.aroundme.persistence;

import com.randomappsinc.aroundme.models.PlaceType;

public class DBConverter {

    public static PlaceType getPlaceTypeFromDO(PlaceTypeDO placeTypeDO) {
        PlaceType placeType = new PlaceType();
        placeType.setPlaceTypeId(placeTypeDO.getPlaceTypeId());
        placeType.setText(placeType.getText());
        return placeType;
    }
}
