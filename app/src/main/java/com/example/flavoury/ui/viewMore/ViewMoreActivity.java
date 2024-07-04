package com.example.flavoury.ui.viewMore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.home.FollowingFragment;
import com.example.flavoury.ui.home.HomeFragmentAdapter;
import com.example.flavoury.ui.home.RecipeListAdapter;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewMoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView title;
    ImageView backBtn;
    String ipAddress, Uid;
    ViewMoreFragmentAdapter viewMoreFragmentAdapter;
    ViewPager2 viewPager;
    ViewMoreFragment viewMoreFragment;
    OnBackPressedCallback onBackPressedCallback;
    ArrayList<RecipeModel> recipeModels = new ArrayList<>();
    DatabaseHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        getSupportActionBar().hide();

        db = new DatabaseHelper(this);
        Uid = db.getUid();

        ipAddress = getResources().getString(R.string.ipAddress);

        title = findViewById(R.id.view_more_title);
        backBtn = findViewById(R.id.view_more_backBtn);
        viewPager = findViewById(R.id.view_more_viewpager);

        String type = getIntent().getStringExtra("ViewMore");
        switch (type){
            case "Follow":
                title.setText(type);
            case "Popular":
                title.setText(type);
                getPopularPost();
            case "Fitness":
                title.setText(type);
                getFitnessPost();
        }

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void getPopularPost(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_popular_recipe.php?RNo=50&Uid="+Uid);

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
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    recipeModels.add(recipe);
                }

                connection.disconnect();
            }catch (Exception e){
                Log.d("HomeFragmentGET", "Get Post Error" + e.toString());
            }finally {
                runOnUiThread(()->{
                    setFragment(recipeModels);
                });
            }
        }).start();
    }

    private void getFitnessPost(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_fitness_recipe.php?RNo=50&Uid="+Uid);

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
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    recipeModels.add(recipe);
                }

                connection.disconnect();
            }catch (Exception e){
                Log.d("HomeFragmentGET", "Get Post Error: " + e.toString());
            }finally {
                runOnUiThread(()->{
                    setFragment(recipeModels);
                });
            }
        }).start();
    }

    private void setFragment(ArrayList<RecipeModel> recipeModelArrayList){
        viewMoreFragmentAdapter = new ViewMoreFragmentAdapter(this.getSupportFragmentManager(), getLifecycle());
        for (int i = 0; i < recipeModelArrayList.size(); i++){
            ViewMoreFragment viewMoreFragment = new ViewMoreFragment(recipeModelArrayList.get(i));
            viewMoreFragmentAdapter.addFragment(viewMoreFragment);
        }
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(viewMoreFragmentAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPadding(40 ,10,40,100);
    }
}
