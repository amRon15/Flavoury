package com.example.flavoury.ui.addRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.myProfile.MyProfileFragment;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

public class UpdateRecipeActivity extends AppCompatActivity {
    RecipeModel recipe;
    String ipAddress;
    OnBackPressedCallback onBackPressedCallback;
    DatabaseHelper db = new DatabaseHelper(this);
    ShapeableImageView recipeImg;
    EditText editRecipeName, editDescription;
    Button nextBtn;
    String[] categoryList;
    ImageButton backBtn;
    Spinner durationSpinner, servingSpinner, categorySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        getSupportActionBar().hide();

        recipe = (RecipeModel) getIntent().getSerializableExtra("Recipe");
        categoryList = getResources().getStringArray(R.array.category);
        ipAddress = getResources().getString(R.string.ipAddress);

        setView();
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);



        //set recipe img
        setImg();
    }

    private void setImg(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("recipe").child(recipe.getImgid()+".jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
            }
        });
    }

    private void setView() {
        final DatabaseHelper db = new DatabaseHelper(this);
        final String userID = db.getUid();

        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);
        nextBtn = findViewById(R.id.add_recipe_nextBtn);
        backBtn = findViewById(R.id.add_recipe_cancelBtn);
        recipeImg = findViewById(R.id.add_recipe_recipeImg);

        editRecipeName.setEnabled(false);
        editDescription.setImeActionLabel("setDescription", KeyEvent.KEYCODE_ENTER);

        editRecipeName.setText(recipe.getRName());
        editDescription.setText(recipe.getDescription());

        categorySpinner = findViewById(R.id.add_recipe_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(categoryAdapter.getPosition(recipe.getCategory()));

        durationSpinner = findViewById(R.id.add_recipe_recipeTime);
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.minutes,
                android.R.layout.simple_spinner_item
        );
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);
        durationSpinner.setSelection(durationAdapter.getPosition(recipe.getCookTime()));

        servingSpinner = findViewById(R.id.add_recipe_recipePortion);
        ArrayAdapter<CharSequence> servingAdapter = ArrayAdapter.createFromResource(this, R.array.serving_size, android.R.layout.simple_spinner_item);
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSpinner.setAdapter(servingAdapter);
        servingSpinner.setSelection(servingAdapter.getPosition(recipe.getServing()));

        nextBtn.setOnClickListener(v->{
            String description = String.valueOf(editDescription.getText());
            String cookTime = (String) durationSpinner.getSelectedItem();
            String category = (String) categorySpinner.getSelectedItem();
            String serving = (String) servingSpinner.getSelectedItem();


            if (!description.isEmpty() && !cookTime.isEmpty() && !category.isEmpty() && !serving.isEmpty()) {
                recipe.setUid(userID);
                recipe.setDescription(description);
                recipe.setCookTime(cookTime);
                recipe.setCategory(category);
                recipe.setServing(serving);
                recipe.setLikes(0);

                Intent intent = new Intent(this, UpdateStepIngActivity.class);
                intent.putExtra("Recipe", recipe);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
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
