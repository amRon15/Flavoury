package com.example.flavoury.ui.profile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView profile_myRecyclerView;

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