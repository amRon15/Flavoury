package com.example.flavoury.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;

public class DetailViewModel extends ViewModel {

    String intentFromRecipe;
    private final MutableLiveData<RecipeModel> recipeList = new MutableLiveData<RecipeModel>();

    public void setRecipeID(String recipeID) {
        intentFromRecipe = recipeID;
    }


    public LiveData<RecipeModel> getRecipeList() {
        return recipeList;
    }

}

