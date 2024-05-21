package com.example.flavoury.ui.myProfile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserProfileModel;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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