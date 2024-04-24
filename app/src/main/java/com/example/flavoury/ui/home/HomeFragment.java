package com.example.flavoury.ui.home;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.login.LoginActivity;
import com.example.flavoury.ui.login.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView catRecyclerView,popRecyclerView,exploreRecyclerView;
    String[] categoryType;
    RecipeListAdapter popListAdapter,exploreListAdapter;
    HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton addRecipeBtn = root.findViewById(R.id.homeAddBtn);
        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
                Intent addRecipeIntent = new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(addRecipeIntent);
            }
        });

        categoryType = getResources().getStringArray(R.array.category);

        catRecyclerView = root.findViewById(R.id.home_catList);
        popRecyclerView = root.findViewById(R.id.home_popList);
        exploreRecyclerView = root.findViewById(R.id.home_exploreList);

        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryType);
        popListAdapter = new RecipeListAdapter();
        exploreListAdapter = new RecipeListAdapter();

        popRecyclerView.setAdapter(popListAdapter);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        exploreRecyclerView.setAdapter(exploreListAdapter);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        catRecyclerView.setAdapter(categoryListAdapter);
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.fetchRecipes();
        homeViewModel.getRecipeList().observe(getViewLifecycleOwner(),this::handleRecipes);
        homeViewModel.getRandomRecipeList().observe(getViewLifecycleOwner(), this::handleRandomRecipe);
        return root;
    }

    private void handleRecipes(List<RecipeModel> recipe){
        popListAdapter.setRecipeListAdapter(recipe,getContext(),homeViewModel);
        popListAdapter.notifyItemRangeChanged(0, popListAdapter.getItemCount());
    }

    private void handleRandomRecipe(List<RecipeModel> recipe){
        exploreListAdapter.setRecipeListAdapter(recipe,getContext(),homeViewModel);
        exploreListAdapter.notifyItemRangeChanged(0, exploreListAdapter.getItemCount());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeViewModel.resetData();
    }

}