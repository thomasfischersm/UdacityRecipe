package com.playposse.udacityrecipe.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.data.RecipeContentContract.IngredientTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
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
    static final String RECIPE_INGREDIENTS = "com.playposse.udacityrecipe.RECIPE_INGREDIENTS";
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
        Cursor ingredientCursor = contentResolver.query(IngredientTable.CONTENT_URI,
                IngredientTable.COLUMN_NAMES,
                IngredientTable.RECIPE_ID_COLUMN + "=?",
                new String[]{Long.toString(recipeId)},
                null);

        StringBuilder ingredientBuilder;

        try {
            ingredientBuilder = new StringBuilder();
            SmartCursor smartCursor =
                    new SmartCursor(ingredientCursor, IngredientTable.COLUMN_NAMES);

            if ((ingredientCursor != null) && ingredientCursor.moveToFirst()) {
                do {
                    String quantity = smartCursor.getString(IngredientTable.QUANTITY_COLUMN);
                    String measure = smartCursor.getString(IngredientTable.MEASURE_COLUMN);
                    String ingredient = smartCursor.getString(IngredientTable.INGREDIENT_COLUMN);

                    String ingredientStr = context.getString(
                            R.string.whole_ingredient,
                            quantity,
                            measure,
                            ingredient);
                    ingredientBuilder.append(ingredientStr);
                } while (ingredientCursor.moveToNext());
            }
        } finally {
            if (ingredientCursor != null) {
                ingredientCursor.close();
            }
        }


        selectRecipe(context, recipeId, recipeName, ingredientBuilder.toString().trim(), recipePhotoUrl);
    }

    private static void selectRecipe(
            Context context,
            long recipeId,
            String recipeName,
            String recipeIngredients,
            String recipePhotoUrl) {

        Intent intent = new Intent(RECIPE_SELECTED_ACTION);
        intent.putExtra(RECIPE_ID, recipeId);
        intent.putExtra(RECIPE_NAME, recipeName);
        intent.putExtra(RECIPE_INGREDIENTS, recipeIngredients);
        intent.putExtra(RECIPE_PHOTO_URL, recipePhotoUrl);
        context.sendBroadcast(intent);
    }
}
