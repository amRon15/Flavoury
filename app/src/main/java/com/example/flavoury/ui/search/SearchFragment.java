package com.example.flavoury.ui.search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentSearchBinding;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private String searchType = "recipe";
    Button searchBoxBtn;
    ImageButton cancelBtn, searchBtn;
    RecyclerView historyRecyclerView, recipeRecyclerView;
    MaterialDivider recipeDiv, userDiv;
    Button recipeBtn, userBtn;
    Dialog searchDialog;
    EditText searchEditText;
    List<RecipeModel> recipes = new ArrayList<RecipeModel>();
    List<RecipeModel> searchRecipes = new ArrayList<RecipeModel>();
    ShimmerFrameLayout shimmerFrameLayout;
    SearchViewModel searchViewModel;
    boolean isRecipeDivVisible = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchBoxBtn = root.findViewById(R.id.search_search_bar);
        recipeRecyclerView = root.findViewById(R.id.search_recipeRecyclerView);

        searchView(root);
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);

        shimmerFrameLayout = root.findViewById(R.id.search_shimmer);
        shimmerFrameLayout.startShimmer();

        return root;
    }

    public void searchView(View root) {
        searchDialog = new Dialog(getContext());
        searchDialog.setContentView(R.layout.dialog_box_search);
        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialog.getWindow().setBackgroundDrawable(root.getBackground());
        searchDialog.setCancelable(true);

        cancelBtn = searchDialog.findViewById(R.id.search_dialog_cancelBtn);
        recipeDiv = searchDialog.findViewById(R.id.search_dialog_recipeDiv);
        userDiv = searchDialog.findViewById(R.id.search_dialog_userDiv);
        recipeBtn = searchDialog.findViewById(R.id.search_dialog_recipe);
        userBtn = searchDialog.findViewById(R.id.search_dialog_user);
        searchBtn = searchDialog.findViewById(R.id.search_dialog_btn);
        searchEditText = searchDialog.findViewById(R.id.search_dialog_edit);


        searchBoxBtn.setOnClickListener(v -> searchDialog.show());
        cancelBtn.setOnClickListener(v -> searchDialog.dismiss());


        recipeBtn.setOnClickListener(v -> {
            searchType = "recipe";
            isRecipeDivVisible = true;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.primary_color));
            userBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            searchEditText.setHint("Search Recipe");
        });

        userBtn.setOnClickListener(v -> {
            searchType = "user";
            isRecipeDivVisible = false;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            userBtn.setTextColor(getResources().getColor(R.color.primary_color));
            searchEditText.setHint("Search User");
        });

        //intent to SearchRecipe / User activity include editText
        searchBtn.setOnClickListener(v -> {
            if (!searchEditText.getText().toString().isEmpty()) {
                Intent intent = new Intent(getActivity(),
                        searchType.equals("recipe") ? SearchRecipeActivity.class : SearchUserActivity.class);
                intent.putExtra("searchText", String.valueOf(searchEditText.getText()));
                searchDialog.cancel();
                searchEditText.setText("");
                startActivity(intent);

            }
        });
    }

    //get all recipes return from SearchViewModel
    public List<RecipeModel> getRecipes(List<RecipeModel> recipes) {
        return this.recipes = recipes;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}