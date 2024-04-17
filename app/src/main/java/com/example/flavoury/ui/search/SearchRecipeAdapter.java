package com.example.flavoury.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.RoundCornerTransform;
import com.example.flavoury.ui.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.MyViewHolder> {
    private List<RecipeModel> recipes = new ArrayList<>();
    private Intent detail_recipe_intent;
    private Context searchFragment;

    public void setSearchRecipeAdapter(List<RecipeModel> recipes, Context searchFragment) {
        this.recipes = recipes;
        this.searchFragment = searchFragment;
    }

    @NonNull
    @Override
    public SearchRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_recipe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipe = recipes.get(position);
        holder.bindData(recipe);
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        } else {
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton recipeImg, userIcon;
        TextView recipeName, recipeCookingMinutes, recipeCat, userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.search_recipeIcon);
            recipeCat = itemView.findViewById(R.id.search_recipeCategory);
            recipeName = itemView.findViewById(R.id.search_recipeName);
            recipeCookingMinutes = itemView.findViewById(R.id.search_recipeCookingTime);
            userName = itemView.findViewById(R.id.search_userName);
            userIcon = itemView.findViewById(R.id.search_userIcon);
        }

        void bindData(RecipeModel recipe) {
            if (recipeImg != null) {
                Picasso.get().load(recipe.getRecipeImg()).centerCrop().fit().transform(new RoundCornerTransform()).into(recipeImg);
            }
            recipeName.setText(recipe.getRecipeName());
            recipeCookingMinutes.setText("~ " + recipe.getCookingMinutes() + " Mins");
            userName.setText(recipe.getUserName());
            recipeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detail_recipe_intent = new Intent(searchFragment, DetailActivity.class);
                    detail_recipe_intent.putExtra("detailRecipe", recipe);

                    searchFragment.startActivity(detail_recipe_intent);
                }
            });
        }
    }
}
