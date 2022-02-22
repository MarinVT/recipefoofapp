package com.foodrecipe;

import android.content.Context;

import com.foodrecipe.listeners.RandomRecipesResponseListeners;
import com.foodrecipe.listeners.RecipeDetailsListener;
import com.foodrecipe.models.RandomRecipeAPIResponse;
import com.foodrecipe.models.RecipeDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class RequestManager {
    // obj for context
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }


    public void getRandomRecipes(RandomRecipesResponseListeners responseListeners, List<String> tags) {
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);

        Call<RandomRecipeAPIResponse> callRandomRecipesAPIResponse = callRandomRecipes.callRandomRecipesAPI(
                context.getString(R.string.api_key), "10", tags);

        callRandomRecipesAPIResponse.enqueue(new Callback<RandomRecipeAPIResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeAPIResponse> call, Response<RandomRecipeAPIResponse> response) {
                if (!response.isSuccessful()) {
                    responseListeners.didError(response.message());
                    return;
                }

                responseListeners.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeAPIResponse> call, Throwable t) {
                responseListeners.didError(t.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener recipeDetailsListener, int id) {
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> callRecipeDetailsResponse = callRecipeDetails.callRecipeDetails(
                id, context.getString(R.string.api_key));


        callRecipeDetailsResponse.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()) {
                    recipeDetailsListener.didError(response.message());
                    return;
                }

                recipeDetailsListener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                recipeDetailsListener.didError(t.getMessage());
            }
        });
    }

    // random recipe interface
    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeAPIResponse> callRandomRecipesAPI(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags")List<String> tags
                );
    }

    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Part("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}
