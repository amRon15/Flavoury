package com.example.flavoury.ui.myProfile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    public final String myUserID = myUser.getUid();
    private final MutableLiveData<UserProfileModel> userProfile = new MutableLiveData<UserProfileModel>();
    private final MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();
    private ArrayList<RecipeModel> recipes = new ArrayList<>();
    private UserProfileModel myUserProfile = new UserProfileModel();
    private int recipeLikes;

    public void fetchMyUserData() {
        db.collection("User").document(myUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myUserProfile.setUserID(myUserID);
                myUserProfile.setUserName(documentSnapshot.getString("userName"));
            }
        });
    }

    public void fetchRecipe() {
        db.collection("recipe").whereEqualTo("userID", myUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        RecipeModel recipe = new RecipeModel();
                        recipe.setRecipeName(documentSnapshot.getString("recipeName"));
                        recipe.setRecipeID(documentSnapshot.getId());
                        Long likes = documentSnapshot.getLong("like");
                        if (likes != null) {
                            recipeLikes += likes.intValue();
                        }
//                        recipe.setRecipeImg(documentSnapshot.getString("recipeImg"));
                        recipes.add(recipe);
                    }
                    myUserProfile.setRecipeLikes(recipeLikes);
                    myUserProfile.setRecipeNum(recipes.size());
                    userProfile.postValue(myUserProfile);
                    recipeList.postValue(recipes);
                }
            }
        });
    }

    public void resetData() {
        recipes.clear();
        recipeList.postValue(recipes);
    }

    public LiveData<UserProfileModel> getUserData() {
        return userProfile;
    }

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}