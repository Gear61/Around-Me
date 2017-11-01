package com.randomappsinc.aroundme.persistence;

import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.persistence.models.PlaceDO;
import com.randomappsinc.aroundme.persistence.models.PlaceTypeDO;

class DBConverter {

    static PlaceType getPlaceTypeFromDO(PlaceTypeDO placeTypeDO) {
        PlaceType placeType = new PlaceType();
        placeType.setPlaceTypeId(placeTypeDO.getPlaceTypeId());
        placeType.setText(placeTypeDO.getText());
        placeType.setTimeLastUpdated(placeTypeDO.getTimeLastUpdated());
        return placeType;
    }

    static Place getPlaceFromDO(PlaceDO placeDO) {
        Place place = new Place();
        place.setName(placeDO.getName());
        place.setImageUrl(placeDO.getImageUrl());
        place.setUrl(placeDO.getUrl());
        place.setRating(placeDO.getRating());
        place.setReviewCount(placeDO.getReviewCount());
        place.setPhoneNumber(placeDO.getPhoneNumber());
        place.setPrice(placeDO.getPrice());
        place.setCity(placeDO.getCity());
        place.setZipCode(placeDO.getZipCode());
        place.setCountry(placeDO.getCountry());
        place.setState(placeDO.getState());
        place.setAddress(placeDO.getAddress());
        place.setLatitude(placeDO.getLatitude());
        place.setLongitude(placeDO.getLongitude());
        place.setIsFavorited(placeDO.isFavorited());
        return place;
    }
}
