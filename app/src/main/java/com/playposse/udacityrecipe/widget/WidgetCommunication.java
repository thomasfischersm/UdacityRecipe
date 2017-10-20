package com.playposse.udacityrecipe.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;
import com.playposse.udacityrecipe.data.RecipePhotoLibrary;
import com.playposse.udacityrecipe.util.SmartCursor;

/**
 * A class that sends updates to the {@link RecipeWidget}.
 */
public final class WidgetCommunication {

    private static final String LOG_TAG = WidgetCommunication.class.getSimpleName();

    static final String RECIPE_SELECTED_ACTION =
            "com.playposse.udacityrecipe.RECIPE_SELECTED";
    static final String RECIPE_ID = "com.playposse.udacityrecipe.RECIPE_ID";
    static final String RECIPE_NAME = "com.playposse.udacityrecipe.RECIPE_NAME";
    static final String RECIPE_STEPS = "com.playposse.udacityrecipe.RECIPE_STEPS";
    static final String RECIPE_PHOTO_URL = "com.playposse.udacityrecipe.RECIPE_PHOTO_URL";

    public static void selectRecipe(Context context, long recipeId) {
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            Log.e(LOG_TAG, "selectRecipe: Content resolver was null!");
            return;
        }

        // Get recipe information.
        Cursor recipeCursor = contentResolver.query(
                RecipeTable.CONTENT_URI,
                RecipeTable.COLUMN_NAMES,
                RecipeTable.ID_COLUMN + "=?",
                new String[]{Long.toString(recipeId)},
                null);

        if ((recipeCursor == null) || !recipeCursor.moveToFirst()) {
            // Something went wrong. Stop processing!
            Log.e(LOG_TAG, "selectRecipe: Failed to load the recipe: " + recipeId);
            return;
        }

        String recipeName;
        String recipePhotoUrl;
        try {
            SmartCursor recipeSmartCursor = new SmartCursor(recipeCursor, RecipeTable.COLUMN_NAMES);
            recipeName = recipeSmartCursor.getString(RecipeTable.NAME_COLUMN);
            recipePhotoUrl = recipeSmartCursor.getString(RecipeTable.IMAGE_COLUMN);
        } finally {
            recipeCursor.close();
        }

        // Get the recipe steps.
        Cursor stepCursor = contentResolver.query(StepTable.CONTENT_URI,
                StepTable.COLUMN_NAMES,
                StepTable.RECIPE_ID_COLUMN + "=?",
                new String[]{Long.toString(recipeId)},
                StepTable.STEP_INDEX_COLUMN + " asc");

        StringBuilder stepsBuilder;

        try {
            stepsBuilder = new StringBuilder();
            SmartCursor smartCursor = new SmartCursor(stepCursor, StepTable.COLUMN_NAMES);

            if ((stepCursor != null) && stepCursor.moveToFirst()) {
                do {
                    int stepIndex = smartCursor.getInt(StepTable.STEP_INDEX_COLUMN);
                    String shortDescription =
                            smartCursor.getString(StepTable.SHORT_DESCRIPTION_COLUMN);
                    String stepStr = context.getString(
                            R.string.whole_short_step_for_widget,
                            stepIndex,
                            shortDescription);
                    stepsBuilder.append(stepStr);
                } while (stepCursor.moveToNext());
            }
        } finally {
            if (stepCursor != null) {
                stepCursor.close();
            }
        }


        selectRecipe(context, recipeId, recipeName, stepsBuilder.toString().trim(), recipePhotoUrl);
    }

    private static void selectRecipe(
            Context context,
            long recipeId,
            String recipeName,
            String recipeSteps,
            String recipePhotoUrl) {

        Intent intent = new Intent(RECIPE_SELECTED_ACTION);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(RECIPE_NAME, recipeName);
        intent.putExtra(RECIPE_STEPS, recipeSteps);
        intent.putExtra(RECIPE_PHOTO_URL, recipePhotoUrl);
        context.sendBroadcast(intent);
    }
}
