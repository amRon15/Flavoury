package com.example.flavoury.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();
    private MutableLiveData<List<RecipeModel>> randomRecipeList = new MutableLiveData<List<RecipeModel>>();

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }

    public LiveData<List<RecipeModel>> getRandomRecipeList() {
        return randomRecipeList;
    }

}
