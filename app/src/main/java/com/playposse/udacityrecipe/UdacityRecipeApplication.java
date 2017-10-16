package com.playposse.udacityrecipe;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import com.playposse.udacityrecipe.activity.ActivityNavigator;
import com.playposse.udacityrecipe.data.RecipeDatabaseHelper;
import com.playposse.udacityrecipe.service.RecipeDownloadService;

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

        // Cache movie data.
        Intent intent = new Intent(getApplicationContext(), RecipeDownloadService.class);
        intent.putExtra(
                RecipeDownloadService.RESULT_RECEIVER_PARAM,
                new ResultReceiver(new Handler()) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        ActivityNavigator.startMainActivity(getApplicationContext());
                    }
                });
        startService(intent);
    }
}
