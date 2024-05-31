package com.example.flavoury.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentSearchBinding;
import com.example.flavoury.ui.detail.DetailActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    LinearLayout searchBox;
    RecyclerView historyRecyclerView, recipeRecyclerView;
    SearchView searchView;
    List<RecipeModel> recipes = new ArrayList<RecipeModel>();
    List<RecipeModel> searchRecipes = new ArrayList<RecipeModel>();

    SearchRecipeAdapter searchRecipeAdapter;
    SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchBox = root.findViewById(R.id.search_search_box);
        recipeRecyclerView = root.findViewById(R.id.search_recipeRecyclerView);

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        searchRecipeAdapter = new SearchRecipeAdapter();
//        recipeRecyclerView.setAdapter(searchRecipeAdapter);
//        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //dynamically show the recipes according to the text in searchView

//        historyRecyclerView = root.findViewById(R.id.search_historyList);
//        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(myArray);
//        historyRecyclerView.setAdapter(searchHistoryAdapter);
//        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//        historyRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        return root;
    }

    //get all recipes return from SearchViewModel
    public List<RecipeModel> getRecipes(List<RecipeModel> recipes) {
        return this.recipes = recipes;
    }

    //get the recipes that the name contains searchView's text & send it to handleRecyclerView()
    public void onSearchRecipe(String text, List<RecipeModel> recipes) {
        searchRecipes.clear();
        for (RecipeModel recipe : recipes) {
            if (recipe.getRecipeName().toLowerCase().contains(text.toLowerCase())) {
                searchRecipes.add(recipe);
            }
        }
        handleRecyclerView(searchRecipes);
    }

    //send the list to adapter & show the recipes in recyclerView
    public void handleRecyclerView(List<RecipeModel> recipes) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}