package com.randomappsinc.aroundme.persistence;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.utils.MyApplication;

import io.realm.Realm;

public class DatabaseManager {

    private static DatabaseManager instance;

    public static DatabaseManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized DatabaseManager getSync() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private PlaceTypesDBManager mPlaceTypesDBManager;

    private DatabaseManager() {
        Realm.init(MyApplication.getAppContext());
        mPlaceTypesDBManager = PlaceTypesDBManager.get();
    }

    public void seedPlaceTypes() {
        String[] initialTypes = MyApplication
                .getAppContext()
                .getResources()
                .getStringArray(R.array.initial_options);

        for (String initialType : initialTypes) {
            mPlaceTypesDBManager.addPlaceType(initialType);
        }
    }

    public PlaceTypesDBManager getPlaceTypesDBManager() {
        return mPlaceTypesDBManager;
    }
}
