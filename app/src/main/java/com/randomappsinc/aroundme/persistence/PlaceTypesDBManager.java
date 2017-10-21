package com.randomappsinc.aroundme.persistence;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.models.PlaceType;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

public class PlaceTypesDBManager {

    private static PlaceTypesDBManager instance;

    public static PlaceTypesDBManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PlaceTypesDBManager getSync() {
        if (instance == null) {
            instance = new PlaceTypesDBManager();
        }
        return instance;
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public void addPlaceType(final String text) {
        Number number = getRealm().where(PlaceTypeDO.class).findAll().max("placeTypeId");
        final int placeTypeId = number == null ? 1 : number.intValue() + 1;

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                PlaceTypeDO placeTypeDO = new PlaceTypeDO();
                placeTypeDO.setPlaceTypeId(placeTypeId);
                placeTypeDO.setText(text);
                realm.insert(placeTypeDO);
            }
        });
    }

    public List<PlaceType> getPlaceTypes() {
        List<PlaceTypeDO> placeTypeDOs = getRealm()
                .where(PlaceTypeDO.class)
                .findAllSorted("text", Sort.ASCENDING);

        List<PlaceType> placeTypes = new ArrayList<>();
        for (PlaceTypeDO placeTypeDO : placeTypeDOs) {
            placeTypes.add(DBConverter.getPlaceTypeFromDO(placeTypeDO));
        }
        return placeTypes;
    }

    public void updatePlaceType(final PlaceType placeType) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insertOrUpdate(placeType.toPlaceTypeDO());
            }
        });
    }

    public void deletePlaceType(final PlaceType placeType) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                PlaceTypeDO placeTypeDO = placeType.toPlaceTypeDO();
                placeTypeDO.deleteFromRealm();
            }
        });
    }
}
