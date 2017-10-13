package com.playposse.udacityrecipe.service.retrofit;

/**
 * A Retrofit class to represent a recipe ingredient.
 */
public class Ingredient {

    private String quantity;
    private String measure;
    private String ingredient;

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
