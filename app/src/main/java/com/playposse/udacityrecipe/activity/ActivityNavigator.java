package com.playposse.udacityrecipe.activity;

import android.content.Context;
import android.content.Intent;

/**
 * A utility that helps navigating between activities.
 */
public final class ActivityNavigator {

    private ActivityNavigator() {}

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, RecipeListActivity.class));
    }
}
