package com.example.flavoury.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    String[] detailArray = {"step1", "step2", "step3", "step4", "step5"};
    String[] foodArray = {"6 Eggs", "2 tsp of sugar", "Some Orange Peel"};
    RecipeModel detailRecipe;
    RecyclerView detailStepRecyclerView, detail_ingredients_recyclerview;
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
        if(fromHomeFragment!=null) {
            detailRecipe = (RecipeModel) getIntent().getSerializableExtra("detailRecipe");
        }

        detailStepRecyclerView = findViewById(R.id.detail_step_recyclerView);
        DetailStepAdapter detailStepAdapter = new DetailStepAdapter(detailArray);
        detailStepRecyclerView.setAdapter(detailStepAdapter);
        detailStepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        detail_ingredients_recyclerview = findViewById(R.id.detail_ingredients_recyclerview);
        Detail_IngredientsAdapter detailIngredientsAdapter = new Detail_IngredientsAdapter(foodArray,detailRecipe);
        detail_ingredients_recyclerview.setAdapter(detailIngredientsAdapter);
        detail_ingredients_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setView(){
        //ImageView recipeImg = findViewById(R.id.recipe_detail_recipeImg);
        TextView recipeName = findViewById(R.id.recipe_detail_recipeName);
        TextView recipeCookingTime = findViewById(R.id.recipe_detail_cookingMins);
        TextView recipeLike = findViewById(R.id.recipe_detail_likeNum);
        //TextView recipeDescription = findViewById(R.id.recipe_detail_description);

        //TextView userName = findViewById(R.id.recipe_detail_userName);

    }
}
