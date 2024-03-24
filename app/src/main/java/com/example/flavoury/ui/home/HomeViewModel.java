package com.example.flavoury.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.example.flavoury.RecipeWithUser;
import com.example.flavoury.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<RecipeWithUser>> recipeList = new MutableLiveData<>();

    public void fetchRecipes(){

    }


    public LiveData<List<RecipeWithUser>> getRecipeList() {
        return recipeList;
    }
}
