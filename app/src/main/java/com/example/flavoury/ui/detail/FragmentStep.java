package com.example.flavoury.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class FragmentStep extends Fragment {
    ArrayList<String> stepArrayList = new ArrayList<>();

    public FragmentStep(ArrayList<String> stepArrayList){
        this.stepArrayList = stepArrayList;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView stepRecyclerView = view.findViewById(R.id.recipe_detail_step_recyclerView);
        DetailStepAdapter detailStepAdapter = new DetailStepAdapter(stepArrayList);
        stepRecyclerView.setAdapter(detailStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
}
