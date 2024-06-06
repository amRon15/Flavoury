package com.example.flavoury.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        getUid();
    }

    private void getUid() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String Uid = databaseHelper.getUid();
        Log.d("ProfileActivity", "UID: " + Uid);

        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.0.172/Flavoury/profile.php?Uid=" + Uid);

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

                runOnUiThread(() -> {
                    TextView UnameTV = findViewById(R.id.profile_userName);
                    UnameTV.setText(Username);
                });

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    TextView UnameTV = findViewById(R.id.profile_userName);
                    UnameTV.setText("Temporary username");
                });
            }
        }).start();
    }
}
