package com.example.flavoury.ui.search;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchRecipeActivity extends AppCompatActivity {
    ImageButton backBtn;
    String searchText, Uid;
    ArrayList<RecipeModel> recipeModelArrayList;
    RecyclerView recipeRecyclerView;
    DatabaseHelper db = new DatabaseHelper(this);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        getSupportActionBar().hide();

        Uid = db.getUid();

        searchText = getIntent().getStringExtra("searchText");
        searchRecipe();

        recipeRecyclerView = findViewById(R.id.search_recipe_list);

        backBtn = findViewById(R.id.search_recipe_backBtn);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void searchRecipe() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_search_recipe.php?RName=" + searchText + "&Uid=" + Uid);
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
                Log.d("SearchUserActivityServer", jsonResponseString);

                if (!jsonResponseString.isEmpty()){
                    JSONArray jsonArray = new JSONArray(jsonResponseString);
                    recipeModelArrayList = new ArrayList<>();
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RecipeModel recipeModel = new RecipeModel();
                        recipeModel.setRecipeInList(jsonObject);
                        recipeModelArrayList.add(recipeModel);
                    }
                }

                runOnUiThread(()->{
                    SearchRecipeAdapter searchRecipeAdapter = new SearchRecipeAdapter(recipeModelArrayList);
                    recipeRecyclerView.setAdapter(searchRecipeAdapter);
                    recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                });

                connection.disconnect();
            } catch (Exception e) {
                Log.d("SearchUserActivityServer", e.toString());
            }
        }).start();
    }
}
