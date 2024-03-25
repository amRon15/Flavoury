package com.example.flavoury.ui.detail;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class DetailActivity extends AppCompatActivity {

    String[] detailArray = {"step1","step2","step3","step4","step5"};
    RecyclerView detailStepRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailStepRecyclerView = findViewById(R.id.detail_step_recyclerView);
        DetailStepAdapter detailStepAdapter = new DetailStepAdapter(detailArray);
        detailStepRecyclerView.setAdapter(detailStepAdapter);
        detailStepRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }
}