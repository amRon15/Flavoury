package com.example.flavoury.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.UserSharePref;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    ImageButton backBtn;
    ShapeableImageView userIconBtn;
    TextView editIconBtn;
    Uri userIconUri = null;
    final private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("user");
    UploadTask uploadTask;
    String ipAddress;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();

        ipAddress = getResources().getString(R.string.ipAddress);

        databaseHelper.onCreate(databaseHelper.getWritableDatabase());

        Button signBtn = findViewById(R.id.registration_signUp);

        //back to prev page callback function
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);;
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn = findViewById(R.id.registration_backBtn);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        userIconBtn = findViewById(R.id.registration_userIcon);
        editIconBtn = findViewById(R.id.registration_editIcon);
        // callback for photo picker
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
//                Log.d("PhotoPicker", "Selected uri " + uri);
                userIconBtn.setBackgroundColor(getColor(com.firebase.ui.auth.R.color.fui_transparent));
                userIconBtn.setImageURI(uri);
                userIconBtn.setElevation(100);
                userIconUri = uri;
            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });

        // click to launch photo picker
        userIconBtn.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        editIconBtn.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        signBtn.setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.registration_userName)).getText().toString();
            String email = ((EditText) findViewById(R.id.registration_userEmail)).getText().toString();
            String password = ((EditText) findViewById(R.id.registration_password)).getText().toString();
            String ccPassword = ((EditText) findViewById(R.id.registration_confirmPassword)).getText().toString();
            String iconid = UUID.randomUUID().toString();
            performSignUp(username, email, password, ccPassword, iconid);
        });
    }

    private void performSignUp(String username, String email, String password, String ccpassword, String iconid) {

        if (!password.equals(ccpassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread signUpThread = new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(ipAddress+"signup.php");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");

                connection.setDoInput(true);
                connection.setDoOutput(true);

                String signUpParams = "Username=" + URLEncoder.encode(username, "UTF-8") +
                        "&Email=" + URLEncoder.encode(email, "UTF-8") +
                        "&Password=" + URLEncoder.encode(password, "UTF-8") +
                        "&Iconid=" + URLEncoder.encode(iconid , "UTF-8");

                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(signUpParams.getBytes(StandardCharsets.UTF_8));
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

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(jsonResponseString);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if (userIconUri != null) {
                                saveUserIconToStorage(iconid);
                            }

                            if (status.equals("success")) {
                                String Uid = jsonResponse.getString("Uid");
                                saveUidToDatabase(Uid);
                                Toast.makeText(RegistrationActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Registration", e.toString());
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(RegistrationActivity.this, "HTTP Error: " + responseCode, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(RegistrationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        signUpThread.start();
    }
    private void saveUidToDatabase(String uid) {
        databaseHelper.saveUid(uid);
    }

    private void saveUserIconToStorage(String userImgId){
        StorageReference imgRef = storageRef.child(userImgId + ".jpg");
        uploadTask = imgRef.putFile(userIconUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getApplicationContext(), "Upload image successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //
}
