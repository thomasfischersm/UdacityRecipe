package com.playposse.udacityrecipe.data;

import com.playposse.udacityrecipe.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A library of recipe photos. The online JSON data doesn't seem to provide any recipe photos. To
 * make the app look better, here are photos from unsplash.com
 */
public final class RecipePhotoLibrary {

    private static Map<String, Integer> recipeNameToDrawableMap = new HashMap<String, Integer>() {{
        put("Nutella Pie", R.drawable.toa_heftiba_134254);
        put("Brownies", R.drawable.nordwood_themes_357997);
        put("Yellow Cake", R.drawable.brina_blum_114391);
        put("Cheesecake", R.drawable.chinh_le_duc_296872);
    }};

    private RecipePhotoLibrary() {}

    public static Integer getPhoto(String recipeName) {
        if (recipeNameToDrawableMap.containsKey(recipeName)) {
            return recipeNameToDrawableMap.get(recipeName);
        } else {
            return null;
        }
    }
}
