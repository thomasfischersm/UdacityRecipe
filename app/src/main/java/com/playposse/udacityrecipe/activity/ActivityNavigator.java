package com.playposse.udacityrecipe.activity;

import android.content.Context;
import android.content.Intent;

import com.playposse.udacityrecipe.R;

/**
 * A utility that helps navigating between activities.
 */
public final class ActivityNavigator {

    private static final String RECIPE_ID_EXTRA = "recipeIdExtra";
    private static final String RECIPE_STEP_INDEX_EXTRA = "recipeStepIndexExtra";
    private static final String RECIPE_NAME_EXTRA = "recipeNameExtra";

    private static final long DEFAULT_ID = -1;
    private static final int DEFAULT_STEP_INDEX = -1;

    private ActivityNavigator() {
    }

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, RecipeListActivity.class));
    }

    static void startRecipeActivity(Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA, recipeId);
        context.startActivity(intent);
    }

    static long getRecipeId(Intent intent) {
        return intent.getLongExtra(RECIPE_ID_EXTRA, DEFAULT_ID);
    }

    static void startRecipeSepActivity(
            Context context,
            long recipeId,
            int recipeStepIndex,
            String recipeName) {

        Intent intent = new Intent(context, RecipeStepActivity.class);
        intent.putExtra(RECIPE_ID_EXTRA, recipeId);
        intent.putExtra(RECIPE_STEP_INDEX_EXTRA, recipeStepIndex);
        intent.putExtra(RECIPE_NAME_EXTRA, recipeName);
        context.startActivity(intent);
    }

    static int getRecipeStepIndex(Intent intent) {
        return intent.getIntExtra(RECIPE_STEP_INDEX_EXTRA, DEFAULT_STEP_INDEX);
    }

    static String getRecipeName(Intent intent) {
        return intent.getStringExtra(RECIPE_NAME_EXTRA);
    }

    private static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
