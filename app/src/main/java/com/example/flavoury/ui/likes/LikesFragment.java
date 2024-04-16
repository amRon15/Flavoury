package com.example.flavoury.ui.likes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentLikesBinding;

import java.util.List;

public class LikesFragment extends Fragment {

    private FragmentLikesBinding binding;
    RecyclerView likesRecyclerView;
    LikesPostAdapter likesPostAdapter;
    LikesViewModel likesViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        likesRecyclerView = root.findViewById(R.id.likes_postList);
        likesPostAdapter = new LikesPostAdapter();
        likesRecyclerView.setAdapter(likesPostAdapter);
        likesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        likesViewModel = new ViewModelProvider(this).get(LikesViewModel.class);
        likesViewModel.fetchRecipe();
        likesViewModel.getRecipeList().observe(getViewLifecycleOwner(),this::handleRecipes);

        return root;
    }

    public void handleRecipes(List<RecipeModel> recipe){
        likesPostAdapter.setLikesPostAdapter(recipe,getContext());
        likesPostAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        likesViewModel.resetDate();
    }
}