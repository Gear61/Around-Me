package com.randomappsinc.aroundme.utils;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.randomappsinc.aroundme.api.RestClient;
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.persistence.PreferencesManager;

public final class MyApplication extends Application {

    private static Context mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule());
        mInstance = getApplicationContext();

        if (PreferencesManager.get().getBearerToken().isEmpty()) {
            RestClient.getInstance().refreshToken();
        }

        if (PreferencesManager.get().isFirstAppOpen()) {
            DatabaseManager.get().seedPlaceTypes();
        }
    }

    public static Context getAppContext() {
        return mInstance;
    }
}
