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

public class LoginActivity extends AppCompatActivity {
    int data;
    TextView signUpBtn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
            performLogin(username, password);
        });
    }

    private void performLogin(String username, String password) {
        Login login = new Login(new Login.LoginCallback() {
            @Override
            public void onLoginResult(String result) {
                if (result.equals("Login Success")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.execute(username, password);
    }
}




