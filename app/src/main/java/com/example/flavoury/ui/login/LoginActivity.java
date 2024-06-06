
package com.example.flavoury.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView signUpBtn;
    private String Uid;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();

        signUpBtn = findViewById(R.id.login_signUpBtn);
        Button logBtn = findViewById(R.id.login_loginBtn);

        signUpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        logBtn.setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.login_username)).getText().toString();
            String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
            performlogIn(username, password);
        });

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.onCreate(databaseHelper.getWritableDatabase());
    }

    private void performlogIn(String username, String password) {
        Thread loginThread = new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://192.168.0.172/Flavoury/login.php");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");

                connection.setDoInput(true);
                connection.setDoOutput(true);

                String loginParams = "Username=" + URLEncoder.encode(username, "UTF-8") +
                        "&Password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(loginParams.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equals("success")) {
                                Uid = jsonResponse.getString("Uid");
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                saveUidToDatabase(Uid);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("Uid", Uid);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "HTTP Error: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        loginThread.start();
    }

    private void saveUidToDatabase(String uid) {
        databaseHelper.saveUid(uid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();

    }
}