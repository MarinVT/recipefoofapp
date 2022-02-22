package com.foodrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.foodrecipe.adapter.RandomRecipeAdapter;
import com.foodrecipe.listeners.RandomRecipesResponseListeners;
import com.foodrecipe.listeners.RecipeClickListener;
import com.foodrecipe.models.RandomRecipeAPIResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    RequestManager requestManager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");

        searchView = findViewById(R.id.search_view_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                requestManager.getRandomRecipes(randomRecipesResponseListeners, tags);
                progressDialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.tags,
                R.layout.spinner_text
        );

        arrayAdapterSpinner.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapterSpinner);
        spinner.setOnItemSelectedListener(spinnerSelectorListener);


        requestManager = new RequestManager(this);
//        requestManager.getRandomRecipes(randomRecipesResponseListeners);
//        progressDialog.show();
    }

    private final RandomRecipesResponseListeners randomRecipesResponseListeners = new RandomRecipesResponseListeners() {
        @Override
        public void didFetch(RandomRecipeAPIResponse response, String message) {
            progressDialog.dismiss();
            recyclerView = findViewById(R.id.recycle_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectorListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            requestManager.getRandomRecipes(randomRecipesResponseListeners, tags);
            progressDialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class)
            .putExtra("id", id));
        }
    };

}








