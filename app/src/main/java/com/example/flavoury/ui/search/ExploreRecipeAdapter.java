package com.example.flavoury.ui.search;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ExploreRecipeAdapter extends RecyclerView.Adapter<ExploreRecipeAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModelArrayList;
    public ExploreRecipeAdapter(ArrayList<RecipeModel> recipeModelArrayList){
        this.recipeModelArrayList = recipeModelArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipe = recipeModelArrayList.get(position);
        holder.bindData(recipe);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList == null){
            return 0;
        }else {
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg;
        ShimmerFrameLayout shimmerFrameLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.list_post_shimmer);
            recipeImg = itemView.findViewById(R.id.list_post_img);
        }

        void bindData(RecipeModel recipe){

            setRecipeImg(recipe);
            recipeImg.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra("Recipe", (Serializable) recipe);
                itemView.getContext().startActivity(intent);
            });
        }

        void setRecipeImg(RecipeModel recipe){
            StorageReference recipeRef = FirebaseStorage.getInstance().getReference().child("recipe").child(recipe.getImgid()+".jpg");
            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recipeImg.setVisibility(View.VISIBLE);
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ExploreRecipe", "Failed to load: "+ e.getMessage());
                }
            });
        }
    }
}
