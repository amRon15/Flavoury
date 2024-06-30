package com.example.flavoury.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.printservice.CustomPrinterIconCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class FollowingFragment extends Fragment {
    RecipeModel recipe;
    ShapeableImageView userIcon, recipeImg;
    TextView description, userName, recipeName, category, cookTime;
    ShimmerFrameLayout shimmerUserIcon, shimmerRecipeImg;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    public FollowingFragment(RecipeModel recipe){
        this.recipe = recipe;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_follow_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userIcon = view.findViewById(R.id.follow_post_userIcon);
        userName = view.findViewById(R.id.follow_post_username);
        recipeImg = view.findViewById(R.id.follow_post_recipeImg);
        recipeName = view.findViewById(R.id.follow_post_recipeName);
        description = view.findViewById(R.id.follow_post_description);
        category = view.findViewById(R.id.follow_post_category);
        cookTime = view.findViewById(R.id.follow_post_minutes);
        shimmerRecipeImg = view.findViewById(R.id.follow_post_shimmer_recipeImg);
        shimmerUserIcon = view.findViewById(R.id.follow_post_shimmer_userIcon);

        recipeName.setText(recipe.getRName());
        userName.setText(recipe.getUsername());
        description.setText(recipe.getDescription());
        category.setText(recipe.getCategory());
        cookTime.setText(recipe.getCookTime());

        userIcon.setOnClickListener(v->{
            intentToProfile();
        });

        userName.setOnClickListener(v->{
            intentToProfile();
        });

        recipeImg.setOnClickListener(v->{
            intentToRecipe();
        });

        recipeName.setOnClickListener(v->{
            intentToRecipe();
        });

        setImg();
    }

    private void intentToRecipe(){
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("Recipe", (Serializable) recipe);
        startActivity(intent);
    }

    private void intentToProfile(){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("otherUid", recipe.getUid());
        startActivity(intent);
    }

    private void setImg(){
        StorageReference recipeRef = storageRef.child("recipe").child(recipe.getImgid()+".jpg");
        StorageReference userRef = storageRef.child("user").child(recipe.getIconid()+".jpg");

        recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                shimmerRecipeImg.stopShimmer();
                shimmerRecipeImg.setVisibility(View.GONE);
            }
        });
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                shimmerUserIcon.stopShimmer();
                shimmerUserIcon.setVisibility(View.GONE);
            }
        });
    }
}
