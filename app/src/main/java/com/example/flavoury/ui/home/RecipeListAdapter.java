package com.example.flavoury.ui.home;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.google.android.material.shape.RoundedCornerTreatment;


import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    private List<RecipeModel> recipes = new ArrayList<>();
    private Intent detail_recipe_intent;
    private Context homeFragment;
    private HomeViewModel homeViewModel;

    public void setRecipeListAdapter(List<RecipeModel> recipes, Context homeFragment,HomeViewModel homeViewModel) {
        this.recipes = recipes;
        this.homeFragment = homeFragment;
        this.homeViewModel = homeViewModel;
    }


    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_recipe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.MyViewHolder holder, int position) {
        RecipeModel recipe = recipes.get(position);
        holder.bindData(recipe);
    }

    @Override
    public int getItemCount() {
        if (!recipes.isEmpty()) {
            return recipes.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton userIcon, recipeImg;
        public Button userName;
        public TextView recipeName, cookingTime, likes;
        ToggleButton likeToggle;

        public MyViewHolder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.home_pop_list_userImg);
            userName = itemView.findViewById(R.id.home_pop_list_userName);
            cookingTime = itemView.findViewById(R.id.home_pop_list_time);
            likes = itemView.findViewById(R.id.home_pop_list_like);
            recipeName = itemView.findViewById(R.id.home_pop_list_recipeName);
            recipeImg = itemView.findViewById(R.id.home_pop_list_recipeImg);
            likeToggle = itemView.findViewById(R.id.home_pop_likeToggle);
        }

        void bindData(RecipeModel recipe) {

            userName.setText(recipe.getUserName());
            recipeName.setText(recipe.getRecipeName());
            cookingTime.setText("~" + Integer.toString(recipe.getCookingMinutes()) + " Mins");
            likes.setText(Integer.toString(recipe.getLike()));
            recipeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_recipe_intent = new Intent(homeFragment, DetailActivity.class);
                    detail_recipe_intent.putExtra("detailRecipeID", recipe.getRecipeID());

                    homeFragment.startActivity(detail_recipe_intent);
                }
            });
            likeToggle.setChecked(recipe.getIsRecipeLike());
            likeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    homeViewModel.handleLikeRecipe(isChecked, recipe.getRecipeID());
                    compoundButton.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            compoundButton.animate().scaleX(1).scaleY(1).setDuration(100);
                        }
                    });
                }
            });

//            Picasso.get().load(recipe.getRecipeImg()).centerCrop().fit().into(recipeImg);
        }
    }
}


