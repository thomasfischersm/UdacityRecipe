package com.playposse.udacityrecipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.playposse.udacityrecipe.data.RecipeContentContract.IngredientTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;

/**
 * A helper class that manages the SQLLite database.
 */
public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    public  static final String DB_NAME = "udacityrecipe";

    private static final int DB_VERSION = 1;

    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecipeTable.SQL_CREATE_TABLE);
        db.execSQL(IngredientTable.SQL_CREATE_TABLE);
        db.execSQL(StepTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
