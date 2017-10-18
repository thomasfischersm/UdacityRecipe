package com.playposse.udacityrecipe.activity;

import android.app.Activity;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows a list of recipes for phones.
 */
public class RecipeListActivity extends ParentActivity implements RecipeListFragmentOwner {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_recipe_list;
    }

    @Override
    public void onRecipeSelected(long recipeId) {
        ActivityNavigator.startRecipeActivity(this, recipeId);
    }
}
