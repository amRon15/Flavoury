package com.example.flavoury.ui.search;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flavoury.R;

public class SearchUserActivity extends AppCompatActivity {
    ImageButton backBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_search_user);
        getSupportActionBar().hide();

        backBtn = findViewById(R.id.search_user_backBtn);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);

        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}
