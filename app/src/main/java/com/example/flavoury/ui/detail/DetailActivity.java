package com.example.flavoury.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.RoundCornerTransform;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {
    RecipeModel detailRecipe;
    RecyclerView detailStepRecyclerView, detail_ingredients_recyclerview;
    DetailStepAdapter detailStepAdapter;
    DetailIngredientsAdapter detailIngredientsAdapter;
    ImageButton recipe_detail_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().hide();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        recipe_detail_backBtn = findViewById(R.id.recipe_detail_back_btn);
        recipe_detail_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        detailRecipe = (RecipeModel) getIntent().getSerializableExtra("detailRecipe");

        detailStepRecyclerView = findViewById(R.id.recipe_detail_step_recyclerView);
        detail_ingredients_recyclerview = findViewById(R.id.recipe_detail_ingredients_recyclerView);

        detailStepAdapter = new DetailStepAdapter();
        detailIngredientsAdapter = new DetailIngredientsAdapter();

        detailStepRecyclerView.setAdapter(detailStepAdapter);
        detailStepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        detail_ingredients_recyclerview.setAdapter(detailIngredientsAdapter);
        detail_ingredients_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DetailViewModel detailViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DetailViewModel.class);
        detailViewModel.fetchRecipe(detailRecipe);
        detailViewModel.getRecipeList().observe(this, new Observer<RecipeModel>() {
            @Override
            public void onChanged(RecipeModel recipeModel) {
                setView(recipeModel,detailViewModel);
                handleRecyclerView(recipeModel);
            }
        });


    }

    private void setView(RecipeModel recipe, DetailViewModel detailViewModel) {
        ImageView recipeImg = findViewById(R.id.recipe_detail_recipeImg);
        TextView recipeName = findViewById(R.id.recipe_detail_recipeName);
        TextView recipeCookingTime = findViewById(R.id.recipe_detail_cookingMins);
        TextView recipeLike = findViewById(R.id.recipe_detail_likeNum);
        TextView recipeDescription = findViewById(R.id.recipe_detail_description);
        TextView userName = findViewById(R.id.recipe_detail_userName);
        TextView recipeCals = findViewById(R.id.recipe_detail_calsNum);
        ToggleButton recipeLikeToggle = findViewById(R.id.recipe_detail_likeToggle);

        if (recipe.getRecipeImg()!=null) {
            Picasso.get().load(recipe.getRecipeImg()).centerCrop().fit().into(recipeImg);
        }
        recipeCals.setText("");
        recipeName.setText(recipe.getRecipeName());
        recipeCookingTime.setText("~" + recipe.getCookingMinutes() + " Mins");
        recipeLike.setText(recipe.getLike() + " Likes");
        if (recipe.getDescription()!=null){
            recipeDescription.setText(recipe.getDescription());
        }else {
            recipeDescription.setText("This is a recipe of " + recipe.getRecipeName());
        }
        userName.setText(recipe.getUserName());
        recipeLikeToggle.setChecked(recipe.getIsRecipeLike());

        recipeLikeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                detailViewModel.handleLikeRecipe(isChecked, recipe.getRecipeID());

                //Set the like number when toggle
                if (recipe.getIsRecipeLike()){
                    recipeLike.setText(isChecked ? (recipe.getLike() + " Likes") : ((recipe.getLike() - 1) + " Likes"));
                }else {
                    recipeLike.setText(isChecked ? (recipe.getLike() + 1 + " Likes") : (recipe.getLike() + " Likes"));
                }
                compoundButton.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        compoundButton.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
            }
        });
    }

    private void handleRecyclerView(RecipeModel recipe) {
        detailIngredientsAdapter.setDetailIngredientsAdapter(recipe.getIngredients(), recipe.getIngredients().size());
        detailStepAdapter.setDetailStepAdapter(recipe.getSteps(), recipe.getSteps().size());

        detailIngredientsAdapter.notifyDataSetChanged();
        detailStepAdapter.notifyDataSetChanged();
    }
}
