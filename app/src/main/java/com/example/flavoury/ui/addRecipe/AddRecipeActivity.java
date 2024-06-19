package com.example.flavoury.ui.addRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.Ingredients;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    RecyclerView ingredientRecyclerView, stepRecyclerView;
    ImageButton addIngredient, addStep;
    ShapeableImageView recipeImg;
    TextView addRecipe, cancelRecipe;
    EditText editRecipeName, editDescription;
    Spinner durationSpinner, servingSpinner, categorySpinner;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    String[] categoryList;
    ArrayList<String> steps = new ArrayList<>();
    String uId, recipeName, description, imgId, cookingMinutes , result;
    Uri imgUri;
    OnBackPressedCallback onBackPressedCallback;
    RecipeModel recipe;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("recipe");
    UploadTask uploadTask;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().hide();
        db = new DatabaseHelper(this);
        db.onCreate(db.getWritableDatabase());

        uId = db.getUid();

        ingredients.add(new Ingredient());
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
                recipeImg.setImageURI(uri);
                recipeImg.setElevation(100);
                imgUri = uri;
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
            ingredients.add(new Ingredient());
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

            imgId = UUID.randomUUID().toString();

            JSONArray jsonIngredient = new JSONArray();
            try {
            for (int i=0;i<ingredients.size();i++){
                Ingredient ingredient = ingredients.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ingredient", ingredient.getIngredient());
                jsonObject.put("portion", ingredient.getPortion());
                jsonIngredient.put(jsonObject);
            }
            }catch (Exception e){
                Log.d("AddRecipeActivitySend", "Ingredient loop error: "+e.getMessage());
            }

            JSONArray jsonStep = new JSONArray();
            for(int i=0; i< steps.size();i++){
                String step = steps.get(i);
                jsonStep.put(step);
            }

            recipe = new RecipeModel(userID, recipeName, category, cookingMinutes, description, 0, serving, imgId, ingredients, steps);
//            Log.d("AddRecipeActivitySend", userID + recipeName+ category+ cookingMinutes+ description+ serving+ imgId+ encodeIngredients.get(0).getIngredient()+ encodeStep.get(0));
            sendRecipe(recipe, jsonIngredient, jsonStep);
        });

    }

    private void sendRecipe(RecipeModel recipe, JSONArray jsonIngredient, JSONArray jsonStep){
        Thread addRecipeThread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_create_recipe.php");

                //param
                String recipeParam = "Uid=" + URLEncoder.encode(uId, "UTF-8") +
                        "&RName=" + URLEncoder.encode(recipe.getRName(), "UTF-8") +
                        "&Category=" + URLEncoder.encode(recipe.getCategory(), "UTF-8") +
                        "&CookTime=" + URLEncoder.encode(recipe.getCookTime(), "UTF-8") +
                        "&Description=" + URLEncoder.encode(recipe.getDescription(), "UTF-8") +
                        "&Serving=" + URLEncoder.encode(recipe.getServing(), "UTF-8") +
                        "&Imgid=" + URLEncoder.encode(recipe.getImgid(), "UTF-8") +
                        "&Step=" + jsonStep +
                        "&Ingredient=" + jsonIngredient;

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(recipeParam);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    Log.d("AddRecipeActivitySend", "HTTP OK");
                    while ((line = bufferedReader.readLine()) != null){
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("AddRecipeActivitySend", jsonResponseString);
                    runOnUiThread(() -> {
                        try{
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Log.d("AddRecipeActivitySend", jsonObject.toString());
                            if (status.equals("success")){
                                saveRecipeImgToStorage();
                                Toast.makeText(this, "Upload recipe successful", Toast.LENGTH_LONG).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            }else {
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("AddRecipeActivitySend", "JSON Error: "+e.getMessage());
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "HTTP Error: "+ responseCode, Toast.LENGTH_LONG).show());
                }
            }catch (Exception e){
                e.printStackTrace();
//                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("AddRecipeActivitySend", "Exception: " + e.toString());

            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        });
        addRecipeThread.start();
    }

    private Runnable mutilThread = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://10.0.0.2/Favoury/app_create_recipe.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                    String box = "";
                    String line = null;
                    while ((line = bufferedReader.readLine()) !=null){
                        box += line+"\n";
                    }
                    inputStream.close();
                    result = box;
                }
            }catch (Exception e){
                Log.d("AddRecipeActivity", e.getMessage());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("AddRecipeActivity", "Successful");
                }
            });
        }
    };

    private void saveRecipeImgToStorage(){
        StorageReference imgRef = storageRef.child(imgId+".jpg");
        uploadTask = imgRef.putFile(imgUri);
        Log.d("FirebaseStorage", imgRef.getPath());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Log.d("FirebaseStorage", taskSnapshot.getMetadata()+"");
                Toast.makeText(getApplicationContext(), "Upload image successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
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