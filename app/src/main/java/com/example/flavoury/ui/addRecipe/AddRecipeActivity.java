package com.example.flavoury.ui.addRecipe;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    RecyclerView addRecipeIngredient, addRecipeStep, addRecipeCategory;
    ImageButton addIngredient, addStep;
    TextView addRecipe, cancelRecipe;
    EditText editRecipeName, editDescription;
    Spinner durationSpinner, servingSpinner, categorySpinner;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredients> ingredients = new ArrayList<>();
    String[] categoryList;
    ArrayList<String> steps = new ArrayList<>();
    boolean isRecipeReady;
    String recipeName, description, duration;
    int cookingMinutes;
    OnBackPressedCallback onBackPressedCallback;

    AddRecipeViewModel addRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().hide();


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

    }



    private void handleRecyclerView() {
        addRecipeIngredient = findViewById(R.id.add_recipe_ingredient_recyclerView);
        addRecipeStep = findViewById(R.id.add_recipe_step_recyclerView);

        addRecipeStepAdapter = new AddRecipeStepAdapter();
        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter();

        addRecipeIngredient.setAdapter(addRecipeIngredientAdapter);
        addRecipeIngredient.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addRecipeIngredientAdapter.setAddRecipeIngredientAdapter(ingredients);

        addRecipeStep.setAdapter(addRecipeStepAdapter);
        addRecipeStep.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addRecipeStepAdapter.setAddRecipeStepAdapter(steps);


    }

    private void setView(String userID) {
        Log.d("USer", userID);
        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);

        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);
        addRecipe = findViewById(R.id.add_recipe_saveBtn);

        recipeName = String.valueOf(editRecipeName.getText());
        description = String.valueOf(editDescription.getText());

        addIngredient.setOnClickListener(view -> {
//                addIngredientList.add(new AddRecipeModel());
            ArrayList<Ingredients> newIngredients = ingredients;
            newIngredients.add(ingredients.size(), new Ingredients());
            ingredients.clear();
            ingredients.addAll(newIngredients);
            addRecipeIngredientAdapter.notifyItemInserted(ingredients.size());
            Log.d("Ingredients", "Ingredients: " + ingredients + "\nNew Ingredients: " + newIngredients);
            scaleAnim(view);
            addRecipeIngredientAdapter.notifyItemInserted(ingredients.size() - 1);
        });
        addStep.setOnClickListener(view -> {
//                addStepList.add(new AddRecipeModel());
            steps.add("");
            scaleAnim(view);
            addRecipeStepAdapter.notifyItemInserted(steps.size() - 1);
        });

        editRecipeName.setImeActionLabel("setRecipeName", KeyEvent.KEYCODE_ENTER);
        editDescription.setImeActionLabel("setDescription", KeyEvent.KEYCODE_ENTER);

        addRecipe.setOnClickListener(v -> {

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