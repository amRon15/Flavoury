package com.example.flavoury.ui.home;

import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserModel;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    private List<RecipeModel> recipes = new ArrayList<>();

    public void setRecipeListAdapter(List<RecipeModel> recipes) {
        this.recipes = recipes;
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
            return recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton userIcon, recipeImg;
        public Button userName;
        public TextView recipeName, cookingTime, likes;

        public MyViewHolder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.home_pop_list_userImg);
            userName = itemView.findViewById(R.id.home_pop_list_userName);
            cookingTime = itemView.findViewById(R.id.home_pop_list_time);
            likes = itemView.findViewById(R.id.home_pop_list_like);
            recipeName = itemView.findViewById(R.id.home_pop_list_recipeName);
            recipeImg = itemView.findViewById(R.id.home_pop_list_recipeImg);
        }

        void bindData(RecipeModel recipe) {

            userName.setText(recipe.getUserName());
            recipeName.setText(recipe.getRecipeName());
            cookingTime.setText("~" + Integer.toString(recipe.getCookingMinutes()) + " Mins");
            likes.setText(Integer.toString(recipe.getLike()));

            Picasso.get().load(recipe.getRecipeImg()).centerCrop().fit().into(recipeImg);
        }
    }
}


