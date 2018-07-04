package com.randomappsinc.aroundme.persistence.managers;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.models.Place;
import com.randomappsinc.aroundme.persistence.DBConverter;
import com.randomappsinc.aroundme.persistence.models.PlaceDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class PlacesDBManager {

    private static PlacesDBManager instance;

    public static PlacesDBManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PlacesDBManager getSync() {
        if (instance == null) {
            instance = new PlacesDBManager();
        }
        return instance;
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public void addFavorite(final Place place) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insert(place.toPlaceDO());
            }
        });
    }

    public void removeFavorite(final Place place) {
        final PlaceDO placeDO = getRealm()
                .where(PlaceDO.class)
                .equalTo("id", place.getId())
                .findFirst();

        if (placeDO == null) {
            return;
        }

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                placeDO.deleteFromRealm();
            }
        });
    }

    public List<Place> getFavoritePlaces() {
        List<PlaceDO> placeDOs = getRealm()
                .where(PlaceDO.class)
                .equalTo("isFavorited", true)
                .findAll();

        List<Place> favorites = new ArrayList<>();
        for (PlaceDO placeDO : placeDOs) {
            favorites.add(DBConverter.getPlaceFromDO(placeDO));
        }
        return favorites;
    }

    public boolean isPlaceFavorited(Place place) {
        PlaceDO placeDO = getRealm()
                .where(PlaceDO.class)
                .equalTo("id", place.getId())
                .findFirst();

        return placeDO != null && placeDO.isFavorited();
    }

    public void updateFavorite(final Place place) {
        if (!isPlaceFavorited(place)) {
            return;
        }

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                place.setIsFavorited(true);
                realm.insertOrUpdate(place.toPlaceDO());
            }
        });
    }
}
