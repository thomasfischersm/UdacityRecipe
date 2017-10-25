package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    public void onRecipeSelected(long recipeId) {
        ActivityNavigator.startRecipeActivity(this, recipeId);
    }
}
