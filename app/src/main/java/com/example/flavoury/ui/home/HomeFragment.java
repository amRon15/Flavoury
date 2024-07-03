package com.example.flavoury.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;


import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.FireBaseToDB;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.search.SearchHistoryAdapter;
import com.example.flavoury.ui.search.SearchRecipeActivity;
import com.example.flavoury.ui.search.SearchUserActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.example.flavoury.ui.viewMore.ViewMoreActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.divider.MaterialDivider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    ImageButton addRecipeBtn, cancelSearchBtn, searchBtn;
    Dialog searchDialog;
    Button followMore, popMore, fitMore, recipeBtn, userBtn, searchBar;
    MaterialDivider recipeDiv, userDiv;
    EditText searchEditText;
    TextView clearSearchBtn, nullHistory;
    ArrayList<String> recipeHistoryList, userHistoryList;
    ShimmerFrameLayout popListShimmer, fitListShimmer, followPost;
    ViewPager2 followViewPager;
    RecyclerView popRecyclerView, fitRecyclerView, historyRecyclerView;
    SearchHistoryAdapter historyAdapter;
    RecipeListAdapter popularAdapter, fitnessAdapter;
    HomeFragmentAdapter homeFragmentAdapter;
    private String searchType = "recipe";
    private boolean isRecipeDivVisible = true;
    String Uid;
    ArrayList<RecipeModel> followingPostList, popularPostList, fitnessPostList;
    String ipAddress;
    DatabaseHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipAddress = getResources().getString(R.string.ipAddress);

        db = new DatabaseHelper(getContext());
        Uid = db.getUid();

        searchBar = root.findViewById(R.id.home_search_bar);
        addRecipeBtn = root.findViewById(R.id.homeAddBtn);

        popListShimmer = root.findViewById(R.id.home_pop_list_shimmer);
        popListShimmer.startShimmer();

        fitListShimmer = root.findViewById(R.id.home_explore_list_shimmer);
        fitListShimmer.startShimmer();

        followPost = root.findViewById(R.id.home_shimmer_follow_post);
        followPost.startShimmer();

        followViewPager = root.findViewById(R.id.home_follow_list);

        popRecyclerView = root.findViewById(R.id.home_pop_list);
        fitRecyclerView = root.findViewById(R.id.home_fitness_list);

        popMore = root.findViewById(R.id.home_pop_btn);
        fitMore = root.findViewById(R.id.home_fitness_btn);

        //Nav to view more
        popMore.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ViewMoreActivity.class);
            intent.putExtra("ViewMore", "Popular");
            startActivity(intent);
        });

        fitMore.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), ViewMoreActivity.class);
            intent.putExtra("ViewMore", "Fitness");
            startActivity(intent);
        });

        recipeHistoryList = db.getRecipeHistory();
        userHistoryList = db.getUserHistory();


        getFollowPost();
        getPopularPost();
        getFitnessPost();

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
        clearSearchBtn = searchDialog.findViewById(R.id.search_dialog_clear_btn);
        nullHistory = searchDialog.findViewById(R.id.search_dialog_history_null);
        historyRecyclerView = searchDialog.findViewById(R.id.search_dialog_history);


        if (recipeHistoryList != null) {
            historyAdapter = new SearchHistoryAdapter(recipeHistoryList, "recipe");
            historyRecyclerView.setAdapter(historyAdapter);
            historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            nullHistory.setVisibility(View.GONE);
        }

        //both change search type
        recipeBtn.setOnClickListener(v -> {
            if (recipeHistoryList != null) {
                historyAdapter = new SearchHistoryAdapter(recipeHistoryList, "recipe");
                historyRecyclerView.setAdapter(historyAdapter);
                historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                nullHistory.setVisibility(View.GONE);
            } else {
                nullHistory.setVisibility(View.VISIBLE);
            }
            searchType = "recipe";
            isRecipeDivVisible = true;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.primary_color));
            userBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            searchEditText.setHint("Search Recipe / Ingredient");

            //history recyclerView

        });

        userBtn.setOnClickListener(v -> {
            if (userHistoryList != null){
                historyAdapter = new SearchHistoryAdapter(userHistoryList, "user");
                historyRecyclerView.setAdapter(historyAdapter);
                historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                nullHistory.setVisibility(View.GONE);
            } else {
                nullHistory.setVisibility(View.VISIBLE);
            }

            searchType = "user";
            isRecipeDivVisible = false;
            recipeDiv.setVisibility(isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            userDiv.setVisibility(!isRecipeDivVisible ? View.VISIBLE : View.INVISIBLE);
            recipeBtn.setTextColor(getResources().getColor(R.color.secondaryText_color));
            userBtn.setTextColor(getResources().getColor(R.color.primary_color));
            searchEditText.setHint("Search User");//history recyclerView

//            historyRecyclerView(userHistoryList);
        });

        searchBar.setOnClickListener(v -> searchDialog.show());
        cancelSearchBtn.setOnClickListener(v -> {
            searchDialog.dismiss();
            searchEditText.setText("");
        });

        //intent to SearchRecipe / User activity include editText
        searchBtn.setOnClickListener(v -> {
            if (!searchEditText.getText().toString().isEmpty()) {
                Intent intent = new Intent(getActivity(),
                        searchType.equals("recipe") ? SearchRecipeActivity.class : SearchUserActivity.class);
                intent.putExtra("searchText", String.valueOf(searchEditText.getText()));
                //save history when press search btn
                switch (searchType){
                    case "recipe":
                        db.saveRecipeHistory(String.valueOf(searchEditText.getText()));
                    case "user":
                        db.saveUserHistory(String.valueOf(searchEditText.getText()));
                }
                searchDialog.cancel();
                searchEditText.setText("");
                startActivity(intent);
            }
        });
    }

    private void getFollowPost(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_following_user_recipe.php?RNo=10&Uid="+Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                Log.d("HomeFragmentGET", jsonResponseString);
                followingPostList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    followingPostList.add(recipe);
                }

                connection.disconnect();
            }catch (Exception e){
                Log.d("HomeFragmentGET", "Get Post Error" + e.toString());
            }finally {
                getActivity().runOnUiThread(()->{
                    homeFragmentAdapter = new HomeFragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
                    for (int i = 0; i < followingPostList.size(); i++){
                        FollowingFragment followingFragment = new FollowingFragment(followingPostList.get(i));
                        homeFragmentAdapter.addFragment(followingFragment);
                    }
                    followViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    followViewPager.setAdapter(homeFragmentAdapter);
                    followViewPager.setClipToPadding(false);
                    followViewPager.setClipChildren(false);
                    followViewPager.setOffscreenPageLimit(3);
                    followViewPager.setPadding(40 ,0,40,0);


                    followPost.stopShimmer();
                    followPost.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private void getPopularPost(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_popular_recipe.php?RNo=10&Uid="+Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                popularPostList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    popularPostList.add(recipe);
                }

                connection.disconnect();
            }catch (Exception e){
                Log.d("HomeFragmentGET", "Get Post Error" + e.toString());
            }finally {
                getActivity().runOnUiThread(()->{
                    popularAdapter = new RecipeListAdapter(popularPostList);
                    popRecyclerView.setAdapter(popularAdapter);
                    popRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    popListShimmer.stopShimmer();
                    popListShimmer.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private void getFitnessPost(){
        new Thread(()->{
            try {
                URL url = new URL(ipAddress+"app_fitness_recipe.php?RNo=10&Uid="+Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                fitnessPostList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipe = new RecipeModel();
                    recipe.setRecipeInList(jsonObject);
                    fitnessPostList.add(recipe);
                }

                connection.disconnect();
            }catch (Exception e){
                Log.d("HomeFragmentGET", "Get Post Error: " + e.toString());
            }finally {
                getActivity().runOnUiThread(()->{
                    fitnessAdapter = new RecipeListAdapter(fitnessPostList);
                    fitRecyclerView.setAdapter(fitnessAdapter);
                    fitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    fitListShimmer.stopShimmer();
                    fitListShimmer.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}