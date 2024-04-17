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

import java.sql.Timestamp;

public class RegistrationActivity extends AppCompatActivity {
    Button signUpBtn;
    ImageButton userIconbtn;
    EditText userNameText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Uri userIcon;
    FirebaseUser user;
    TextView userEmailText;

    String userName,userId,userEmail,currentUserEmail;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().hide();


        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = user.getEmail();

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        // get intent data from LoginActivity intent put extra
        Intent fromLoginActivity = getIntent();

//        userId = fromLoginActivity.getStringExtra("userId");
        userId = user.getUid();
        if(currentUserEmail.isEmpty()) {
            userEmail = fromLoginActivity.getStringExtra("userEmail");
        }else{
          userEmail = currentUserEmail;
        }

        signUpBtn = findViewById(R.id.registration_signUp);
        userIconbtn = findViewById(R.id.registration_userIcon);
        userNameText = findViewById(R.id.registration_userName);
        userEmailText = findViewById(R.id.registration_userEmail);
        userEmailText.setText(userEmail);

        signUpBtn.setEnabled(false);

        // callback for photo picker
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
                Log.d("PhotoPicker", "Selected uri " + uri);
                userIconbtn.setImageURI(uri);
                userIcon = uri;
            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });


        // click to launch photo picker
        userIconbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });
        userNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                signUpBtn.setEnabled(charSequence.length()!=0);
                userName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        userNameText.setImeActionLabel("setUserName", KeyEvent.KEYCODE_ENTER);

        //add new user to fireStore
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newUser = new UserModel(userName,userEmail,currentTime,userIcon );
                Log.d("RegistrationActivity", "User " + newUser);

                    db.collection("User").document(userId).set(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()){
                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            })
            .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("RegistrationActivity", "Failed to added new user");
                        }
                    });
            }
        });


    }


}
