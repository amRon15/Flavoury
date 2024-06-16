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

import com.example.flavoury.R;
import com.example.flavoury.UserSharePref;
import com.example.flavoury.ui.login.LoginActivity;


public class SettingActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button logoutBtn,logoutCancel,logoutConfirm;
    Dialog logoutDialog;
    private UserSharePref userSharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        final SharedPreferences shareRef = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userSharePref = new UserSharePref(shareRef);
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
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.logout_dialog_box));
        logoutDialog.setCancelable(true);
        logoutCancel = logoutDialog.findViewById(R.id.logout_dialog_cancel);
        logoutConfirm = logoutDialog.findViewById(R.id.logout_dialog_confirm);

        logoutBtn.setOnClickListener(view -> logoutDialog.show());

        logoutCancel.setOnClickListener(view -> logoutDialog.dismiss());

        logoutConfirm.setOnClickListener(view -> {
            userSharePref.setLoginStatus(false);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

    }
}