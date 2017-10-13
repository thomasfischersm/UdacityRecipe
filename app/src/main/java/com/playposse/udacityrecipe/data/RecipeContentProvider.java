package com.playposse.udacityrecipe.data;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteOpenHelper;

import com.playposse.udacityrecipe.data.RecipeContentContract.IngredientTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;
import com.playposse.udacityrecipe.util.BasicContentProvider;

/**
 * A {@link ContentProvider} that stores the recipe information locally.
 */
public class RecipeContentProvider extends BasicContentProvider {

    private static final int RECIPE_TABLE_KEY = 1;
    private static final int INGREDIENT_TABLE_KEY = 2;
    private static final int STEP_TABLE_KEY = 3;

    public RecipeContentProvider() {
        addTable(
                RECIPE_TABLE_KEY,
                RecipeContentContract.AUTHORITY,
                RecipeTable.PATH,
                RecipeTable.CONTENT_URI,
                RecipeTable.TABLE_NAME);
        addTable(
                INGREDIENT_TABLE_KEY,
                RecipeContentContract.AUTHORITY,
                IngredientTable.PATH,
                IngredientTable.CONTENT_URI,
                IngredientTable.TABLE_NAME);
        addTable(
                STEP_TABLE_KEY,
                RecipeContentContract.AUTHORITY,
                StepTable.PATH,
                StepTable.CONTENT_URI,
                StepTable.TABLE_NAME);
    }

    @Override
    protected SQLiteOpenHelper createDatabaseHelper() {
        return new RecipeDatabaseHelper(getContext());
    }
}
