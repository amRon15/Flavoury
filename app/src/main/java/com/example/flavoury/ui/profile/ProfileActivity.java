package com.example.flavoury.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView profile_myRecyclerView;
    boolean isFollowed = false;
    Button follow_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        profile_myRecyclerView = findViewById(R.id.profile_myRecyclerView);
        ProfileAdapter adapter = new ProfileAdapter(generateProfileitems());
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(ProfileActivity.this, 2, GridLayoutManager.VERTICAL, false);
        profile_myRecyclerView.setLayoutManager(layoutManager);
        profile_myRecyclerView.setAdapter(adapter);

        follow_button = findViewById(R.id.profile_followBtn);
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowed) {
                    // 如果已經關注，則取消關注
                    isFollowed = false;
                    follow_button.setBackgroundResource(R.drawable.profile_follow_btn);
                    follow_button.setText("Follow");
                } else {
                    // 如果未關注，則執行關注操作
                    isFollowed = true;
                    follow_button.setBackgroundResource(R.drawable.profile_follow_btn);
                    follow_button.setText("Followed");
                }
            }
        });
    }

    private List<Profileitems> generateProfileitems() {
        List<Profileitems> profileitemlists = new ArrayList<>();
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food","food • >60mins"));

        return profileitemlists;
    }
}