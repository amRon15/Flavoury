package com.example.flavoury.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.flavoury.R;
import com.example.flavoury.UserModel;
import com.example.flavoury.UserSharePref;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.search.SearchHistoryAdapter;
import com.example.flavoury.ui.search.SearchRecipeActivity;
import com.example.flavoury.ui.search.SearchUserActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.divider.MaterialDivider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    Button searchBoxBtn;
    ImageButton addRecipeBtn, cancelSearchBtn, searchBtn, searchBarBtn;
    Dialog searchDialog;
    Button popMore, fitMore, recipeBtn, userBtn;
    MaterialDivider recipeDiv, userDiv;
    EditText searchEditText;
    Set<String> recipeHistorySet, userHistorySet;
    ArrayList<String> recipeHistoryList, userHistoryList;
    ShimmerFrameLayout popListShimmer, fitListShimmer, followPost;

    private UserSharePref userSharePref;
    private SharedPreferences sharePref;
    private String searchType = "recipe";
    private boolean isRecipeDivVisible = true;

    RecyclerView popRecyclerView, fitRecyclerView, historyRecyclerView;
    RecipeListAdapter popularAdapter, fitnessAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        sharePref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userSharePref = new UserSharePref(sharePref);

        searchBoxBtn = root.findViewById(R.id.home_search_bar);
        searchBarBtn = root.findViewById(R.id.home_searchBtn);
        addRecipeBtn = root.findViewById(R.id.homeAddBtn);

        popListShimmer = root.findViewById(R.id.home_pop_list_shimmer);
        popListShimmer.startShimmer();

        fitListShimmer = root.findViewById(R.id.home_explore_list_shimmer);
        fitListShimmer.startShimmer();

        followPost = root.findViewById(R.id.home_shimmer_follow_post);
        followPost.startShimmer();



        //search dialog pop up
        searchView(root);

        //get history from share preference
//        getAllHistory();

        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            startActivity(intent);
        });
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);

        return root;
    }

    public void searchView(View root) {
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
        historyRecyclerView = searchDialog.findViewById(R.id.search_recipeRecyclerView);

        //both change search type
        recipeBtn.setOnClickListener(v -> {
            searchType = "recipe";
            isRecipeDivVisible = true;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.primary_color));
            userBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            searchEditText.setHint("Search Recipe");

            //history recyclerView
//            historyRecyclerView(recipeHistoryList);
        });

        userBtn.setOnClickListener(v -> {
            searchType = "user";
            isRecipeDivVisible = false;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            userBtn.setTextColor(getResources().getColor(R.color.primary_color));
            searchEditText.setHint("Search User");//history recyclerView

//            historyRecyclerView(userHistoryList);
        });

        searchBoxBtn.setOnClickListener(v -> searchDialog.show());
        cancelSearchBtn.setOnClickListener(v -> searchDialog.dismiss());

        //intent to SearchRecipe / User activity include editText
        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),
                    searchType.equals("recipe") ? SearchRecipeActivity.class : SearchUserActivity.class);
            intent.putExtra("searchText", String.valueOf(searchEditText.getText()));
            searchDialog.cancel();

            //save history when press search btn

            startActivity(intent);
        });
    }



    private void historyRecyclerView(ArrayList<String> arrayList) {
        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(arrayList);
        historyRecyclerView.setAdapter(searchHistoryAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void getAllHistory() {
        recipeHistoryList = (ArrayList<String>) userSharePref.getRecipeHistory();
        userHistoryList = (ArrayList<String>) userSharePref.getUserHistory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}