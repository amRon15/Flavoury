package com.example.flavoury.ui.addRecipe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    RecyclerView addRecipeIngredient, addRecipeStep;
    ImageButton addIngredient, addStep;
    TextView addRecipe, cancelRecipe;
    EditText editRecipeName, editDescription;
    Spinner durationSpinner;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredients> ingredients = new ArrayList<>();
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

        ingredients.add(new Ingredients());
        steps.add(new String());
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        durationSpinner = findViewById(R.id.add_recipe_recipeTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.minutes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapter);

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

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addIngredientList.add(new AddRecipeModel());
                ingredients.add(new Ingredients());
                scaleAnim(view);
                addRecipeIngredientAdapter.notifyItemInserted(ingredients.size()-1);
            }
        });
        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addStepList.add(new AddRecipeModel());
                steps.add("");
                scaleAnim(view);
                addRecipeStepAdapter.notifyItemInserted(steps.size()-1);
            }
        });
        cancelRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
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