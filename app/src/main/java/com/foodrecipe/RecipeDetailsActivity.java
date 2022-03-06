package com.foodrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodrecipe.adapter.IngredientsAdapter;
import com.foodrecipe.adapter.InstructionsAdapter;
import com.foodrecipe.adapter.SimilarRecipeAdapter;
import com.foodrecipe.listeners.InstructionListener;
import com.foodrecipe.listeners.RecipeClickListener;
import com.foodrecipe.listeners.RecipeDetailsListener;
import com.foodrecipe.listeners.SimilarRecipesListener;
import com.foodrecipe.models.InstructionsResponse;
import com.foodrecipe.models.RecipeDetailsResponse;
import com.foodrecipe.models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    int id;
    TextView textView_meal_name;
    TextView textView_meal_source;
    TextView textView_meal_summary;
    ImageView imageView_meal;
    RecyclerView recycle_meal_ingredients;
    RecyclerView recycle_meal_similar;
    RecyclerView recycle_meal_instructions;

    InstructionsAdapter instructionsAdapter;

    SimilarRecipeAdapter similarRecipeAdapter;

    IngredientsAdapter ingredientsAdapter;

    RequestManager requestManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));

        requestManager = new RequestManager(this);
        requestManager. getRecipeDetails(recipeDetailsListener, id);

        requestManager.getSimilarRecipe(similarRecipesListener, id);

        requestManager.getInstruction(instructionListener, id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Details");
        progressDialog.show();

    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal = findViewById(R.id.imageView_meal);
        recycle_meal_ingredients = findViewById(R.id.recycle_meal_ingredients);
        recycle_meal_similar = findViewById(R.id.recycle_meal_similar);
        recycle_meal_instructions = findViewById(R.id.recycle_meal_instructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse recipeDetailsResponse, String message) {
            progressDialog.dismiss();

            textView_meal_name.setText(recipeDetailsResponse.title);
            textView_meal_source.setText(recipeDetailsResponse.sourceName);
            textView_meal_summary.setText(recipeDetailsResponse.summary);

            Picasso.get().load(recipeDetailsResponse.image).into(imageView_meal);

            recycle_meal_ingredients.setHasFixedSize(true);
            recycle_meal_ingredients.setLayoutManager(
                    new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false)
            );
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, recipeDetailsResponse.extendedIngredients);
            recycle_meal_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message , Toast.LENGTH_SHORT).show();
        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> responses, String message) {
            recycle_meal_similar.setHasFixedSize(true);
            recycle_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, responses, recipeClickListener);
            recycle_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
            .putExtra("id", id));
        }
    };

    private final InstructionListener instructionListener = new InstructionListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycle_meal_instructions.setHasFixedSize(true);
            recycle_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));

            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycle_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };

}





