package com.randomappsinc.aroundme.persistence;

import com.randomappsinc.aroundme.models.PlaceType;

class DBConverter {

    static PlaceType getPlaceTypeFromDO(PlaceTypeDO placeTypeDO) {
        PlaceType placeType = new PlaceType();
        placeType.setPlaceTypeId(placeTypeDO.getPlaceTypeId());
        placeType.setText(placeTypeDO.getText());
        return placeType;
    }
}
