package com.example.flavoury.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.Ingredient;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

//Get data from firebase and post to new database
public class FireBaseToDB extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference recipeFromDb = db.collection("recipe");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("recipe");
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    String Uid = "1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_firebasetodb);
        getSupportActionBar().hide();

        Button getBtn = findViewById(R.id.firebasetodb_get);
        Button postBtn = findViewById(R.id.firebasetodb_post);
        Button viewBtn = findViewById(R.id.firebasetodb_view);

        viewBtn.setOnClickListener(v->{
            viewData();
        });

        getBtn.setOnClickListener(v->{
            getFromFB();
        });

        postBtn.setOnClickListener(v->{
            postToDB(recipeModelArrayList);
        });
    }

    private void getFromFB(){
        recipeFromDb.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    RecipeModel recipe = new RecipeModel();

                    String rname = (String) queryDocumentSnapshot.getData().get("recipeName");
                    String category = (String) queryDocumentSnapshot.getData().get("category");
                    Long minutes = (Long) queryDocumentSnapshot.getData().get("cookingMinutes");
                    String description = "This is a " + rname + " recipe.";
                    int cookMinutes = minutes.intValue();
                    String cookTime;
                    if (cookMinutes > 120){
                        cookTime = "> 120 mins";
                    }else {
                        cookTime = cookMinutes + " mins";
                    }
                    String imgId = (String) queryDocumentSnapshot.getData().get("recipeImg");
                    String serving = "1 Serving";
                    ArrayList<String> steps = (ArrayList<String>) queryDocumentSnapshot.get("steps");
                    ArrayList<Object> ingredients = (ArrayList<Object>) queryDocumentSnapshot.get("ingredients");

                    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
                    for (Object ig : ingredients){
                        Map<String, String> object = (Map<String, String>) ig;
                        String ingredient = object.get("ingredient");
                        String portion = object.get("portion");
                        Ingredient ingredient1 = new Ingredient(ingredient, portion);
                        ingredientArrayList.add(ingredient1);
                    }


                    recipe.setUid(Uid);
                    if (!category.equals("vegan")) {
                        recipe.setCookTime(cookTime);
                        recipe.setDescription(description);
                        recipe.setServing(serving);
                        recipe.setIngredients(ingredientArrayList);
                        recipe.setSteps(steps);
                        recipe.setRName(rname);
                        recipe.setImgid(imgId);
                        if (category.equals("chinese")) {
                            recipe.setCategory("Chinese");
                        } else if (category.equals("western")) {
                            recipe.setCategory("European");
                        } else if (category.equals("fitness")) {
                            recipe.setCategory("Fitness");
                        } else if (category.equals("japanese")) {
                            recipe.setCategory("Japanese");
                        }
                        recipeModelArrayList.add(recipe);
                    }



                }
            }
        });
    }

    private void saveImg(Uri uri, String UUID){
        StorageReference recipeRef = storageReference.child(UUID+".jpg");
        recipeRef.putFile(uri);
    }

    private void postToDB(ArrayList<RecipeModel> recipes){
        for (RecipeModel recipe : recipes) {
            JSONArray jsonStep = new JSONArray();
            JSONArray jsonIngredient = new JSONArray();
            try {
                for (int i=0;i<recipe.getIngredients().size();i++){
                    Ingredient ingredient = recipe.getIngredients().get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ingredient", ingredient.getIngredient());
                    jsonObject.put("portion", ingredient.getPortion());
                    jsonIngredient.put(jsonObject);
                }
                for(int i=0; i< recipe.getSteps().size();i++){
                    String step = recipe.getSteps().get(i);
                    jsonStep.put(step);
                }
            }catch (Exception e){
                Log.d("AddRecipeActivitySend", "Ingredient loop error: "+e.getMessage());
            }finally {
            new Thread(() -> {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://10.0.2.2/Flavoury/app_create_recipe.php");
                    String uuId = String.valueOf(UUID.randomUUID());
                    //param
                    String recipeParam = "Uid=" + URLEncoder.encode(Uid, "UTF-8") +
                            "&RName=" + URLEncoder.encode(recipe.getRName(), "UTF-8") +
                            "&Category=" + URLEncoder.encode(recipe.getCategory(), "UTF-8") +
                            "&CookTime=" + URLEncoder.encode(recipe.getCookTime(), "UTF-8") +
                            "&Description=" + URLEncoder.encode(recipe.getDescription(), "UTF-8") +
                            "&Serving=" + URLEncoder.encode(recipe.getServing(), "UTF-8") +
                            "&Imgid=" + URLEncoder.encode(uuId, "UTF-8") +
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
                                    Toast.makeText(this, "Upload recipe successful", Toast.LENGTH_LONG).show();
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
            }).start();}
        }
    }

    private void viewData(){}
}
