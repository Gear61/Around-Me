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

    private static final int CURRENT_REALM_VERSION = 2;

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

    private PlaceTypesDBManager placeTypesDBManager;
    private PlacesDBManager placesDBManager;
    private EventsDBManager eventsDBManager;

    private DatabaseManager() {
        Realm.init(MyApplication.getAppContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        placeTypesDBManager = PlaceTypesDBManager.get();
        placesDBManager = PlacesDBManager.get();
        eventsDBManager = EventsDBManager.get();
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
                oldVersion++;
            }

            // Get rid of Hungarian
            if (oldVersion == 1) {
                RealmObjectSchema eventSchema = schema.get("EventDO");
                if (eventSchema != null) {
                    eventSchema.renameField("mId", "id")
                            .renameField("mImageUrl", "imageUrl")
                            .renameField("mName", "name")
                            .renameField("mNumAttending", "numAttending")
                            .renameField("mNumInterested", "numInterested")
                            .renameField("mCost", "cost")
                            .renameField("mCostMax", "costMax")
                            .renameField("mDescription", "description")
                            .renameField("mUrl", "url")
                            .renameField("mIsCanceled", "isCanceled")
                            .renameField("mIsFree", "isFree")
                            .renameField("mTicketsUrl", "ticketsUrl")
                            .renameField("mTimeStart", "timeStart")
                            .renameField("mTimeEnd", "timeEnd")
                            .renameField("mCity", "city")
                            .renameField("mZipCode", "zipCode")
                            .renameField("mCountry", "country")
                            .renameField("mState", "state")
                            .renameField("mAddress", "address")
                            .renameField("mLatitude", "latitude")
                            .renameField("mLongitude", "longitude")
                            .renameField("mIsFavorited", "isFavorited");
                } else {
                    realm.deleteAll();
                    throw new IllegalStateException("EventDO doesn't exist.");
                }

                RealmObjectSchema placeCategorySchema = schema.get("PlaceCategoryDO");
                if (placeCategorySchema != null) {
                    placeCategorySchema.renameField("mAlias", "alias")
                            .renameField("mTitle", "title");
                } else {
                    realm.deleteAll();
                    throw new IllegalStateException("PlaceCategoryDO doesn't exist.");
                }

                RealmObjectSchema placeSchema = schema.get("PlaceDO");
                if (placeSchema != null) {
                    placeSchema.renameField("mId", "id")
                            .renameField("mName", "name")
                            .renameField("mImageUrl", "imageUrl")
                            .renameField("mUrl", "url")
                            .renameField("mRating", "rating")
                            .renameField("mReviewCount", "reviewCount")
                            .renameField("mPhoneNumber", "phoneNumber")
                            .renameField("mPrice", "price")
                            .renameField("mCity", "city")
                            .renameField("mZipCode", "zipCode")
                            .renameField("mCountry", "country")
                            .renameField("mState", "state")
                            .renameField("mAddress", "address")
                            .renameField("mLatitude", "latitude")
                            .renameField("mLongitude", "longitude")
                            .renameField("mIsFavorited", "isFavorited")
                            .renameField("mCategories", "categories");
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
            placeTypesDBManager.addPlaceType(initialType);
        }
    }

    public PlaceTypesDBManager getPlaceTypesDBManager() {
        return placeTypesDBManager;
    }

    public PlacesDBManager getPlacesDBManager() {
        return placesDBManager;
    }

    public EventsDBManager getEventsDBManager() {
        return eventsDBManager;
    }
}
