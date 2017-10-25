package com.playposse.udacityrecipe;

import android.app.Application;

import com.playposse.udacityrecipe.data.RecipeDatabaseHelper;

/**
 * Implementation of the {@link Application}.
 */
public class UdacityRecipeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Start with a fresh database when running for debug.
        if (BuildConfig.DEBUG) {
            getApplicationContext().deleteDatabase(RecipeDatabaseHelper.DB_NAME);
        }
    }
}
