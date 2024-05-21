package com.example.flavoury.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.example.flavoury.RecipeWithUser;
import com.example.flavoury.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();

    public void fetchRecipes() {
        db.collection("recipe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RecipeModel> recipes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecipeModel recipe = new RecipeModel();
                                recipe.setRecipeID(document.getId());
                                recipe.setUserID(document.getString("userID"));
                                recipe.setRecipeName(document.getString("recipeName"));
                                Long recipeLike = document.getLong("like");
                                Long recipeCookingMinutes = document.getLong("cookingMinutes");
                                if (recipeLike != null) {
                                    recipe.setLike(recipeLike.intValue());
                                } else {
                                    recipe.setLike(0);
                                }
                                if (recipeCookingMinutes != null) {
                                    recipe.setCookingMinutes(recipeCookingMinutes.intValue());
                                } else {
                                    recipe.setCookingMinutes(0);
                                }
                                recipe.setRecipeImg(document.getString("recipeImg"));
                                db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot userDocument : task.getResult()) {
                                                String userName = userDocument.getString("userName");
                                                String userID = userDocument.getId();
                                                String userIcon = userDocument.getString("userIcon");
                                                if (userID.equals(document.getString("userID"))) {
                                                    recipe.setUserName(userName);
                                                    Log.d("RecipeUser",recipe.getUserName());
                                                }
                                            }
                                        }
                                    }
                                });
                                recipes.add(recipe);
                            }
                            recipeList.postValue(recipes);
                        }
                    }
                });
    }


    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}
