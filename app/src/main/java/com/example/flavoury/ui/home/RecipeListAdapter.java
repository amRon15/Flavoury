package com.example.flavoury.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.RoundCornerTransform;
import com.example.flavoury.ui.detail.DetailActivity;
import com.squareup.picasso.Picasso;


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
//            userName = itemView.findViewById(R.id.home_pop_list_userName);
            cookingTime = itemView.findViewById(R.id.home_pop_list_time);
            likes = itemView.findViewById(R.id.home_pop_list_like);
            recipeName = itemView.findViewById(R.id.home_pop_list_recipeName);
            recipeImg = itemView.findViewById(R.id.home_pop_list_recipeImg);
            likeToggle = itemView.findViewById(R.id.home_pop_likeToggle);
        }

        void bindData(RecipeModel recipe) {
//            Log.d("Recipe","RecipeName:" + recipe.getRecipeName() + ", RecipeIMG: "+recipe.getRecipeImg());
//            if (recipe.getUserName() != null) {
//                userName.setText(recipe.getUserName());
//            }
//            recipeName.setText(recipe.getRecipeName());
////            cookingTime.setText("~" + Integer.toString(recipe.getCookingMinutes()) + " Mins");
//            likes.setText(Integer.toString(recipe.getLikes()));
//            recipeImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    detail_recipe_intent = new Intent(homeFragment, DetailActivity.class);
//                    detail_recipe_intent.putExtra("detailRecipe", recipe);
//
//                    homeFragment.startActivity(detail_recipe_intent);
//                }
//            });
//
//            if (recipe.getRecipeImg()!=null) {
//                Picasso.get().load(recipe.getRecipeImg()).centerCrop().fit().transform(new RoundCornerTransform()).into(recipeImg);
//            }
        }
    }
}


