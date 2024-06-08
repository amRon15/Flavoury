package com.example.flavoury;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.firebase.ui.auth.data.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flavoury.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    FloatingActionButton scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hide Action Bar
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        //nav bar scan btn
        scanBtn = findViewById(R.id.navigation_scanBtn);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_likes, R.id.navigation_myProfile)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getUid();
    }

    private void getUid() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String Uid = databaseHelper.getUid();
        Log.v("ProfileActivity", "UID: " + Uid);

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

                JSONObject jsonObject = new JSONObject(response.toString());
                final String Username = jsonObject.getString("Username");

                if (Username.isEmpty()){
                    Log.d("cantfind", Username);
                }

                runOnUiThread(() -> {
//                    TextView UnameTV = findViewById(R.id.profile_userName);
//                    UnameTV.setText(Username);
                });


                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
//                    TextView UnameTV = findViewById(R.id.profile_userName);
//                    UnameTV.setText("Temporary username");
                });
            }
        }).start();
    }

}