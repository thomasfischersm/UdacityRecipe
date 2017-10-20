package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows the recipe and recipe steps side by side.
 */
public class RecipeMasterActivity extends ParentActivity implements RecipeFragmentOwner {

    private static final String LOG_TAG = RecipeMasterActivity.class.getSimpleName();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recipe_master;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long recipeId = ActivityNavigator.getRecipeId(getIntent());

        RecipeFragment recipeFragment =
                (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment);
        recipeFragment.setRecipeId(recipeId);

        Log.i(LOG_TAG, "onCreate: Recipe is of type " + recipeFragment.getClass().getName());
    }

    @Override
    public void onRecipeStepSelected(long recipeId, int stepIndex, String recipeName) {
        RecipeStepContainerFragment recipeStepContainerFragment =
                (RecipeStepContainerFragment) getSupportFragmentManager().findFragmentById(
                        R.id.recipe_step_container_fragment);
        recipeStepContainerFragment.setRecipeStep(recipeId, stepIndex);
    }
}
