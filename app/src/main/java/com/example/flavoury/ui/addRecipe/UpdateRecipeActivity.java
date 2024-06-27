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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.material.imageview.ShapeableImageView;

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

public class UpdateRecipeActivity extends AppCompatActivity {
    RecipeModel recipe;
    String uId;
    OnBackPressedCallback onBackPressedCallback;
    DatabaseHelper db = new DatabaseHelper(this);
    ShapeableImageView recipeImg;
    EditText editRecipeName, editDescription;
    TextView saveBtn, backBtn;
    String[] categoryList;
    String recipeName, description, cookingMinutes, category, serving;
    ImageButton addIngredient, addStep;
    Spinner durationSpinner, servingSpinner, categorySpinner;
    AddRecipeStepAdapter addRecipeStepAdapter;
    AddRecipeIngredientAdapter addRecipeIngredientAdapter;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    ArrayList<String> steps = new ArrayList<>();
    RecyclerView ingredientRecyclerView, stepRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        categoryList = getResources().getStringArray(R.array.category);

        editRecipeName = findViewById(R.id.add_recipe_recipeName);
        editDescription = findViewById(R.id.add_recipe_description);
        saveBtn = findViewById(R.id.add_recipe_saveBtn);
        backBtn = findViewById(R.id.add_recipe_cancelBtn);
        recipeImg = findViewById(R.id.add_recipe_recipeImg);

        setView();
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


        recipe = (RecipeModel) getIntent().getSerializableExtra("Recipe");
        uId = recipe.getUid();



        backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });


        //set recipe img

        editRecipeName.setText(recipe.getRName());
        editDescription.setText(recipe.getDescription());

        categorySpinner = findViewById(R.id.add_recipe_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setPrompt(recipe.getCategory());

        durationSpinner = findViewById(R.id.add_recipe_recipeTime);
        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.minutes,
                android.R.layout.simple_spinner_item
        );
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);
        durationSpinner.setPrompt(recipe.getCookTime());

        servingSpinner = findViewById(R.id.add_recipe_recipePortion);
        ArrayAdapter<CharSequence> servingAdapter = ArrayAdapter.createFromResource(this, R.array.serving_size, android.R.layout.simple_spinner_item);
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSpinner.setAdapter(servingAdapter);
        servingSpinner.setPrompt(recipe.getServing());

        addRecipeIngredientAdapter = new AddRecipeIngredientAdapter(recipe.getIngredients());
        ingredientRecyclerView = findViewById(R.id.add_recipe_ingredient_recyclerView);
        ingredientRecyclerView.setAdapter(addRecipeIngredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addRecipeStepAdapter = new AddRecipeStepAdapter(recipe.getSteps());
        stepRecyclerView = findViewById(R.id.add_recipe_step_recyclerView);
        stepRecyclerView.setAdapter(addRecipeStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setView( ) {
        final DatabaseHelper db = new DatabaseHelper(this);
        final String userID = db.getUid();

        addIngredient = findViewById(R.id.add_recipe_add_ingredient);
        addStep = findViewById(R.id.add_recipe_add_step);

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

        saveBtn.setOnClickListener(v -> {
            recipeName = String.valueOf(editRecipeName.getText());
            description = String.valueOf(editDescription.getText());
            cookingMinutes = (String) durationSpinner.getSelectedItem();
            category = (String) categorySpinner.getSelectedItem();
            serving = (String) servingSpinner.getSelectedItem();


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

            recipe = new RecipeModel(userID, recipeName, category, cookingMinutes, description, 0, serving, recipe.getImgid(), ingredients, steps);
//            Log.d("AddRecipeActivitySend", userID + recipeName+ category+ cookingMinutes+ description+ serving+ imgId+ encodeIngredients.get(0).getIngredient()+ encodeStep.get(0));
            updateRecipe(jsonIngredient, jsonStep);
        });

    }

    private void updateRecipe(JSONArray jsonIngredient, JSONArray jsonStep){
        new Thread(()->{
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
                Log.d("AddRecipeActivitySend", "Exception: " + e.toString());

            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void saveRecipeImgToStorage(){

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
