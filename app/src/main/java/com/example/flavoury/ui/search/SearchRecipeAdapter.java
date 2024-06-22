package com.example.flavoury.ui.search;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SearchRecipeAdapter extends RecyclerView.Adapter<SearchRecipeAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    public SearchRecipeAdapter(ArrayList<RecipeModel> recipeModelArrayList){
        this.recipeModelArrayList = recipeModelArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_recipe,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = recipeModelArrayList.get(position);
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList.isEmpty()){
            return 0;
        }else {
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg, userIcon;
        TextView recipeName, recipeCookingTime, recipeCategory, userName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImg = itemView.findViewById(R.id.search_recipe_img);
            recipeName = itemView.findViewById(R.id.search_recipe_name);
            recipeCookingTime = itemView.findViewById(R.id.search_recipe_cookingTime);
            recipeCategory = itemView.findViewById(R.id.search_recipe_category);
            userName = itemView.findViewById(R.id.search_recipe_userName);
            userIcon = itemView.findViewById(R.id.search_recipe_userIcon);
        }
        void bindData(RecipeModel recipeModel){
            recipeName.setText(recipeModel.getRName());
            recipeCookingTime.setText(recipeModel.getCookTime());
            recipeCategory.setText(recipeModel.getCategory());
            userName.setText(recipeModel.getUsername());

            setImg(recipeModel.getImgid(), recipeModel.getIconid());

            recipeImg.setOnClickListener(v -> {
                intent(true, recipeModel);
            });

            recipeName.setOnClickListener(v -> {
                intent(true, recipeModel);
            });

            userIcon.setOnClickListener(v -> {
                intent(false, recipeModel);
            });

            userName.setOnClickListener(v -> {
                intent(false, recipeModel);
            });
        }

        void intent(boolean isRecipe, RecipeModel recipe){
            Intent intent = new Intent(itemView.getContext(), isRecipe ? DetailActivity.class : ProfileActivity.class);
            if (isRecipe){
                intent.putExtra("Recipe", recipe);
            }else {
                intent.putExtra("otherUid", recipe.getUid());
            }
            itemView.getContext().startActivity(intent);
        }

        void setImg(String imgId, String iconId){
            StorageReference recipeRef = FirebaseStorage.getInstance().getReference().child("recipe").child(imgId+".jpg");
            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user").child(iconId+".jpg");

            recipeRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).fit().into(recipeImg);
            });

            userRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).fit().into(userIcon);
            });

        }
    }
}
