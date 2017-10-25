package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.service.RecipeDownloadService;

/**
 * The launcher {@link Activity}. It waits until the {@link RecipeDownloadService} is done loading
 * recipes and then forwards the user to the real main activity.
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);


        // Cache movie data.
        Intent intent = new Intent(getApplicationContext(), RecipeDownloadService.class);
        intent.putExtra(
                RecipeDownloadService.RESULT_RECEIVER_PARAM,
                new ResultReceiver(new Handler()) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        finish();
                        ActivityNavigator.startMainActivity(getApplicationContext());
                    }
                });
        startService(intent);
    }
}
