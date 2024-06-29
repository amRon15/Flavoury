package com.example.flavoury.ui.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    public BookmarkAdapter(ArrayList<RecipeModel> recipeModelArrayList){
        this.recipeModelArrayList = recipeModelArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bookmark_recipe,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = new RecipeModel();
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList == null){
            return 0;
        } else{
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg, userIcon;
        TextView userName, recipeName, category, cookTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.bookmark_list_recipeImg);
            recipeName = itemView.findViewById(R.id.bookmark_list_recipeName);
            userName = itemView.findViewById(R.id.bookmark_list_userName);
            category = itemView.findViewById(R.id.bookmark_list_category);
            cookTime = itemView.findViewById(R.id.bookmark_list_mins);
        }
        void bindData(RecipeModel recipeModel){
            setImage(recipeModel);
            userName.setText(recipeModel.getUsername());
            recipeName.setText(recipeModel.getRName());
            category.setText(recipeModel.getCategory());
            cookTime.setText(recipeModel.getCookTime());

            userIcon.setOnClickListener(v->{
                intentToProfile(recipeModel);
            });

            userName.setOnClickListener(v->{
                intentToProfile(recipeModel);
            });

            recipeImg.setOnClickListener(v->{
                intentToDetail(recipeModel);
            });

            recipeName.setOnClickListener(v->{
                intentToDetail(recipeModel);
            });
        }

        void intentToProfile(RecipeModel recipe){
            Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
            intent.putExtra("otherUid", recipe.getRid());
            itemView.getContext().startActivity(intent);
        }

        void intentToDetail(RecipeModel recipe){
            Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
            intent.putExtra("Recipe", recipe);
            itemView.getContext().startActivity(intent);
        }

        void setImage(RecipeModel recipeModel){
            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user").child(recipeModel.getIconid());
            StorageReference recipeRef = FirebaseStorage.getInstance().getReference().child("recipe").child(recipeModel.getImgid());

            userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                }
            });

            recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                }
            });
        }
    }
}
