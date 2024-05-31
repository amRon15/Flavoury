package com.example.flavoury.ui.setting;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.flavoury.R;
import com.example.flavoury.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SettingActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button logoutBtn,logoutCancel,logoutConfirm;
    Dialog logoutDialog;
    FirebaseAuth auth;
    Intent logoutIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);

        logoutBtn = findViewById(R.id.setting_logoutBtn);
        backBtn = findViewById(R.id.setting_backBtn);

        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.dialog_box_logout);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.logout_dialog_box));
        logoutDialog.setCancelable(true);
        logoutCancel = logoutDialog.findViewById(R.id.logout_dialog_cancel);
        logoutConfirm = logoutDialog.findViewById(R.id.logout_dialog_confirm);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.show();
            }
        });

        logoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });

        logoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(logoutIntent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }
}