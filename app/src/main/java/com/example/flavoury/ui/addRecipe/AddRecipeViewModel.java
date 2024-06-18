package com.example.flavoury.ui.addRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddRecipeViewModel extends ViewModel {

    private final MutableLiveData<String> myUserID = new MutableLiveData<>();

    public LiveData<String> getUserID() {
        return myUserID;
    }
}