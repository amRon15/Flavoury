package com.example.flavoury.ui.home;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView,popRecyclerView;
    String[] categoryType;
    ArrayList<ExploreRecipe> popRecipes = new ArrayList<ExploreRecipe>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        for(int i =0;i<4;i++){
            popRecipes.add(new ExploreRecipe("user"+i,"salad",140,103));
        }

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.home_catList);
        categoryType = getResources().getStringArray(R.array.category);

        popRecyclerView = root.findViewById(R.id.home_popList);

        PopListAdapter popListAdapter = new PopListAdapter(popRecipes);
        popRecyclerView.setAdapter(popListAdapter);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));


        HomeAdapter homeAdapter = new HomeAdapter(categoryType);
        recyclerView.setAdapter(homeAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}