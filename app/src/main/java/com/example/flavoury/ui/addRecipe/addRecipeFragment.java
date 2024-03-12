package com.example.flavoury.ui.addRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flavoury.databinding.FragmentAddRecipeBinding;
import com.example.flavoury.databinding.FragmentHomeBinding;

public class addRecipeFragment extends Fragment {

    private FragmentAddRecipeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addRecipeViewModel addRecipeViewModel =
                new ViewModelProvider(this).get(addRecipeViewModel.class);

        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCreateRecipe;
        addRecipeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}