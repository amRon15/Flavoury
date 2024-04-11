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
import com.example.flavoury.ui.detail.DetailActivity;

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
//        holder.postImageBtn.setImageResource(R.drawable.ic_launcher_foreground);
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
        ImageButton postImageBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageBtn = itemView.findViewById(R.id.likes_post_list_image);
        }

        void bindData(RecipeModel recipe) {
            postImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detail_recipe_intent = new Intent(likesFragment, DetailActivity.class);
                    detail_recipe_intent.putExtra("detailRecipe",recipe);
                    likesFragment.startActivity(detail_recipe_intent);
                }
            });
        }
    }



}
