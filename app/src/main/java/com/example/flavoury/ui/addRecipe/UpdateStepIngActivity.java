package com.example.flavoury.ui.addRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UpdateStepIngActivity extends AppCompatActivity {
    String ipAddress;
    RecipeModel recipe;
    OnBackPressedCallback onBackPressedCallback;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> steps;
    RecyclerView ingredientRecyclerView, stepRecyclerView;
    Button saveBtn;
    ImageButton backBtn, addIngredient, addStep;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step_ingredient);
        getSupportActionBar().hide();

        recipe = (RecipeModel) getIntent().getSerializableExtra("Recipe");
        ipAddress = getResources().getString(R.string.ipAddress);
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        setView();

    }

    private void setView(){
        saveBtn = findViewById(R.id.add_step_saveBtn);
        backBtn = findViewById(R.id.add_step_cancelBtn);
        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);

        ingredients = recipe.getIngredients();
        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter(ingredients);
        ingredientRecyclerView = findViewById(R.id.add_recipe_ingredient_recyclerView);
        ingredientRecyclerView.setAdapter(addRecipeIngredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        steps = recipe.getSteps();
        addRecipeStepAdapter = new AddRecipeStepAdapter(steps);
        stepRecyclerView = findViewById(R.id.add_recipe_step_recyclerView);
        stepRecyclerView.setAdapter(addRecipeStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        addIngredient.setOnClickListener(view -> {
            ingredients.add(new Ingredient());
            addRecipeIngredientAdapter.ingredients = ingredients;
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

            updateRecipe(jsonIngredient, jsonStep);
        });
    }

    private void updateRecipe(JSONArray jsonIngredient, JSONArray jsonStep) {
        Thread updateThread = new Thread(() -> {
            try {
                URL url = new URL(ipAddress + "app_update_recipe.php");

                String recipeParam = "Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8") +
                        "&Uid=" + URLEncoder.encode(recipe.getUid(), "UTF-8") +
                        "&RName=" + URLEncoder.encode(recipe.getRName(), "UTF-8") +
                        "&Category=" + URLEncoder.encode(recipe.getCategory(), "UTF-8") +
                        "&CookTime=" + URLEncoder.encode(recipe.getCookTime(), "UTF-8") +
                        "&Description=" + URLEncoder.encode(recipe.getDescription(), "UTF-8") +
                        "&Serving=" + URLEncoder.encode(recipe.getServing(), "UTF-8")+
                        "&Imgid=" + URLEncoder.encode(recipe.getImgid(), "UTF-8") +
                        "&Step=" + jsonStep +
                        "&Ingredient=" + jsonIngredient;


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(recipeParam);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");

                    JSONObject jsonObject = new JSONObject(jsonResponseString);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.d("AddRecipeActivitySend", jsonResponseString);
                    runOnUiThread(() -> {
                        if (status.equals("success")) {
                            Toast.makeText(this, "Update recipe successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                    connection.disconnect();
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("AddRecipeActivitySend", "Exception: " + e.toString());

            }
        });

        updateThread.start();
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
