package com.example.flavoury.ui.viewMore;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewMoreAdapter extends RecyclerView.Adapter<ViewMoreAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModels;

    public ViewMoreAdapter(ArrayList<RecipeModel> recipeModels){
        this.recipeModels = recipeModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_follow_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = recipeModels.get(position);
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModels == null){
            return 0;
        } else {
            return recipeModels.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg, userIcon;
        TextView recipeName, userName, description, category, minutes;
        ShimmerFrameLayout userShimmer, recipeShimmer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.follow_post_recipeImg);
            userIcon = itemView.findViewById(R.id.follow_post_userIcon);
            recipeName = itemView.findViewById(R.id.follow_post_recipeName);
            userName = itemView.findViewById(R.id.follow_post_username);
            description = itemView.findViewById(R.id.follow_post_description);
            userShimmer = itemView.findViewById(R.id.follow_post_shimmer_userIcon);
            recipeShimmer = itemView.findViewById(R.id.follow_post_shimmer_recipeImg);
            category = itemView.findViewById(R.id.follow_post_category);
            minutes = itemView.findViewById(R.id.follow_post_minutes);
        }

        void bindData(RecipeModel recipeModel){
            setImg(recipeModel);

            recipeName.setText(recipeModel.getRName());
            userName.setText(recipeModel.getUsername());
            description.setText(recipeModel.getDescription());
            category.setText(recipeModel.getCategory());
            minutes.setText(recipeModel.getCookTime());
        }

        void setImg(RecipeModel recipeModel){
            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user").child(recipeModel.getIconid()+".jpg");
            StorageReference recipeRef = FirebaseStorage.getInstance().getReference().child("recipe").child(recipeModel.getImgid()+".jpg");

            userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                    userShimmer.stopShimmer();
                    userShimmer.setVisibility(View.GONE);
                }
            });

            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                    recipeShimmer.stopShimmer();
                    recipeShimmer.setVisibility(View.GONE);
                }
            });
        }

    }
}
