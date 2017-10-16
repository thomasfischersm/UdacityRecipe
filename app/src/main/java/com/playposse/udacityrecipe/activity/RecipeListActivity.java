package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.playposse.udacityrecipe.R;

/**
 * An {@link Activity} that shows a list of recipes for phones.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipeListFragmentOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);
    }

    @Override
    public void onRecipeSelected(long recipeId) {
        // TODO
    }
}
