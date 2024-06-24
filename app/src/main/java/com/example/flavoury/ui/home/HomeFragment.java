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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.FireBaseToDB;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.search.SearchRecipeActivity;
import com.example.flavoury.ui.search.SearchUserActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.divider.MaterialDivider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    Button searchBar;
    ImageButton addRecipeBtn, cancelSearchBtn, searchBtn;
    Dialog searchDialog;
    Button popMore, fitMore, recipeBtn, userBtn;
    MaterialDivider recipeDiv, userDiv;
    EditText searchEditText;
    ArrayList<String> recipeHistoryList, userHistoryList;
    ShimmerFrameLayout popListShimmer, fitListShimmer, followPost;
    ViewPager2 followViewPager;
    RecyclerView popRecyclerView, fitRecyclerView, historyRecyclerView;
    RecipeListAdapter popularAdapter, fitnessAdapter;
    HomeFragmentAdapter homeFragmentAdapter;
    private String searchType = "recipe";
    private boolean isRecipeDivVisible = true;
    String Uid;
    ArrayList<RecipeModel> followingPostList, popularPostList, fitnessPostList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        DatabaseHelper db = new DatabaseHelper(getContext());
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
                searchDialog.cancel();
                searchEditText.setText("");
                //save history when press search btn

                startActivity(intent);
            }
        });
    }


    private void getFollowPost(){
        new Thread(()->{
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_following_user_recipe.php?RNo=10&Uid="+Uid);

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
                    followPost.stopShimmer();
                    followPost.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private void getPopularPost(){
        new Thread(()->{
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_popular_recipe.php?RNo=10");

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
                URL url = new URL("http://10.0.2.2/Flavoury/app_fitness_recipe.php?RNo=10");

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
                Log.d("HomeFragmentGET", jsonResponseString);
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