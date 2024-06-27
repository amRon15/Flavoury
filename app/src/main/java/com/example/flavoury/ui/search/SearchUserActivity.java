package com.example.flavoury.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.UserModel;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {
    ImageButton backBtn;
    ArrayList<UserModel> userModelArrayList = new ArrayList<>();
    RecyclerView userRecyclerView;
    SearchUserAdapter searchUserAdapter;
    TextView searchResult;
    String searchText, Uid;
    DatabaseHelper db = new DatabaseHelper(this);
    String ipAddress;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        Uid = db.getUid();

        searchText = getIntent().getStringExtra("searchText");
        searchUser();

        backBtn = findViewById(R.id.search_user_backBtn);
        userRecyclerView = findViewById(R.id.search_user_list);
        searchResult = findViewById(R.id.search_user_result);

        searchResult.setText("Search: " + searchText);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());



    }

    private void searchUser() {
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress+"app_search_user.php?UName=" + searchText + "&Uid=" + Uid);
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
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        UserModel userModel = new UserModel(jsonObject);
                        userModelArrayList.add(userModel);
                    }
                }

                runOnUiThread(()->{
                    searchUserAdapter = new SearchUserAdapter(userModelArrayList, Uid);
                    userRecyclerView.setAdapter(searchUserAdapter);
                    userRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                });
                connection.disconnect();
            } catch (Exception e) {
                Log.d("SearchUserActivityServer", e.toString());
            }
        }).start();
    }
}
