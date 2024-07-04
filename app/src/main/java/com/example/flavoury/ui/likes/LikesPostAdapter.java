package com.example.flavoury.ui.likes;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikesPostAdapter extends RecyclerView.Adapter<LikesPostAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModels = new ArrayList<>();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public LikesPostAdapter(ArrayList<RecipeModel> recipeModels){
        this.recipeModels = recipeModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_likes_post,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = recipeModels.get(position);
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModels.isEmpty()){
            return 0;
        } else {
            return recipeModels.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView userIcon, recipeImg;
        TextView likesDescription;
        ShimmerFrameLayout shimmerIcon, shimmerImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.likes_post_user_icon);
            recipeImg = itemView.findViewById(R.id.likes_post_recipeImg);
            likesDescription = itemView.findViewById(R.id.likes_post_likes_description);
            shimmerIcon = itemView.findViewById(R.id.likes_post_shimmer_icon);
            shimmerImg = itemView.findViewById(R.id.likes_post_shimmer_img);
        }
        void bindData(RecipeModel recipeModel){
            likesDescription.setText(recipeModel.getLUsername()+" likes your "+ recipeModel.getRName());
            setImg(recipeModel);

            userIcon.setOnClickListener(v->{
                intentToProfile(recipeModel.getLUid());
            });

            recipeImg.setOnClickListener(v->{
                intentToDetail(recipeModel);
            });
        }

        void setImg(RecipeModel recipeModel){
            storageRef.child("user").child(recipeModel.getLIconid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                    shimmerIcon.stopShimmer();
                    shimmerIcon.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    shimmerIcon.stopShimmer();
                    shimmerIcon.setVisibility(View.GONE);
                }
            });

            storageRef.child("recipe").child(recipeModel.getImgid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                    shimmerImg.stopShimmer();
                    shimmerImg.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    shimmerImg.stopShimmer();
                    shimmerImg.setVisibility(View.GONE);
                }
            });
        }

        void intentToProfile(String otherUid){
            Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
            intent.putExtra("otherUid", otherUid);
            itemView.getContext().startActivity(intent);
        }

        void intentToDetail(RecipeModel recipeModel){
            Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
            intent.putExtra("Recipe", recipeModel);
            itemView.getContext().startActivity(intent);
        }
    }
}
