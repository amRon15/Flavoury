package com.example.flavoury.ui.myProfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.firebase.ui.auth.data.model.Resource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyProfileRecipeAdapter extends RecyclerView.Adapter<MyProfileRecipeAdapter.MyViewHolder> {
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ArrayList<RecipeModel> recipeModelArrayList;

    public MyProfileRecipeAdapter(ArrayList<RecipeModel> recipeModelArrayList) {
        this.recipeModelArrayList = recipeModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = recipeModelArrayList.get(position);
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList==null){
            return 0;
        }else {
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView recipeImg;
        TextView recipeName, category, cookTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.list_profile_post_img);
            recipeName = itemView.findViewById(R.id.list_profile_post_recipeName);
            category = itemView.findViewById(R.id.list_profile_post_category);
            cookTime = itemView.findViewById(R.id.list_profile_post_cookTime);
        }

        void bindData(RecipeModel recipeModel){
            setRecipeImg(recipeModel.getImgid());
            recipeImg.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra("Recipe", (Serializable) recipeModel);
                itemView.getContext().startActivity(intent);
            });
            recipeName.setText(recipeModel.getRName());
            category.setText(recipeModel.getCategory());
            cookTime.setText(recipeModel.getCookTime());
        }

        void setRecipeImg(String imgId) {
            StorageReference recipeRef = storageRef.child("recipe").child(imgId + ".jpg");
            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                }
            });
        }
    }
}