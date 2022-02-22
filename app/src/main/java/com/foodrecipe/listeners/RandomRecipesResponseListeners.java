package com.foodrecipe.listeners;

import com.foodrecipe.models.RandomRecipeAPIResponse;

public interface RandomRecipesResponseListeners {
    void didFetch(RandomRecipeAPIResponse response, String message);
    void didError(String message);
}
