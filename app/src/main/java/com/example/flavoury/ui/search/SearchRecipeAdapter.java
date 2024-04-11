package com.example.flavoury.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.MyViewHolder> {
    private List<RecipeModel> recipes = new ArrayList<>();
    public void setSearchRecipeAdapter(List<RecipeModel> recipes){
        this.recipes = recipes;
    }
    @NonNull
    @Override
    public SearchRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_recipe,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipe = recipes.get(position);
        holder.bindData(recipe);
    }

    @Override
    public int getItemCount() {
        if (recipes !=null){
            return recipes.size();
        }else {
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageButton recipeIcon, userIcon;
        TextView recipeName, recipeCookingMinutes, recipeCat, userName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeIcon = itemView.findViewById(R.id.search_recipeIcon);
            recipeCat = itemView.findViewById(R.id.search_recipeCategory);
            recipeName = itemView.findViewById(R.id.search_recipeName);
            recipeCookingMinutes = itemView.findViewById(R.id.search_recipeCookingTime);
            userName = itemView.findViewById(R.id.search_userName);
            userIcon = itemView.findViewById(R.id.search_userIcon);
        }

        void bindData(RecipeModel recipe){
            recipeName.setText(recipe.getRecipeName());
            recipeCookingMinutes.setText("~ " + recipe.getCookingMinutes() + " Mins");
            userName.setText(recipe.getUserName());
        }
    }
}
