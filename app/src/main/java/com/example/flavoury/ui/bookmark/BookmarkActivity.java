package com.example.flavoury.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.search.ExploreRecipeAdapter;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    ImageButton backBtn;
    OnBackPressedCallback onBackPressedCallback;
    BookmarkAdapter bookmarkAdapter;
    RecyclerView bookmarkRecyclerView;
    String ipAddress, Uid;
    ArrayList<RecipeModel> recipeModelArrayList;
    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        db = new DatabaseHelper(this);
        Uid = db.getUid();

        bookmarkRecyclerView = findViewById(R.id.bookmarks_list);

        getBookmark();
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn = findViewById(R.id.bookmark_backBtn);
        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void getBookmark() {
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress + "app_get_bookmark_recipe.php?Uid="+Uid);
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
                recipeModelArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    recipeModelArrayList.add(recipe);
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.d("BookmarkActivityFetch", e.toString());
            } finally {
                runOnUiThread(() -> {
                    bookmarkAdapter = new BookmarkAdapter(recipeModelArrayList);
                    bookmarkRecyclerView.setAdapter(bookmarkAdapter);
                    bookmarkRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                });
            }
        }).start();
    }
}
