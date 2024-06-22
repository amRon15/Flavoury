package com.example.flavoury.ui.setting;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.UserSharePref;
import com.example.flavoury.ui.login.LoginActivity;
import com.example.flavoury.ui.myProfile.MyProfileFragment;
import com.example.flavoury.ui.sqlite.DatabaseHelper;


public class SettingActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button logoutBtn,logoutCancel,logoutConfirm;
    Dialog logoutDialog;
    final private DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        databaseHelper.onCreate(databaseHelper.getWritableDatabase());

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);


        logoutBtn = findViewById(R.id.setting_logoutBtn);
        backBtn = findViewById(R.id.setting_backBtn);

        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.dialog_box_logout);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background_inset));
        logoutDialog.setCancelable(true);
        logoutCancel = logoutDialog.findViewById(R.id.logout_dialog_cancel);
        logoutConfirm = logoutDialog.findViewById(R.id.logout_dialog_confirm);

        logoutBtn.setOnClickListener(view -> logoutDialog.show());

        logoutCancel.setOnClickListener(view -> logoutDialog.dismiss());

        logoutConfirm.setOnClickListener(view -> {
            databaseHelper.deleteUid();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

    }
}