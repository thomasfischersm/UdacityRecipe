package com.playposse.udacityrecipe.service.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit service for recipes.
 */
public interface RecipeService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> get();
}
