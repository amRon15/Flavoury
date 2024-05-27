package com.example.flavoury.ui.likes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.RoundCornerTransform;
import com.example.flavoury.ui.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LikesPostAdapter extends RecyclerView.Adapter<LikesPostAdapter.MyViewHolder> {

    private List<RecipeModel> recipes;
    private Context likesFragment;
    private Intent detail_recipe_intent;

    public void setLikesPostAdapter(List<RecipeModel> recipes,Context likesFragment) {
        this.recipes = recipes;
        this.likesFragment = likesFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_likes_post, parent, false);
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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        void bindData(RecipeModel recipe) {

        }
    }



}
