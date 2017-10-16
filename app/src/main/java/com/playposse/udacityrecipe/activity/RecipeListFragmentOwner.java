package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * A callback interface for the {@link Activity} to implement and the {@link Fragment} to
 * call. The {@link RecipeListFragment} can tell the owning activity that the user has selected
 * a recipe.
 */
public interface RecipeListFragmentOwner {

    void onRecipeSelected(long recipeId);
}
