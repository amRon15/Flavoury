package com.example.flavoury.ui.home;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentHomeBinding;
import com.example.flavoury.ui.login.LoginActivity;
import com.example.flavoury.ui.login.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView,popRecyclerView,exploreRecyclerView;
    String[] categoryType;
    ArrayList<RecipeInList> popRecipes = new ArrayList<RecipeInList>();
    ArrayList<RecipeInList> exploreRecipes = new ArrayList<RecipeInList>();

    FirebaseAuth firebaseAuth;
    ImageButton button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        for(int i =0;i<4;i++){
            popRecipes.add(new RecipeInList("user"+i,"salad",140,103));
            exploreRecipes.add(new RecipeInList("user"+i,"Protein Pancake",201,52));
        }


        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.home_catList);
        categoryType = getResources().getStringArray(R.array.category);

        popRecyclerView = root.findViewById(R.id.home_popList);
        exploreRecyclerView = root.findViewById(R.id.home_exploreList);

        RecipeListAdapter popListAdapter = new RecipeListAdapter(popRecipes);
        RecipeListAdapter exploreListAdapter = new RecipeListAdapter(exploreRecipes);

        popRecyclerView.setAdapter(popListAdapter);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        exploreRecyclerView.setAdapter(exploreListAdapter);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryType);
        recyclerView.setAdapter(categoryListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        button = root.findViewById(R.id.homeNotificationBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegistrationActivity.class));
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}