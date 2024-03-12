package com.example.flavoury.ui.addRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class addRecipeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public addRecipeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is create recipe fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}