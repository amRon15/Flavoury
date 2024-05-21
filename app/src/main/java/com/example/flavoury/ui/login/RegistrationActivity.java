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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Scanner;

public class RegistrationActivity extends AppCompatActivity {
    ImageButton userIconBtn, backBtn;
    EditText userNameText;
    Uri userIcon;
    TextView userEmailText, editIconText;

    String userName,userId,userEmail,currentUserEmail;
    private EditText[] editTextFields;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();

        editTextFields = new EditText[]{
                findViewById(R.id.registration_userName),
                findViewById(R.id.registration_userEmail),
                findViewById(R.id.registration_password),
                findViewById(R.id.registration_confirmPassword)
        };
        Button signBtn = findViewById(R.id.registration_signUp);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String[] fieldValues = new String[editTextFields.length];

        for (int i = 0; i < editTextFields.length; i++) {
            fieldValues[i] = editTextFields[i].getText().toString().trim();
        }

        String username = fieldValues[0];
        String email = fieldValues[1];
        String password = fieldValues[2];
        String confirmPassword = fieldValues[3];

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            URL url = new URL("http://localhost/LogSign/signup.php"); // redir needed
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = "Username=" + username + "&Email=" + email + "&Password=" + password;

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            String serverResponse = response.toString();
            if (serverResponse.equals("Sign Up Success")) {
                Toast.makeText(this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                // Display PHP error message if available
                if (serverResponse.startsWith("Error: ")) {
                    String phpErrorMessage = serverResponse.substring("Error: ".length());
                    Toast.makeText(this, "Error: " + phpErrorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sign Up Failed. Please try again later", Toast.LENGTH_SHORT).show();
        }



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
