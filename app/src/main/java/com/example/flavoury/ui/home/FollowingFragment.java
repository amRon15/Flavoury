package com.example.flavoury.ui.home;

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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class FollowingFragment extends Fragment {
    RecipeModel recipe;
    ShapeableImageView userIcon, recipeImg;
    TextView description, userName, recipeName;
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
        shimmerRecipeImg = view.findViewById(R.id.follow_post_shimmer_recipeImg);
        shimmerUserIcon = view.findViewById(R.id.follow_post_shimmer_userIcon);

        recipeName.setText(recipe.getRName());
        userName.setText(recipe.getUsername());
        description.setText(recipe.getDescription());


        setImg();
    }

    private void setImg(){
        StorageReference recipeRef = storageRef.child("recipe").child(recipe.getImgid()+".jpg");
        StorageReference userRef = storageRef.child("user").child(recipe.getIconid()+".jpg");

        recipeRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete()) {
                    Uri uri = task.getResult();
                    Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                    shimmerRecipeImg.stopShimmer();
                    shimmerRecipeImg.setVisibility(View.GONE);
                }
            }
        });
        userRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isComplete()) {
                    Uri uri = task.getResult();
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                    shimmerUserIcon.stopShimmer();
                    shimmerUserIcon.setVisibility(View.GONE);
                }
            }
        });
    }
}
