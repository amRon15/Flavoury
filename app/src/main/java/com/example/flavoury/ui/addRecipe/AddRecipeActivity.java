package com.example.flavoury.ui.addRecipe;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.RoundCornerTransform;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AddRecipeActivity extends AppCompatActivity {

    RecyclerView ingredientRecyclerView, stepRecyclerView;
    ImageButton addIngredient, addStep, recipeImg;
    TextView addRecipe, cancelRecipe;
    EditText editRecipeName, editDescription;
    Spinner durationSpinner, servingSpinner, categorySpinner;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredients> ingredients = new ArrayList<>();
    String[] categoryList;
    ArrayList<String> steps = new ArrayList<>();
    String recipeName, description, duration, cookingMinutes;
    OnBackPressedCallback onBackPressedCallback;
    AddRecipeModel  recipe;
    AddRecipeViewModel addRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().hide();

        ingredients.add(new Ingredients());
        steps.add("");

        categoryList = getResources().getStringArray(R.array.category);

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        // Category, Minutes, Serving Size dropdown
        categorySpinner = findViewById(R.id.add_recipe_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.serving_size, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        durationSpinner = findViewById(R.id.add_recipe_recipeTime);
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.minutes,
                android.R.layout.simple_spinner_item
        );
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);

        servingSpinner = findViewById(R.id.add_recipe_recipePortion);
        ArrayAdapter<CharSequence> servingAdapter = ArrayAdapter.createFromResource(this, R.array.serving_size, android.R.layout.simple_spinner_item);
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSpinner.setAdapter(servingAdapter);

        //back btn
        cancelRecipe = findViewById(R.id.add_recipe_cancelBtn);
        cancelRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        //recipe img -> open photo picker
        recipeImg = findViewById(R.id.add_recipe_recipeImg);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
//                Log.d("PhotoPicker", "Selected uri " + uri);
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                recipeImg.setElevation(100);

            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });
        // click to launch photo picker
        recipeImg.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter(ingredients);
        ingredientRecyclerView = findViewById(R.id.add_recipe_ingredient_recyclerView);
        ingredientRecyclerView.setAdapter(addRecipeIngredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addRecipeStepAdapter = new AddRecipeStepAdapter(steps);
        stepRecyclerView = findViewById(R.id.add_recipe_step_recyclerView);
        stepRecyclerView.setAdapter(addRecipeStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        setView();
    }

    private void handleRecyclerView() {
    }

    private void setView( ) {
        final DatabaseHelper db = new DatabaseHelper(this);
        final String userID = db.getUid();
        Log.d("User", userID);
        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);

        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);
        addRecipe = findViewById(R.id.add_recipe_saveBtn);



        addIngredient.setOnClickListener(view -> {
            ingredients.add(new Ingredients());
            addRecipeIngredientAdapter.ingredients = ingredients;
            Log.d("AddAdapter", "Data: " + ingredients.get(ingredients.size()-1).getIngredient());
            addRecipeIngredientAdapter.notifyItemInserted(ingredients.size()-1);
            scaleAnim(view);
        });
        addStep.setOnClickListener(view -> {
            steps.add("");
            addRecipeStepAdapter.steps = steps;
            addRecipeStepAdapter.notifyItemInserted(steps.size() - 1);
            scaleAnim(view);
        });

        editRecipeName.setImeActionLabel("setRecipeName", KeyEvent.KEYCODE_ENTER);
        editDescription.setImeActionLabel("setDescription", KeyEvent.KEYCODE_ENTER);

        addRecipe.setOnClickListener(v -> {
            recipeName = String.valueOf(editRecipeName.getText());
            description = String.valueOf(editDescription.getText());
            cookingMinutes = (String) durationSpinner.getSelectedItem();
            String category = (String) categorySpinner.getSelectedItem();
            String serving = (String) servingSpinner.getSelectedItem();

        });

    }

    private void scaleAnim(View view) {
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1).scaleY(1).setDuration(100);
            }
        });
    }
}