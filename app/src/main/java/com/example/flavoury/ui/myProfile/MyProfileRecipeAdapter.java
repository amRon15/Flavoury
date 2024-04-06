package com.example.flavoury.ui.myProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;

import java.util.List;

public class MyProfileRecipeAdapter extends RecyclerView.Adapter<MyProfileRecipeAdapter.MyViewHolder> {
    private List<RecipeModel> recipes;
    private Intent detail_recipe_intent;
    private Context myProfileFragment;

    public void setMyProfileRecipeAdapter(List<RecipeModel> recipes, Context myProfileFragment) {
        this.recipes = recipes;
        this.myProfileFragment = myProfileFragment;
    }

    @NonNull
    @Override
    public MyProfileRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_profile_recipe, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileRecipeAdapter.MyViewHolder holder, int position) {
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
        public TextView recipeName;
        public ImageButton recipeIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.my_profile_recipeName);
            recipeIcon = itemView.findViewById(R.id.my_profile_recipeIcon);
        }

        void bindData(RecipeModel recipe) {
            recipeName.setText(recipe.getRecipeName());
            recipeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detail_recipe_intent = new Intent(myProfileFragment, DetailActivity.class);
                    detail_recipe_intent.putExtra("detailRecipeID", recipe.getRecipeID());
                    myProfileFragment.startActivity(detail_recipe_intent);
                }
            });
        }
    }
}
