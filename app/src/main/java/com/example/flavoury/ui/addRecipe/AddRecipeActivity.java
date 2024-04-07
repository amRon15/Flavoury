package com.example.flavoury.ui.addRecipe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    RecyclerView addRecipeIngredient, addRecipeStep;
    ImageButton addIngredient, addStep;
    Button addRecipe, cancelRecipe;
    EditText editRecipeName, editDescription, editDuration;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<AddRecipeModel.Ingredient> ingredients = new ArrayList<>();
    ArrayList<AddRecipeModel.Step> steps = new ArrayList<>();
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

        ingredients.add(new AddRecipeModel.Ingredient());
        steps.add(new AddRecipeModel.Step());
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        addRecipeViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        addRecipeViewModel.fetchMyUserID();
        addRecipeViewModel.getUserID().observe(this, this::setView);

        handleRecyclerView();
    }

    private void handleRecyclerView() {
        addRecipeIngredient = findViewById(R.id.add_recipe_ingredient_recyclerView);
        addRecipeStep = findViewById(R.id.add_recipe_step_recyclerView);

        addRecipeStepAdapter = new AddRecipeStepAdapter();
        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter();

        addRecipeIngredient.setAdapter(addRecipeIngredientAdapter);
        addRecipeIngredient.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addRecipeIngredientAdapter.setAddRecipeIngredientAdapter(ingredients, isRecipeReady);

        addRecipeStep.setAdapter(addRecipeStepAdapter);
        addRecipeStep.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addRecipeStepAdapter.setAddRecipeStepAdapter(steps, isRecipeReady);

    }

    private void setView(String userID) {

        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);
        editDuration = findViewById(R.id.add_recipe_recipeTime);
        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);
        addRecipe = findViewById(R.id.add_recipe_addBtn);
        cancelRecipe = findViewById(R.id.add_recipe_cancelBtn);

        addRecipe.setEnabled(true);
        editRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recipeName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                description = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                duration = charSequence.toString();

                try {
                    cookingMinutes = Integer.parseInt(charSequence.toString());
                }catch (NumberFormatException e){
                    cookingMinutes = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addIngredientList.add(new AddRecipeModel());
                ingredients.add(new AddRecipeModel.Ingredient());
                addRecipeIngredientAdapter.notifyItemRangeChanged(0,ingredients.size());
                scaleAnim(view);
            }
        });
        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addStepList.add(new AddRecipeModel());
                steps.add(new AddRecipeModel.Step());
                addRecipeStepAdapter.notifyDataSetChanged();
                scaleAnim(view);
            }
        });
        cancelRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeName==null || duration==null || ingredients.get(0).getIngredient()==null || steps.get(0).getStep()==null){
                    Toast.makeText(getApplicationContext(),"Required Field Must Not Be Empty",Toast.LENGTH_LONG).show();
                    Log.d("FAILED","REQUIRED");
                }else {
                    AddRecipeModel recipe = new AddRecipeModel(recipeName, description, cookingMinutes, ingredients, steps, userID);
                    addRecipeViewModel.addRecipeToDB(recipe, onBackPressedCallback,getApplicationContext());
                }
            }
        });


        editRecipeName.setImeActionLabel("setRecipeName", KeyEvent.KEYCODE_ENTER);
        editDescription.setImeActionLabel("setDescription", KeyEvent.KEYCODE_ENTER);
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