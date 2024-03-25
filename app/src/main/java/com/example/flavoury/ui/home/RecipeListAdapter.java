package com.example.flavoury.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {
    ArrayList<RecipeInList> recipeList;

    public RecipeListAdapter(ArrayList<RecipeInList> recipeInLists) {
        this.recipeList = recipeInLists;
    }

    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recipe_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.MyViewHolder holder, int position) {
        RecipeInList recipe = recipeList.get(position);
        holder.cal.setText(String.valueOf(recipe.getCal()));
        holder.recipeName.setText(recipe.getRecipeName());
        holder.likes.setText(String.valueOf(recipe.getLikes()));
        holder.userName.setText(recipe.getUser());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageButton userIcon,recipeImg;
        public Button userName;
        public TextView recipeName,cal,likes;

        public MyViewHolder(View itemView){
            super(itemView);
            userIcon = itemView.findViewById(R.id.home_pop_list_userImg);
            userName = itemView.findViewById(R.id.home_pop_list_userName);
            cal = itemView.findViewById(R.id.home_pop_list_cal);
            likes = itemView.findViewById(R.id.home_pop_list_like);
            recipeName = itemView.findViewById(R.id.home_pop_list_recipeName);
            recipeImg = itemView.findViewById(R.id.home_pop_list_recipeImg);
        }
    }
}


