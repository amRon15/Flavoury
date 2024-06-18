package com.example.flavoury.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();

    public LiveData<List<RecipeModel>> getRecipe() {
        return recipeList;
    }
}