package com.example.flavoury.ui.addRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    ShapeableImageView recipeImg;
    Button nextBtn;
    ImageButton cancelRecipe;
    EditText editRecipeName, editDescription;
    Spinner durationSpinner, servingSpinner, categorySpinner;
    String[] categoryList;
    String uId, ipAddress;
    OnBackPressedCallback onBackPressedCallback;
    RecipeModel recipe;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        db = new DatabaseHelper(this);
        db.onCreate(db.getWritableDatabase());

        uId = db.getUid();

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
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
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
        cancelRecipe.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        //recipe img -> open photo picker
        recipeImg = findViewById(R.id.add_recipe_recipeImg);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
                recipeImg.setImageURI(uri);
                recipeImg.setElevation(100);
                recipe.setImgUri(uri);
                recipe.setImgid(UUID.randomUUID().toString());
            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });

        // click to launch photo picker
        recipeImg.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        setView();
    }

    private void setView() {
        final DatabaseHelper db = new DatabaseHelper(this);
        final String userID = db.getUid();
        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);
        nextBtn = findViewById(R.id.add_recipe_nextBtn);

        editRecipeName.setImeActionLabel("setRecipeName", KeyEvent.KEYCODE_ENTER);
        editDescription.setImeActionLabel("setDescription", KeyEvent.KEYCODE_ENTER);

        nextBtn.setOnClickListener(v -> {
            String recipeName = String.valueOf(editRecipeName.getText());
            String description = String.valueOf(editDescription.getText());
            String cookTime = (String) durationSpinner.getSelectedItem();
            String category = (String) categorySpinner.getSelectedItem();
            String serving = (String) servingSpinner.getSelectedItem();


            if (!recipeName.isEmpty() && !description.isEmpty() && !cookTime.isEmpty() && !category.isEmpty() && !serving.isEmpty() && !recipe.getImgid().isEmpty()) {
                recipe.setRName(recipeName);
                recipe.setUid(userID);
                recipe.setDescription(description);
                recipe.setCookTime(cookTime);
                recipe.setCategory(category);
                recipe.setServing(serving);
                recipe.setLikes(0);

                Intent intent = new Intent(v.getContext(), AddStepIngredientActivity.class);
                intent.putExtra("Recipe", recipe);
                startActivity(intent);
            }
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