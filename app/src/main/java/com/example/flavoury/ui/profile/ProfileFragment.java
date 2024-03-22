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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.databinding.FragmentSearchBinding;
import com.example.flavoury.ui.myProfile.MyProfile_RecyclerView_list;
import com.example.flavoury.ui.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    RecyclerView profile_myRecyclerView;






    private FragmentMyProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel viewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profile_myRecyclerView = root.findViewById(R.id.profile_myRecyclerView);
        ProfileAdapter adapter = new ProfileAdapter(generateProfileitems());
        int spanCount = 2;
        profile_myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profile_myRecyclerView.setAdapter(adapter);

        /*final TextView textView = binding.textProfile;
        ProfileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;


    }

    private List<Profileitems> generateProfileitems() {
        List<Profileitems> profileitemlists = new ArrayList<>();
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));
        profileitemlists.add(new Profileitems(R.drawable.pancake,"food",""));

        return profileitemlists;
    }

    
    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
