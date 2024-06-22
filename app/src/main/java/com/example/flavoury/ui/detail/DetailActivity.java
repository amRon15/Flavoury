package com.example.flavoury.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
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

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.login.LoginActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
//    RecipeModel detailRecipe;
    RecyclerView detailStepRecyclerView, detail_ingredients_recyclerview;
    DetailStepAdapter detailStepAdapter;
    DetailIngredientsAdapter detailIngredientsAdapter;
    ImageButton recipe_detail_backBtn, bookmarkBtn, editBtn;
    ToggleButton like_toggle;
    ShapeableImageView userIcon;
    ImageView recipeImg;
    DatabaseHelper db = new DatabaseHelper(this);
    String myUserId;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    final String[] tab_title = {"Description", "Ingredient", "Step"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().hide();

        db.onCreate(db.getWritableDatabase());
        myUserId = db.getUid();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        Intent recipeIntent = getIntent();
        RecipeModel recipe = (RecipeModel) recipeIntent.getSerializableExtra("Recipe");

        recipe_detail_backBtn = findViewById(R.id.recipe_detail_backBtn);
        editBtn = findViewById(R.id.recipe_detail_editBtn);
        bookmarkBtn = findViewById(R.id.recipe_detail_bookmarkBtn);
        userIcon = findViewById(R.id.recipe_detail_userIcon);
        recipeImg = findViewById(R.id.recipe_detail_recipeImg);
        tabLayout = findViewById(R.id.recipe_detail_tablayout);
        viewPager = findViewById(R.id.recipe_detail_viewPager);

        recipe_detail_backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

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

        if (recipe.getUid().equals(myUserId)){
            editBtn.setVisibility(View.VISIBLE);
        }

        setRecipeImg(recipe.getImgid());

    }
//
//    private void handleRecyclerView(RecipeModel recipe) {
//        detailIngredientsAdapter.setDetailIngredientsAdapter(recipe.getIngredients(), recipe.getIngredients().size());
//        detailStepAdapter.setDetailStepAdapter(recipe.getSteps(), recipe.getSteps().size());
//
//        detailIngredientsAdapter.notifyDataSetChanged();
//        detailStepAdapter.notifyDataSetChanged();
//    }

    private void getUser(String Uid){
        new Thread(()->{
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/profile.php?Uid=" + Uid);
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
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                String username = jsonObject.getString("Username");
                String uId = jsonObject.getString("Iconid");

                TextView userName = findViewById(R.id.recipe_detail_userName);
                userName.setText(username);
                setUserIcon(uId);


            }catch (Exception e){
                Log.d("DetailActivityFetch", e.toString());
            }
        }).start();
    }

    private void setRecipeImg(String rId){
        StorageReference recipeRef = storageRef.child("recipe").child(rId + ".jpg");
        recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
            }
        });
    }

    private void setUserIcon(String uId){
        StorageReference userRef = storageRef.child("user").child(uId+".jpg");
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        });
    }

    private  void setLike_toggle(){
                final DatabaseHelper db = new DatabaseHelper(this);
                final String userID = db.getUid();
                final String recipeID = db.getRid();
                Log.d("Like", userID);
                Log.d("Like", recipeID);

                like_toggle.setOnClickListener(v -> {
//                    likeRecipe(userID, recipeID);
                });
            }



}