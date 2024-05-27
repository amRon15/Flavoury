package com.example.flavoury.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Scanner;

public class RegistrationActivity extends AppCompatActivity {

    ImageButton userIconBtn, backBtn;
    Uri userIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();

        Button signBtn = findViewById(R.id.registration_signUp);

        signBtn.setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.registration_userName)).getText().toString();
            String email = ((EditText) findViewById(R.id.registration_userEmail)).getText().toString();
            String password = ((EditText) findViewById(R.id.registration_password)).getText().toString();
            String ccpassword = ((EditText) findViewById(R.id.registration_confirmPassword)).getText().toString();
            performsignUp(username, email, password, ccpassword);
        });
    }

    private void performsignUp(String username, String email, String password, String ccpassword) {

        if (!password.equals(ccpassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread signupThread = new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://192.168.0.172/Flavoury/start.php");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");

                connection.setDoInput(true);
                connection.setDoOutput(true);

                String SignupParams = "Username=" + URLEncoder.encode(username, "UTF-8") +
                        "&Email=" + URLEncoder.encode(email, "UTF-8") +
                        "&Password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(SignupParams.getBytes(StandardCharsets.UTF_8));
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
                                Toast.makeText(RegistrationActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistrationActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "HTTP Error: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });


        //back to prev page callback function
//        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                finish();
//            }
//        };
//        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
//
//        backBtn = findViewById(R.id.registration_backBtn);
//        userIconBtn = findViewById(R.id.registration_userIcon);
//        editIconText = findViewById(R.id.registration_editIcon);
//
//        backBtn.setOnClickListener(view -> onBackPressedCallback.handleOnBackPressed());
//
//

//        // callback for photo picker
//        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
//            if(uri != null){
//                Log.d("PhotoPicker", "Selected uri " + uri);
//                userIconBtn.setImageURI(uri);
//                userIcon = uri;
//            }else {
//                Log.d("PhotoPicker","no media selected");
//            }
//        });
//
//        // click to launch photo picker
//        userIconBtn.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
//                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
//                .build()));
//
//        editIconText.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
//                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
//                .build()));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
    }
}
