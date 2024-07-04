package com.example.flavoury.ui.addRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddStepIngredientActivity extends AppCompatActivity {
    String ipAddress;
    ImageButton cancelBtn, addIngredient, addStep;
    Button saveBtn;
    RecyclerView ingredientRecyclerView, stepRecyclerView;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<String> steps = new ArrayList<>();
    RecipeModel recipe;
    OnBackPressedCallback onBackPressedCallback;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("recipe");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step_ingredient);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        recipe = (RecipeModel) getIntent().getSerializableExtra("Recipe");

        ingredients.add(new Ingredient());
        steps.add("");

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter(ingredients);
        ingredientRecyclerView = findViewById(R.id.add_recipe_ingredient_recyclerView);
        ingredientRecyclerView.setAdapter(addRecipeIngredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addRecipeStepAdapter = new AddRecipeStepAdapter(steps);
        stepRecyclerView = findViewById(R.id.add_recipe_step_recyclerView);
        stepRecyclerView.setAdapter(addRecipeStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cancelBtn = findViewById(R.id.add_step_cancelBtn);
        cancelBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        setView();
    }

    private void setView() {
        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);
        saveBtn = findViewById(R.id.add_step_saveBtn);

        addIngredient.setOnClickListener(view -> {
            ingredients.add(new Ingredient());
            addRecipeIngredientAdapter.ingredients = ingredients;
            Log.d("AddAdapter", "Data: " + ingredients.get(ingredients.size() - 1).getIngredient());
            addRecipeIngredientAdapter.notifyItemInserted(ingredients.size() - 1);
            scaleAnim(view);
        });

        addStep.setOnClickListener(view -> {
            steps.add("");
            addRecipeStepAdapter.steps = steps;
            addRecipeStepAdapter.notifyItemInserted(steps.size() - 1);
            scaleAnim(view);
        });

        saveBtn.setOnClickListener(v -> {
            JSONArray jsonIngredient = new JSONArray();
            try {
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ingredient", ingredient.getIngredient());
                    jsonObject.put("portion", ingredient.getPortion());
                    jsonIngredient.put(jsonObject);
                }
            } catch (Exception e) {
                Log.d("AddRecipeActivitySend", "Ingredient loop error: " + e.getMessage());
            }

            JSONArray jsonStep = new JSONArray();
            for (int i = 0; i < steps.size(); i++) {
                String step = steps.get(i);
                jsonStep.put(step);
            }

            sendRecipe(recipe, jsonIngredient, jsonStep);
        });
    }


    private void sendRecipe(RecipeModel recipe, JSONArray jsonIngredient, JSONArray jsonStep) {
        Thread addRecipeThread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress + "app_create_recipe.php");

                //param
                String recipeParam = "Uid=" + URLEncoder.encode(recipe.getUid(), "UTF-8") +
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

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("AddRecipeActivitySend", jsonResponseString);
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")) {
                                saveRecipeImgToStorage();
                                Toast.makeText(this, "Upload recipe successful", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("AddRecipeActivitySend", "JSON Error: " + e.getMessage());
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AddRecipeActivitySend", "Exception: " + e.toString());

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        addRecipeThread.start();
    }

    private void saveRecipeImgToStorage() {
        StorageReference imgRef = storageRef.child(recipe.getImgid() + ".jpg");
        Uri uri = Uri.parse(recipe.getImgUri());
        UploadTask uploadTask = imgRef.putFile(uri);
        Log.d("FirebaseStorage", imgRef.getPath());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("StepIngredient", "Upload image successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("StepIngredient", "Failed to upload image");
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

