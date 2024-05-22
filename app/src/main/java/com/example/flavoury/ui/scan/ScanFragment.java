package com.example.flavoury.ui.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.databinding.FragmentScanBinding;


public class ScanFragment extends Fragment {
    private FragmentScanBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        myProfileRecipeRecyclerView = root.findViewById(R.id.my_profile_recipe_recyclerView);
//        myProfileRecipeAdapter = new MyProfileRecipeAdapter();
//        myProfileRecipeRecyclerView.setAdapter(myProfileRecipeAdapter);
//        myProfileRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}