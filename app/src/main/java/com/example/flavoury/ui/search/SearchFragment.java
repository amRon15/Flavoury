package com.example.flavoury.ui.search;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentSearchBinding;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.divider.MaterialDivider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private String searchType = "recipe";
    String Uid, ipAddress;
    Button searchBoxBtn;
    ImageButton cancelBtn, searchBtn;
    TextView clearSearchBtn;
    RecyclerView historyRecyclerView, recipeRecyclerView, shimmerRecyclerView;
    MaterialDivider recipeDiv, userDiv;
    Button recipeBtn, userBtn;
    Dialog searchDialog;
    EditText searchEditText;
    ArrayList<RecipeModel> randomRecipe = new ArrayList<>();
    ExploreRecipeAdapter exploreRecipeAdapter;
    boolean isRecipeDivVisible = true;
    DatabaseHelper db;
    ArrayList<String> ridList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ridList.add("0");
        ridList.add("1");


        db = new DatabaseHelper(getContext());
        Uid = db.getUid();

        searchBoxBtn = root.findViewById(R.id.search_search_bar);
        recipeRecyclerView = root.findViewById(R.id.search_recipeRecyclerView);

//        recipeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (recyclerView.canScrollVertically(1)){
//                    getRandomRecipe();
//                }
//            }
//        });

        getRandomRecipe();

        ipAddress = getResources().getString(R.string.ipAddress);

        searchView(root);
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

        cancelBtn = searchDialog.findViewById(R.id.search_dialog_cancelBtn);
        recipeDiv = searchDialog.findViewById(R.id.search_dialog_recipeDiv);
        userDiv = searchDialog.findViewById(R.id.search_dialog_userDiv);
        recipeBtn = searchDialog.findViewById(R.id.search_dialog_recipe);
        userBtn = searchDialog.findViewById(R.id.search_dialog_user);
        searchBtn = searchDialog.findViewById(R.id.search_dialog_btn);
        searchEditText = searchDialog.findViewById(R.id.search_dialog_edit);
        clearSearchBtn = searchDialog.findViewById(R.id.search_dialog_clear_btn);

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

    private void getRandomRecipe(){
        new Thread(()->{
            try {
                String encodedArray = URLEncoder.encode(TextUtils.join(",", ridList), "UTF-8");
                URL url = new URL(ipAddress+"app_random_recipe.php?Uid="+Uid+"&Rid="+encodedArray);

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
                JSONArray recipeJSONArray = new JSONArray(jsonResponseString);
                Log.d("SearchFragmentGET", jsonResponseString);
                for (int i = 0; i < recipeJSONArray.length(); i++){
                    JSONObject jsonObject = recipeJSONArray.getJSONObject(i);
                    RecipeModel recipeModel = new RecipeModel();
                    recipeModel.setRecipeInList(jsonObject);
                    randomRecipe.add(recipeModel);
                    ridList.add(recipeModel.getRid());
                }

                connection.disconnect();
            } catch (Exception e){
                Log.d("SearchFragmentGET", e.toString());
            } finally {
                getActivity().runOnUiThread(()->{
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);
                    exploreRecipeAdapter = new ExploreRecipeAdapter(randomRecipe);
                    recipeRecyclerView.setAdapter(exploreRecipeAdapter);
                    recipeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}