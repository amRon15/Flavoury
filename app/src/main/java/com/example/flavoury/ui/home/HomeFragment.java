package com.example.flavoury.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    Button searchButton;
    ImageButton addRecipeBtn;
    Dialog searchDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchButton = root.findViewById(R.id.home_search_bar);
        addRecipeBtn = root.findViewById(R.id.homeAddBtn);


        searchDialog = new Dialog(getContext());
        searchDialog.setContentView(R.layout.dialog_box_search);
        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialog.getWindow().setBackgroundDrawable(root.getBackground());
        searchDialog.setCancelable(true);

        searchButton.setOnClickListener(v -> searchDialog.show());

        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddRecipeActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}