package com.playposse.udacityrecipe.activity;

import android.app.Activity;
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
    }
}
