package com.foodrecipe.listeners;

import com.foodrecipe.models.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> responses, String message);

    void didError(String message);
}
