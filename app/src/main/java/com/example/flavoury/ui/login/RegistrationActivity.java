package com.example.flavoury.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;

import java.sql.Timestamp;

public class RegistrationActivity extends AppCompatActivity {
    Button signUpBtn;
    EditText userName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Intent fromLoginActivity = getIntent();

        signUpBtn = findViewById(R.id.registration_signUp);
        if(userName.getText().toString()==null){
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.secondaryText_color));
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        new user info
//        RegistrationUserModel newUser = new RegistrationUserModel("",userName.getText().toString(),fromLoginActivity.getStringExtra("userEmail"),currentTime,"" );

    }


}
