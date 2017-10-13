package com.playposse.udacityrecipe.service.retrofit;

import java.util.List;

/**
 * A Retrofit Recipe object.
 */
public class Recipe {

    private long id;
    private String name;
    private int servings;
    private String image;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
