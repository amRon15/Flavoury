package com.example.flavoury.ui.myProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserProfileModel;

import java.util.List;

public class MyProfileViewModel extends ViewModel {

    private final MutableLiveData<UserProfileModel> userProfile = new MutableLiveData<UserProfileModel>();
    private final MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();


    public LiveData<UserProfileModel> getUserData() {
        return userProfile;
    }

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}