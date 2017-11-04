package com.randomappsinc.aroundme.persistence;

import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.persistence.models.EventDO;
import com.randomappsinc.aroundme.persistence.models.PlaceDO;
import com.randomappsinc.aroundme.persistence.models.PlaceTypeDO;

public class DBConverter {

    public static PlaceType getPlaceTypeFromDO(PlaceTypeDO placeTypeDO) {
        PlaceType placeType = new PlaceType();
        placeType.setPlaceTypeId(placeTypeDO.getPlaceTypeId());
        placeType.setText(placeTypeDO.getText());
        placeType.setTimeLastUpdated(placeTypeDO.getTimeLastUpdated());
        return placeType;
    }

    public static Place getPlaceFromDO(PlaceDO placeDO) {
        Place place = new Place();
        place.setId(placeDO.getId());
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

    public static Event getEventFromDO(EventDO eventDO) {
        Event event = new Event();
        event.setId(eventDO.getId());
        event.setImageUrl(eventDO.getImageUrl());
        event.setName(eventDO.getName());
        event.setNumAttending(eventDO.getNumAttending());
        event.setNumInterested(eventDO.getNumInterested());
        event.setCost(eventDO.getCost());
        event.setCostMax(eventDO.getCostMax());
        event.setDescription(eventDO.getDescription());
        event.setUrl(eventDO.getUrl());
        event.setCanceled(eventDO.isCanceled());
        event.setFree(eventDO.isFree());
        event.setTicketsUrl(eventDO.getTicketsUrl());
        event.setTimeStart(eventDO.getTimeStart());
        event.setTimeEnd(eventDO.getTimeEnd());
        event.setCity(eventDO.getCity());
        event.setZipCode(eventDO.getZipCode());
        event.setCountry(eventDO.getCountry());
        event.setState(eventDO.getState());
        event.setAddress(eventDO.getAddress());
        event.setLatitude(eventDO.getLatitude());
        event.setLongitude(eventDO.getLongitude());
        event.setIsFavorited(eventDO.isFavorited());
        return event;
    }
}
