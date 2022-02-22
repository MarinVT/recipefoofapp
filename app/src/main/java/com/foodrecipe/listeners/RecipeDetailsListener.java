package com.foodrecipe.listeners;

import com.foodrecipe.models.RecipeDetailsResponse;

public interface RecipeDetailsListener {

    void didFetch(RecipeDetailsResponse recipeDetailsResponse, String message);

    void didError(String message);
}
