package com.randomappsinc.aroundme.persistence;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.utils.MyApplication;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
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

    private DatabaseManager() {
        Realm.init(MyApplication.getAppContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(CURRENT_REALM_VERSION)
                .migration(migration)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        mPlaceTypesDBManager = PlaceTypesDBManager.get();
    }

    private final RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            // Support for favoriting places and events
            if (oldVersion == 0) {
                schema.create("PlaceDO")
                        .addField("mId", String.class)
                        .addPrimaryKey("mId")
                        .addField("mName", String.class)
                        .addField("mImageUrl", String.class)
                        .addField("mUrl", String.class)
                        .addField("mRating", double.class)
                        .addField("mReviewCount", int.class)
                        .addField("mPhoneNumber", String.class)
                        .addField("mPrice", String.class)
                        .addField("mCity", String.class)
                        .addField("mZipCode", String.class)
                        .addField("mCountry", String.class)
                        .addField("mState", String.class)
                        .addField("mAddress", String.class)
                        .addField("mLatitude", double.class)
                        .addField("mLongitude", double.class)
                        .addField("mIsFavorited", boolean.class);

                schema.create("EventDO")
                        .addField("mId", String.class)
                        .addPrimaryKey("mId")
                        .addField("mImageUrl", String.class)
                        .addField("mName", String.class)
                        .addField("mNumAttending", int.class)
                        .addField("mNumInterested", int.class)
                        .addField("mCost", double.class)
                        .addField("mCostMax", double.class)
                        .addField("mDescription", String.class)
                        .addField("mUrl", String.class)
                        .addField("mIsCanceled", boolean.class)
                        .addField("mIsFree", boolean.class)
                        .addField("mTicketsUrl", String.class)
                        .addField("mTimeStart", String.class)
                        .addField("mTimeEnd", String.class)
                        .addField("mCity", String.class)
                        .addField("mZipCode", String.class)
                        .addField("mCountry", String.class)
                        .addField("mState", String.class)
                        .addField("mAddress", String.class)
                        .addField("mLatitude", double.class)
                        .addField("mLongitude", double.class)
                        .addField("mIsFavorited", boolean.class);
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
}
