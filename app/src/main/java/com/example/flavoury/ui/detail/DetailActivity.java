package com.example.flavoury.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    RecipeModel detailRecipe;
    RecyclerView detailStepRecyclerView, detail_ingredients_recyclerview;
    DetailStepAdapter detailStepAdapter;
    Detail_IngredientsAdapter detailIngredientsAdapter;
    ImageButton recipe_detail_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        recipe_detail_backBtn = findViewById(R.id.recipe_detail_back_btn);
        recipe_detail_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle fromHomeFragment = getIntent().getExtras();
        if (fromHomeFragment != null) {
            detailRecipe = (RecipeModel) getIntent().getSerializableExtra("detailRecipe");
        }

        Intent fromRecipe = getIntent();
        String intentFromRecipe = fromRecipe.getStringExtra("detailRecipeID");


        detailStepRecyclerView = findViewById(R.id.detail_step_recyclerView);
        detail_ingredients_recyclerview = findViewById(R.id.detail_ingredients_recyclerview);

        detailStepAdapter = new DetailStepAdapter();
        detailIngredientsAdapter = new Detail_IngredientsAdapter();

        detailStepRecyclerView.setAdapter(detailStepAdapter);
        detailStepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        detail_ingredients_recyclerview.setAdapter(detailIngredientsAdapter);
        detail_ingredients_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DetailViewModel detailViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DetailViewModel.class);
        detailViewModel.setRecipeID(intentFromRecipe);
        detailViewModel.fetchRecipe();

        detailViewModel.getRecipeList().observe(this, new Observer<RecipeModel>() {
            @Override
            public void onChanged(RecipeModel recipeModels) {
                handleRecyclerView(recipeModels);
                setView(recipeModels);
            }
        });
    }

    private void setView(RecipeModel recipe) {
        //ImageView recipeImg = findViewById(R.id.recipe_detail_recipeImg);
        TextView recipeName = findViewById(R.id.recipe_detail_recipeName);
        TextView recipeCookingTime = findViewById(R.id.recipe_detail_cookingMins);
        TextView recipeLike = findViewById(R.id.recipe_detail_likeNum);
        TextView recipeDescription = findViewById(R.id.recipe_detail_description);
        TextView userName = findViewById(R.id.recipe_detail_userName);
        TextView recipeCals = findViewById(R.id.recipe_detail_calsNum);

        recipeCals.setText("");
        recipeName.setText(recipe.getRecipeName());
        recipeCookingTime.setText("~" + recipe.getCookingMinutes() + " Mins");
        recipeLike.setText(recipe.getLike() + " Likes");
        recipeDescription.setText("This is a recipe of " + recipe.getRecipeName());
        userName.setText(recipe.getUserName());
    }

    private void handleRecyclerView(RecipeModel recipe) {
        detailIngredientsAdapter.setDetailIngredientsAdapter(recipe.getIngredients(), recipe.getIngredients().size());
        detailStepAdapter.setDetailStepAdapter(recipe.getStep(), recipe.getStep().size());

        detailIngredientsAdapter.notifyDataSetChanged();
        detailStepAdapter.notifyDataSetChanged();
    }
}
