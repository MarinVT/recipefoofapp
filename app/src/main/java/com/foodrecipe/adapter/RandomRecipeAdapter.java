package com.foodrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.R;
import com.foodrecipe.listeners.RecipeClickListener;
import com.foodrecipe.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{

    Context context;
    List<Recipe> listRecipe;
    RecipeClickListener recipeClickListener;


    public RandomRecipeAdapter(Context context, List<Recipe> listRecipe, RecipeClickListener recipeClickListener) {
        this.context = context;
        this.listRecipe = listRecipe;
        this.recipeClickListener = recipeClickListener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textView_Title.setText(listRecipe.get(position).title);
        holder.textView_Title.setSelected(true);
        holder.textView_likes.setText(listRecipe.get(position).aggregateLikes + " Likes");
        holder.textView_servings.setText(listRecipe.get(position).servings + " Servings");
        holder.textView_time.setText(listRecipe.get(position).readyInMinutes + " Minutes");

        Picasso.get().load(listRecipe.get(position).image).into(holder.imageView_food);

        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeClickListener.onRecipeClicked(String.valueOf(listRecipe.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRecipe.size();
    }
}

class RandomRecipeViewHolder extends RecyclerView.ViewHolder {

    CardView random_list_container;
    TextView textView_Title;
    TextView textView_servings;
    TextView textView_likes;
    TextView textView_time;
    ImageView imageView_food;


    //initialize all
    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textView_Title = itemView.findViewById(R.id.textView_Title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_food = itemView.findViewById(R.id.imageView_food);
    }
}
