package com.example.flavoury.ui.likes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;

import java.util.List;

public class LikesViewModel extends ViewModel {
    private MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<>();

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}