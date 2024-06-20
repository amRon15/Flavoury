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

import com.example.flavoury.Ingredient;
import com.example.flavoury.R;

import java.util.ArrayList;

public class FragmentIngredient extends Fragment {
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        RecyclerView ingredientRecyclerView = view.findViewById(R.id.recipe_detail_ingredients_recyclerView);
//        DetailIngredientsAdapter detailIngredientsAdapter = new DetailIngredientsAdapter();
//        ingredientRecyclerView.setAdapter(detailIngredientsAdapter);
//        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
}
