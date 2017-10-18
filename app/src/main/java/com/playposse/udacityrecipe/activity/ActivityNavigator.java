package com.playposse.udacityrecipe.activity;

import android.content.Context;
import android.content.Intent;

/**
 * A utility that helps navigating between activities.
 */
public final class ActivityNavigator {

    private static final String RECIPE_ID_EXTRA = "recipeIdExtra";

    private static final long DEFAULT_ID = -1;

    private ActivityNavigator() {}

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, RecipeListActivity.class));
    }

    public static void startRecipeActivity(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA, recipeId);
        context.startActivity(intent);
    }

    static long getRecipeId(Intent intent) {
        return intent.getLongExtra(RECIPE_ID_EXTRA, DEFAULT_ID);
    }
}
