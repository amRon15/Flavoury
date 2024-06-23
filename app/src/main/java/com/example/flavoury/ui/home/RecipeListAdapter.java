package com.example.flavoury.ui.home;

import android.net.Uri;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {

    private ArrayList<RecipeModel> recipes = new ArrayList<>();

    public RecipeListAdapter(ArrayList<RecipeModel> recipes) {
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
        if (recipes != null) {
            return recipes.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView userIcon, recipeImg;
        TextView recipeName, userName, cookingTime, category;
        ShimmerFrameLayout shimmerUserIcon, shimmerRecipeImg;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        public MyViewHolder(View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.home_list_userIcon);
            userName = itemView.findViewById(R.id.home_list_userName);
            cookingTime = itemView.findViewById(R.id.home_list_mins);
            recipeName = itemView.findViewById(R.id.home_list_recipeName);
            recipeImg = itemView.findViewById(R.id.home_list_recipeImg);
            category = itemView.findViewById(R.id.home_list_category);
            shimmerRecipeImg = itemView.findViewById(R.id.home_list_shimmer_recipeImg);
            shimmerUserIcon = itemView.findViewById(R.id.home_list_shimmer_userIcon);
        }

        void bindData(RecipeModel recipe) {
            userName.setText(recipe.getUsername());
            recipeName.setText(recipe.getRName());
            cookingTime.setText(recipe.getCookTime());
            category.setText(recipe.getCategory());
            setImg(recipe);
        }

        void setImg(RecipeModel recipe){
            StorageReference recipeRef = storageRef.child("recipe").child(recipe.getImgid()+".jpg");
            StorageReference userRef = storageRef.child("user").child(recipe.getIconid()+".jpg");


            recipeRef.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isComplete()){
                    Uri uri = task.getResult();
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                    shimmerRecipeImg.stopShimmer();
                    shimmerRecipeImg.setVisibility(View.GONE);
                }
            });

            userRef.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isComplete()){
                    Uri uri = task.getResult();
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                    shimmerUserIcon.stopShimmer();
                    shimmerUserIcon.setVisibility(View.GONE);
                }
            });
        }
    }
}


