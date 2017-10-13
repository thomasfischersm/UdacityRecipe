package com.playposse.udacityrecipe.service;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.playposse.udacityrecipe.BuildConfig;
import com.playposse.udacityrecipe.data.RecipeContentContract.IngredientTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;
import com.playposse.udacityrecipe.data.RecipeDatabaseHelper;
import com.playposse.udacityrecipe.service.retrofit.Ingredient;
import com.playposse.udacityrecipe.service.retrofit.Recipe;
import com.playposse.udacityrecipe.service.retrofit.RecipeService;
import com.playposse.udacityrecipe.service.retrofit.Step;
import com.playposse.udacityrecipe.util.DatabaseDumper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An {@link IntentService} that downloads the recipe data from the internet.
 */
public class RecipeDownloadService extends IntentService {

    private static final String LOG_TAG = RecipeDownloadService.class.getSimpleName();

    private static final String SERVICE_NAME = "RecipeDownloadService";
    public static final String RECIPE_SERVICE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    public RecipeDownloadService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        try {
            RecipeService remoteService = retrofit.create(RecipeService.class);
            Call<List<Recipe>> recipeCall = remoteService.get();
            Response<List<Recipe>> response = recipeCall.execute();
            Log.i(LOG_TAG, "onHandleIntent: Got recipes: " + response.body().size());

            importAll(response.body());
        } catch (IOException ex) {
            Log.e(LOG_TAG, "onHandleIntent: Failed to download recipes.", ex);
        }

        // Dump the database to the log.
        if (BuildConfig.DEBUG) {
            RecipeDatabaseHelper databaseHelper = new RecipeDatabaseHelper(getApplicationContext());
            DatabaseDumper.dumpTables(databaseHelper);
            databaseHelper.close();
        }
    }

    private void importAll(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            importRecipe(recipe);

            for (Ingredient ingredient : recipe.getIngredients()) {
                importIngredient(recipe.getId(), ingredient);
            }

            for (Step step : recipe.getSteps()) {
                importStep(recipe.getId(), step);
            }
        }
    }

    private long importRecipe(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeTable.ID_COLUMN, recipe.getId());
        values.put(RecipeTable.NAME_COLUMN, recipe.getName());
        values.put(RecipeTable.SERVINGS_COLUMN, recipe.getServings());
        values.put(RecipeTable.IMAGE_COLUMN, recipe.getImage());

        Uri uri = getContentResolver().insert(RecipeTable.CONTENT_URI, values);
        long recipeId = ContentUris.parseId(uri);

        if (recipe.getId() != recipeId) {
            Log.e(LOG_TAG, "importRecipe: Something went wrong with the recipeId: "
                    + recipe.getId() + " vs. " + recipeId);
        }

        return recipeId;
    }

    private void importIngredient(long recipeId, Ingredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(IngredientTable.RECIPE_ID_COLUMN, recipeId);
        values.put(IngredientTable.QUANTITY_COLUMN, ingredient.getQuantity());
        values.put(IngredientTable.MEASURE_COLUMN, ingredient.getMeasure());
        values.put(IngredientTable.INGREDIENT_COLUMN, ingredient.getIngredient());

        getContentResolver().insert(IngredientTable.CONTENT_URI, values);
    }

    private void importStep(long recipeId, Step step) {
        ContentValues values = new ContentValues();
        values.put(StepTable.RECIPE_ID_COLUMN, recipeId);
        values.put(StepTable.STEP_INDEX_COLUMN, step.getId());
        values.put(StepTable.SHORT_DESCRIPTION_COLUMN, step.getShortDescription());
        values.put(StepTable.DESCRIPTION_COLUMN, step.getDescription());
        values.put(StepTable.VIDEO_URL_COLUMN, step.getVideoURL());
        values.put(StepTable.THUMBNAIL_COLUMN, step.getThumbnailURL());

        getContentResolver().insert(StepTable.CONTENT_URI, values);
    }
}
