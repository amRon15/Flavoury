package com.example.flavoury.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

import com.example.flavoury.R;
import com.example.flavoury.ui.login.LoginActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    ShapeableImageView userIcon;
    TextView editIcon, emailText, usernameText;
    ImageButton backBtn;
    Button saveBtn;
    Uri userIconUri;
    String uId, ipAddress, iconId, oldIconId;
    boolean containIcon;
    DatabaseHelper db;
    StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        db = new DatabaseHelper(this);
        db.onCreate(db.getReadableDatabase());
        uId = db.getUid();

        ipAddress = getResources().getString(R.string.ipAddress);

        userIcon = findViewById(R.id.edit_profile_userIcon);
        editIcon = findViewById(R.id.edit_profile_editIcon);
        emailText = findViewById(R.id.edit_profile_userEmail);
        usernameText = findViewById(R.id.edit_profile_userName);
        backBtn = findViewById(R.id.edit_profile_backBtn);
        saveBtn = findViewById(R.id.edit_profile_saveBtn);

        getUserInfo();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
                userIcon.setImageURI(uri);
                userIconUri = uri;
                iconId = UUID.randomUUID().toString();
            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });

        // click to launch photo picker
        userIcon.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        editIcon.setOnClickListener(view -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        saveBtn.setOnClickListener(v->{
            saveIcon();
        });
    }

    private void getUserInfo(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"profile.php?Uid="+uId);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine())!=null){
                    response.append(line);
                }
                reader.close();
                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                oldIconId = jsonObject.getString("Iconid");
                String email = jsonObject.getString("Email");
                String username = jsonObject.getString("Username");

                runOnUiThread(()->{
                    emailText.setText(email);
                    usernameText.setText(username);
                    setIcon();
                });

            } catch (Exception e){

            }
        }).start();
    }

    private void setIcon(){
        userRef.child(oldIconId+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                containIcon = false;
            }
        });
    }

    private void saveIcon(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_update_profile.php");

                String param = "Uid=" + URLEncoder.encode(uId, "UTF-8") +
                        "&Iconid=" + URLEncoder.encode(iconId, "UTF-8");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(param);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("EditProfileActivity", jsonResponseString);
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")) {
                                sendImgToStorage();
                                Toast.makeText(this, "Update Icon successful", Toast.LENGTH_LONG).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("EditProfileActivity", "JSON Error: " + e.getMessage());
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }

            } catch (Exception e){
                Log.d("EditProfileActivity", e.toString());
            }
        }).start();
    }

    private void sendImgToStorage(){
        StorageReference oldIcon = userRef.child(oldIconId+".jpg");

        if (containIcon) {
            oldIcon.delete();
        };
        userRef.child(iconId + ".jpg").putFile(userIconUri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
