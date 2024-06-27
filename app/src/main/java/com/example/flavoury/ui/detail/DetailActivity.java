package com.example.flavoury.ui.detail;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.myProfile.MyProfileFragment;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DetailActivity extends AppCompatActivity {
    //    RecipeModel detailRecipe;
    RecyclerView detailStepRecyclerView, detail_ingredients_recyclerview;
    DetailStepAdapter detailStepAdapter;
    DetailIngredientsAdapter detailIngredientsAdapter;
    ImageButton backBtn, moreBtn;
    ToggleButton like_toggle, bookmarkBtn;
    ShapeableImageView userIcon;
    ImageView recipeImg;
    Button deleteConfirm, deleteCancel;
    DatabaseHelper db = new DatabaseHelper(this);
    TextView userName;
    String myUserId;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    RecipeModel recipe;
    Dialog deleteDialog;
    final String[] tab_title = {"Description", "Ingredient", "Step"};
    String ipAddress;
    boolean isUserBookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        db.onCreate(db.getWritableDatabase());
        myUserId = db.getUid();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        Intent recipeIntent = getIntent();
        recipe = (RecipeModel) recipeIntent.getSerializableExtra("Recipe");

        backBtn = findViewById(R.id.recipe_detail_backBtn);
        bookmarkBtn = findViewById(R.id.recipe_detail_bookmarkBtn);
        userIcon = findViewById(R.id.recipe_detail_userIcon);
        recipeImg = findViewById(R.id.recipe_detail_recipeImg);
        tabLayout = findViewById(R.id.recipe_detail_tablayout);
        viewPager = findViewById(R.id.recipe_detail_viewPager);
        userName = findViewById(R.id.recipe_detail_userName);
        moreBtn = findViewById(R.id.recipe_detail_moreBtn);

        deleteDialog = new Dialog(this);
        deleteDialog.setContentView(R.layout.dialog_box_delete_recipe);
        deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background_inset));
        deleteDialog.setCancelable(true);
        deleteCancel = deleteDialog.findViewById(R.id.delete_dialog_cancel);
        deleteConfirm = deleteDialog.findViewById(R.id.delete_dialog_confirm);

        isBookmark();

        deleteConfirm.setOnClickListener(v->{
            deleteRecipe();
        });

        deleteCancel.setOnClickListener(v->{
            deleteDialog.dismiss();
        });

        bookmarkBtn.setOnClickListener(view -> {
            if (bookmarkBtn.isChecked()){
                cancelBookmark();
            } else {
                bookmarkRecipe();
            }
        });


        moreBtn.setOnClickListener(v->{
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View bottomSheetLayout = LayoutInflater.from(this).inflate(R.layout.fragment_recipe_detail_bottomsheet, null);
            bottomSheetDialog.setContentView(bottomSheetLayout);

            ViewGroup parent = (ViewGroup) bottomSheetLayout.getParent();
            parent.setBackgroundResource(android.R.color.transparent);
            bottomSheetDialog.show();

            Button editBtn = bottomSheetLayout.findViewById(R.id.recipe_detail_editBtn);
            Button deleteBtn = bottomSheetLayout.findViewById(R.id.recipe_detail_deleteBtn);

            editBtn.setOnClickListener(view -> {
                Intent intent = new Intent(this, AddRecipeActivity.class);
                intent.putExtra("Recipe", (Serializable) recipe);
                bottomSheetDialog.dismiss();
            });

            deleteBtn.setOnClickListener(view -> {
//                deleteRecipe();
                bottomSheetDialog.dismiss();

            });
        });

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        getStepAndIngredient();
        getUser(recipe.getUid());
        setView(recipe);

        viewPagerAdapter = new ViewPagerAdapter(this, recipe);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                viewPager.setCurrentItem(position);
                tab.setText(tab_title[position]);

            }
        }).attach();
        viewPager.setCurrentItem(0);
    }

    private void setView(RecipeModel recipe) {
        TextView recipeName = findViewById(R.id.recipe_detail_recipeName);
        TextView cookingTime = findViewById(R.id.recipe_detail_cookingMins);
        TextView likeNum = findViewById(R.id.recipe_detail_likeNum);
        TextView recipeCals = findViewById(R.id.recipe_detail_calsNum);
        TextView category = findViewById(R.id.recipe_detail_category);
        ToggleButton recipeLikeToggle = findViewById(R.id.recipe_detail_likeToggle);

        recipeName.setText(recipe.getRName());
        cookingTime.setText(recipe.getCookTime());
        likeNum.setText(String.valueOf(recipe.getLikes()));
        category.setText(recipe.getCategory());

        if (recipe.getUid().equals(myUserId)) {
            registerForContextMenu(moreBtn);
            moreBtn.setVisibility(View.VISIBLE);
            bookmarkBtn.setVisibility(View.GONE);
        }

        setRecipeImg(recipe.getImgid());

    }

    private void deleteRecipe(){
        new Thread(()->{
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress+"app_delete_recipe.php");

                String recipeParam = "Uid=" + URLEncoder.encode(recipe.getUid(), "UTF-8") +
                        "Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(recipeParam.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine()) != null){
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    JSONObject jsonObject = new JSONObject(jsonResponseString);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    runOnUiThread(()->{
                        if (status == "success"){
                            Intent intent = new Intent(this, MyProfileFragment.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            } catch (Exception e){
                Log.d("DetailActivityFetch", "Error: " + e.toString());
            } finally {
                if (connection != null){
                    connection.disconnect();
                    deleteDialog.dismiss();
                }
            }

        }).start();
    }

    private void isBookmark(){
        try {
            URL url = new URL(ipAddress+"app_is_user_bookmark.php?Uid="+myUserId+"&Rid="+recipe.getRid());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();
            String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
            isUserBookmark = jsonResponseString.equals("null");
        } catch (Exception e){
            Log.d("DetailActivityFetch", e.toString());
        } finally {
            runOnUiThread(()->{
                bookmarkBtn.setChecked(isUserBookmark);
            });
        }
    }

    private void bookmarkRecipe(){
        new Thread(()->{
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress+"app_bookmark_recipe.php");

                String recipeParam = "Uid=" + URLEncoder.encode(recipe.getUid(), "UTF-8") +
                        "Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(recipeParam.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine()) != null){
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");

                    JSONObject jsonObject = new JSONObject(jsonResponseString);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    Log.d("DetailActivityFetch", status + ", " + message);

                }

            } catch (Exception e){
                Log.d("DetailActivityFetch", "Error: " + e.toString());
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void cancelBookmark(){
        new Thread(()->{
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress+"app_delete_bookmark.php");

                String recipeParam = "Uid=" + URLEncoder.encode(recipe.getUid(), "UTF-8") +
                        "Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(recipeParam.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine()) != null){
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");

                    JSONObject jsonObject = new JSONObject(jsonResponseString);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    Log.d("DetailActivityFetch", status + ", " + message);

                }

            } catch (Exception e){
                Log.d("DetailActivityFetch", "Error: " + e.toString());
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void getUser(String Uid) {
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress+"profile.php?Uid=" + Uid);
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
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                String username = jsonObject.getString("Username");
                String uId = jsonObject.getString("Iconid");

                runOnUiThread(() -> {
                    userName.setText(username);
                    setUserIcon(uId);
                });

                connection.disconnect();
            } catch (Exception e) {
                Log.d("DetailActivityFetch", e.toString());
            }
        }).start();
    }

    private void setRecipeImg(String rId) {
        StorageReference recipeRef = storageRef.child("recipe").child(rId + ".jpg");
        recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
            }
        });
    }

    private void setUserIcon(String uId) {
        StorageReference userRef = storageRef.child("user").child(uId + ".jpg");
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        });
    }

    private void getStepAndIngredient() {
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress+"app_ingredient_step.php?Rid=" + recipe.getRid());

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
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                JSONArray stepJson = jsonObject.getJSONArray("step");
                JSONArray ingredientJson = jsonObject.getJSONArray("ingredient");

                recipe.StepAndIngredient(stepJson, ingredientJson);

                runOnUiThread(() -> {

                    viewPagerAdapter = new ViewPagerAdapter(this, recipe);
                    viewPager.setAdapter(viewPagerAdapter);

                    viewPager.setCurrentItem(0);
                });

                connection.disconnect();

            } catch (Exception e) {
                Log.d("DetailActivityFetch", "Error: " + e.toString());
            }
        }).start();
    }

    private  void setLike_toggle(){
        final DatabaseHelper db = new DatabaseHelper(this);
        final String recipeID = db.getRid();
        Log.d("Like", recipeID);

        like_toggle.setOnClickListener(v -> {
            likeRecipe(myUserId, recipeID);
        });
    }

    private void likeRecipe(String userID, String recipeID) {
        Thread addRecipeThread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress+"app_like_reicpe.php");

                String recipeParam = "Uid=" + URLEncoder.encode(userID, "UTF-8") +
                        "&Rid=" + URLEncoder.encode(recipeID, "UTF-8");

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
                    Log.d("Like", "HTTP OK");
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("Like", jsonResponseString);
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Log.d("Like", jsonObject.toString());
                            if (status.equals("success")) {
                                Toast.makeText(this, "Like", Toast.LENGTH_LONG).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Like", "JSON Error: " + e.getMessage());
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Like", "Exception: " + e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        addRecipeThread.start();
    }


}