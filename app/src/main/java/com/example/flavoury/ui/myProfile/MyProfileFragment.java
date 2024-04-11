package com.example.flavoury.ui.myProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserProfileModel;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.ui.Setting.SettingActivity;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    RecyclerView myProfileRecipeRecyclerView;
    MyProfileRecipeAdapter myProfileRecipeAdapter;
    MyProfileViewModel myProfileViewModel;
    ImageButton settingBtn, addRecipeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myProfileRecipeRecyclerView = root.findViewById(R.id.my_profile_recipe_recyclerView);
        myProfileRecipeAdapter = new MyProfileRecipeAdapter();
        myProfileRecipeRecyclerView.setAdapter(myProfileRecipeAdapter);
        myProfileRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        myProfileViewModel.fetchRecipe();
        myProfileViewModel.fetchMyUserData();
        myProfileViewModel.getRecipeList().observe(getViewLifecycleOwner(), this::handleRecipe);
        myProfileViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<UserProfileModel>() {
            @Override
            public void onChanged(UserProfileModel userProfileModel) {
                setView(root, userProfileModel);
            }
        });

        return root;
    }

    private void setView(View root, UserProfileModel userData) {
        settingBtn = root.findViewById(R.id.my_profile_setting);
        addRecipeBtn = root.findViewById(R.id.my_profile_addRecipe);
        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
                Intent addRecipeIntent = new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(addRecipeIntent);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
                Intent intent = new Intent(requireActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        TextView recipeNum = root.findViewById(R.id.my_profile_recipeNum);
        TextView recipeLikes = root.findViewById(R.id.my_profile_likeNum);
        TextView userName = root.findViewById(R.id.my_profile_userName);

        userName.setText(userData.getUserName());
    }


    private void handleRecipe(List<RecipeModel> recipe) {
        myProfileRecipeAdapter.setMyProfileRecipeAdapter(recipe, getContext());
        myProfileRecipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myProfileViewModel.resetData();
    }
}
