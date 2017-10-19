package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows an individual step in a recipe.
 */
public class RecipeStepActivity extends ParentActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recipe_step;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long recipeId = ActivityNavigator.getRecipeId(getIntent());
        int stepIndex = ActivityNavigator.getRecipeStepIndex(getIntent());

        RecipeStepContainerFragment recipeStepContainerFragment =
                (RecipeStepContainerFragment) getSupportFragmentManager().findFragmentById(
                        R.id.recipe_fragment);
        recipeStepContainerFragment.setRecipeStep(recipeId, stepIndex);
    }
}
