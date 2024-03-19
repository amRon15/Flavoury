package com.example.flavoury.ui.profile;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.databinding.FragmentSearchBinding;
import com.example.flavoury.ui.search.SearchViewModel;

public class ProfileFragment extends Fragment {


    private FragmentMyProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel viewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        /*final TextView textView = binding.textProfile;
        ProfileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
