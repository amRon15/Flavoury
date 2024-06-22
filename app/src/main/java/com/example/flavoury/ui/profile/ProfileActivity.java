package com.example.flavoury.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ShapeableImageView userIcon;
    TextView userName;
    ToggleButton followBtn;
    ImageButton backBtn;
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    RecyclerView recipeRecyclerView;
    ProfileRecipeAdapter profileRecipeAdapter;
    String uId, otherUid;
    boolean isUserFollowed;
    DatabaseHelper db = new DatabaseHelper(this);
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        db.onCreate(db.getWritableDatabase());
        uId = db.getUid();

        Intent uIdIntent = getIntent();
        otherUid = uIdIntent.getStringExtra("otherUid");

        getUserInfo(otherUid);
        getRecipe(otherUid);
        if (!uId.isEmpty() & !otherUid.isEmpty()){
            isUserFollowed();
        }

        userIcon = findViewById(R.id.profile_userIcon);
        userName = findViewById(R.id.profile_userName);
        followBtn = findViewById(R.id.profile_followBtn);
        backBtn = findViewById(R.id.profile_backBtn);
        recipeRecyclerView = findViewById(R.id.profile_recipe_recyclerView);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        followBtn.setOnClickListener(v -> followUser());


        profileRecipeAdapter = new ProfileRecipeAdapter(recipeModelArrayList);
        recipeRecyclerView.setAdapter(profileRecipeAdapter);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));



    }

    private void getUserInfo(String Uid) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/profile.php?Uid=" + Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.d("UsernameFromDB", Uid);
                Log.d("UsernameFromDB", response.toString());


                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                final String Username = jsonObject.getString("Username");
                final String UserIconId = jsonObject.getString("Iconid");
                setUserIcon(UserIconId);

                Log.d("UsernameFromDB", jsonObject.toString());

                if (Username.isEmpty()) {
                    Log.d("canfind", Username);
                    userName.setText("Undefined");
                }

                runOnUiThread(() -> {
                    userName.setText(Username);
                });

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d("UsernameFromDB", e.getMessage());
                runOnUiThread(() -> {
                    userName.setText("Undefined");
                });
            }
        }).start();
    }

    private void getRecipe(String Uid) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_my_user_recipe.php?Uid=" + Uid);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                if (!jsonResponseString.isEmpty()) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RecipeModel recipeModel = new RecipeModel(jsonObject);
                        recipeModelArrayList.add(recipeModel);
                    }
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.d("MyProfileGetRecipe", "Catch error :" + e.toString());
            }
        }).start();
    }

    private void isUserFollowed(){
        new Thread(()->{
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_is_user_followed.php?Uid="+uId+"&Followid="+otherUid);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line=reader.readLine()) != null){
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                Log.d("ProfileFetchData", jsonResponseString);
                JSONObject jsonObject = null;
                if (!jsonResponseString.isEmpty()){
                    jsonObject = new JSONObject(jsonResponseString);
                }

                if (jsonObject.getString("status")=="success"){
                    isUserFollowed = true;
                }else {
                    isUserFollowed = false;
                }

                runOnUiThread(()->{
                    followBtn.setChecked(isUserFollowed);
                });

            }catch (Exception e){
                Log.d("MyProfileGetRecipe", "Catch error :" + e.toString());
            }
        }).start();
    }

    private void followUser() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url;
                if (isUserFollowed){
                    url = new URL("http://10.0.2.2/Flavoury/app_follow_user.php");
                }else {
                    url = new URL("http://10.0.2.2/Flavoury/app_delete_follow.php");
                }
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                String followUserParams = "Uid=" + URLEncoder.encode(uId, "UTF-8") +
                        "&Followid=" + URLEncoder.encode(otherUid, "UTF-8");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(followUserParams.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    Log.d("ProfileFetchData", "HTTP OK");
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("ProfileFetchData", jsonResponseString);
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(jsonResponseString);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if (status.equals("success")) {
                                Log.d("ProfileFetchData", message);
                            } else {
                                Log.d("ProfileFetchData", message);
                            }
                        } catch (JSONException e) {
                            Log.d("ProfileFetchData", e.toString());
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("ProfileFetchData", e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void setUserIcon(String imgId) {
        StorageReference userRef = storageRef.child("user").child(imgId + ".jpg");
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        });
    }

}
