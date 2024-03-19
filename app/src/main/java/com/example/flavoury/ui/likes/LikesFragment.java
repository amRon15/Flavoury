package com.example.flavoury.ui.likes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.flavoury.databinding.FragmentLikesBinding;
public class LikesFragment extends Fragment {

    private FragmentLikesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LikesViewModel likesViewModel =
                new ViewModelProvider(this).get(LikesViewModel.class);

        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLikes;
        likesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}