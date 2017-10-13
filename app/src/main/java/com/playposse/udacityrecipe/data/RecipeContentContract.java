package com.playposse.udacityrecipe.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A contract for {@link RecipeContentProvider}.
 */
public final class RecipeContentContract {

    public static final String AUTHORITY = "com.playposse.udacityrecipe.provider";

    private static final String CONTENT_SCHEME = "content";

    private RecipeContentContract() {}

    private static Uri createContentUri(String path) {
        return new Uri.Builder()
                .scheme(CONTENT_SCHEME)
                .encodedAuthority(AUTHORITY)
                .appendPath(path)
                .build();
    }

    /**
     * Stores meta information about recipes.
     */
    public static final class RecipeTable implements BaseColumns {

        public static final String PATH = "recipe";
        public static final Uri CONTENT_URI = createContentUri(PATH);
        public static final String TABLE_NAME = "recipe";

        public static final String ID_COLUMN = _ID;
        public static final String NAME_COLUMN = "name";
        public static final String SERVINGS_COLUMN = "servings";
        public static final String IMAGE_COLUMN = "image";

        public static final String[] COLUMN_NAMES = new String[]{
                ID_COLUMN,
                NAME_COLUMN,
                SERVINGS_COLUMN,
                IMAGE_COLUMN};

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE recipe "
                        + "(_id INTEGER PRIMARY KEY, "
                        + "name TEXT, "
                        + "servings INTEGER, "
                        + "image TEXT)";
    }

    /**
     * Stores meta information about ingredients.
     */
    public static final class IngredientTable implements BaseColumns {

        public static final String PATH = "ingredient";
        public static final Uri CONTENT_URI = createContentUri(PATH);
        public static final String TABLE_NAME = "ingredient";

        public static final String ID_COLUMN = _ID;
        public static final String RECIPE_ID_COLUMN = "recipe_id";
        public static final String QUANTITY_COLUMN = "quantity";
        public static final String MEASURE_COLUMN = "measure";
        public static final String INGREDIENT_COLUMN = "ingredient";

        public static final String[] COLUMN_NAMES = new String[]{
                ID_COLUMN,
                RECIPE_ID_COLUMN,
                QUANTITY_COLUMN,
                MEASURE_COLUMN,
                INGREDIENT_COLUMN};

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE ingredient "
                        + "(_id INTEGER PRIMARY KEY, "
                        + "recipe_id INTEGER, "
                        + "quantity TEXT, " // TEXT makes it easier to deal with fractions.
                        + "measure TEXT, "
                        + "ingredient TEXT, "
                        + "FOREIGN KEY(recipe_id) REFERENCES recipe(_id))";
    }

    /**
     * Stores meta information about a cooking step.
     */
    public static final class StepTable implements BaseColumns {

        public static final String PATH = "step";
        public static final Uri CONTENT_URI = createContentUri(PATH);
        public static final String TABLE_NAME = "step";

        public static final String ID_COLUMN = _ID;
        public static final String RECIPE_ID_COLUMN = "recipe_id";
        public static final String STEP_INDEX_COLUMN = "step_index";
        public static final String SHORT_DESCRIPTION_COLUMN = "short_description";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String VIDEO_URL_COLUMN = "videoUrl";
        public static final String THUMBNAIL_COLUMN = "thumbnailUrl";

        public static final String[] COLUMN_NAMES = new String[]{
                ID_COLUMN,
                RECIPE_ID_COLUMN,
                STEP_INDEX_COLUMN,
                SHORT_DESCRIPTION_COLUMN,
                DESCRIPTION_COLUMN,
                VIDEO_URL_COLUMN,
                THUMBNAIL_COLUMN};

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE step "
                        + "(_id INTEGER PRIMARY KEY, "
                        + "recipe_id INTEGER, "
                        + "step_index INTEGER, "
                        + "short_description TEXT, "
                        + "description TEXT, "
                        + "videoUrl TEXT, "
                        + "thumbnailUrl TEXT, "
                        + "FOREIGN KEY(recipe_id) REFERENCES recipe(_id))";
    }
}
