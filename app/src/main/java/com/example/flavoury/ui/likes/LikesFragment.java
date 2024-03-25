package com.example.flavoury.ui.likes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentLikesBinding;
public class LikesFragment extends Fragment {

    private FragmentLikesBinding binding;
    RecyclerView likesRecyclerView;
    String[] testArr = {"a","c","b","b","b","b","b","b","b","b","b","b","b","b","b","b","b","b","b","b","b"};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LikesViewModel likesViewModel =
                new ViewModelProvider(this).get(LikesViewModel.class);

        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        likesRecyclerView = root.findViewById(R.id.likes_postList);
        LikesPostAdapter likesPostAdapter = new LikesPostAdapter(testArr);
        likesRecyclerView.setAdapter(likesPostAdapter);
        likesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}