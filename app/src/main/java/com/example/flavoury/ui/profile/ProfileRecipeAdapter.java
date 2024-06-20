package com.example.flavoury.ui.profile;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileRecipeAdapter extends RecyclerView.Adapter<ProfileRecipeAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModelArrayList;
    public ProfileRecipeAdapter(ArrayList<RecipeModel> recipeModelArrayList){
        this.recipeModelArrayList = recipeModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipe = recipeModelArrayList.get(position);
        holder.bindData(recipe);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList.isEmpty()){
            return 0;
        }else{
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.list_post_img);
        }
        void bindData(RecipeModel recipe){
            setRecipeImg(recipe.getImgid());
        }

        void setRecipeImg(String imgId){
            StorageReference recipeRef = FirebaseStorage.getInstance().getReference().child("recipe").child(imgId+".jpg");
            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                }
            });
        }
    }
}
