package com.example.flavoury.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.search.SearchRecipeActivity;
import com.example.flavoury.ui.search.SearchUserActivity;
import com.google.android.material.divider.MaterialDivider;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    Button searchBoxBtn;
    ImageButton addRecipeBtn, cancelSearchBtn, searchBtn;
    Dialog searchDialog;
    Button popMore, fitMore, recipeBtn, userBtn;;
    MaterialDivider recipeDiv, userDiv;
    EditText searchEditText;
    private String searchType = "recipe";
    private boolean isRecipeDivVisible = true;

    RecyclerView popRecyclerView, fitRecyclerView;
    RecipeListAdapter popularAdapter, fitnessAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchBoxBtn = root.findViewById(R.id.home_search_bar);
        addRecipeBtn = root.findViewById(R.id.homeAddBtn);

        //search dialog pop up
        searchView(root);

        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            startActivity(intent);
        });

        return root;
    }

    public void searchView(View root){
        searchDialog = new Dialog(getContext());
        searchDialog.setContentView(R.layout.dialog_box_search);
        searchDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchDialog.getWindow().setBackgroundDrawable(root.getBackground());
        searchDialog.setCancelable(true);
        searchDialog.setCanceledOnTouchOutside(true);

        cancelSearchBtn = searchDialog.findViewById(R.id.search_dialog_cancelBtn);
        recipeDiv = searchDialog.findViewById(R.id.search_dialog_recipeDiv);
        userDiv = searchDialog.findViewById(R.id.search_dialog_userDiv);
        recipeBtn = searchDialog.findViewById(R.id.search_dialog_recipe);
        userBtn = searchDialog.findViewById(R.id.search_dialog_user);
        searchEditText = searchDialog.findViewById(R.id.search_dialog_edit);
        searchBtn = searchDialog.findViewById(R.id.search_dialog_btn);

        //both change search type
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

        searchBoxBtn.setOnClickListener(v -> searchDialog.show());
        cancelSearchBtn.setOnClickListener(v -> searchDialog.dismiss());

        //intent to SearchRecipe / User activity include editText
        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),
                    searchType == "recipe" ? SearchRecipeActivity.class : SearchUserActivity.class);
            intent.putExtra("searchText",searchEditText.getText());
//            startActivity(intent);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}