package com.foodrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodrecipe.adapter.IngredientsAdapter;
import com.foodrecipe.listeners.RecipeDetailsListener;
import com.foodrecipe.models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {

    int id;
    TextView textView_meal_name;
    TextView textView_meal_source;
    ImageView imageView_meal;
    TextView textView_meal_summary;
    RecyclerView recycle_meal_ingredients;
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
            recycle_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, recipeDetailsResponse.extendedIngredients);
            recycle_meal_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message , Toast.LENGTH_SHORT).show();
        }
    };

}





