package com.example.flavoury.ui.likes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    String ipAddress;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //layout: fragment_likes.xml
        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //10.0.2.2
        ipAddress = getResources().getString(R.string.ipAddress);

        //recycler view adapter: LikesPostAdapter

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(),onBackPressedCallback);

//        likesRecyclerView = root.findViewById(R.id.likes_postList);
//        likesPostAdapter = new LikesPostAdapter();
//        likesRecyclerView.setAdapter(likesPostAdapter);
//        likesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));


        return root;
    }

    public void handleRecipes(List<RecipeModel> recipe){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}