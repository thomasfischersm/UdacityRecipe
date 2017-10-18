package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows the recipe and the overview of steps.
 */
public class RecipeActivity extends ParentActivity implements RecipeFragmentOwner {

    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recipe;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long recipeId = ActivityNavigator.getRecipeId(getIntent());

        RecipeFragment recipeFragment =
                (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment);
        recipeFragment.setRecipeId(recipeId);

        Log.i(LOG_TAG, "onCreate: Recipe is of type " + recipeFragment.getClass().getName());
    }

    @Override
    public void onRecipeStepSelected(long recipeId, long stepId) {
        // TODO
    }
}
