package com.randomappsinc.aroundme.persistence;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.persistence.managers.EventsDBManager;
import com.randomappsinc.aroundme.persistence.managers.PlaceTypesDBManager;
import com.randomappsinc.aroundme.persistence.managers.PlacesDBManager;
import com.randomappsinc.aroundme.utils.MyApplication;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class DatabaseManager {

    private static final int CURRENT_REALM_VERSION = 1;

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
    private PlacesDBManager mPlacesDBManager;
    private EventsDBManager mEventsDBManager;

    private DatabaseManager() {
        Realm.init(MyApplication.getAppContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        mPlaceTypesDBManager = PlaceTypesDBManager.get();
        mPlacesDBManager = PlacesDBManager.get();
        mEventsDBManager = EventsDBManager.get();
    }

    private RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            // Support for categories
            if (oldVersion == 0) {
                RealmObjectSchema placeCategorySchema = schema.create("PlaceCategoryDO")
                        .addField("mAlias", String.class)
                        .addField("mTitle", String.class);

                RealmObjectSchema placeSchema = schema.get("PlaceDO");
                if (placeSchema != null) {
                    placeSchema.addRealmListField("mCategories", placeCategorySchema);
                } else {
                    realm.deleteAll();
                    throw new IllegalStateException("PlaceDO doesn't exist.");
                }
            }
        }
    };

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

    public PlacesDBManager getPlacesDBManager() {
        return mPlacesDBManager;
    }

    public EventsDBManager getEventsDBManager() {
        return mEventsDBManager;
    }
}
