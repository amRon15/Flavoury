package com.example.flavoury.ui.detail;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DetailViewModel extends ViewModel {

    String intentFromRecipe;

    public void setRecipeID(String recipeID) {
        intentFromRecipe = recipeID;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String myUserID;

    {
        assert myUser != null;
        myUserID = myUser.getUid();
    }

    private final MutableLiveData<RecipeModel> recipeList = new MutableLiveData<RecipeModel>();
    RecipeModel recipe;
    private ArrayList<String> myUserLikes = new ArrayList<>();

    public void fetchRecipe() {

        db.collection("recipe").document(intentFromRecipe).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot recipeDocument = task.getResult();
                    if (recipeDocument.exists()) {

                        recipe = recipeDocument.toObject(RecipeModel.class);
                        //get my user like recipe & check
                        db.collection("User").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.get("likeRecipe") != null) {
                                        for (String recipeID : (ArrayList<String>) documentSnapshot.get("likeRecipe")) {
                                            if (recipeID.contains(intentFromRecipe)) {
                                                recipe.setRecipeLike(true);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        db.collection("User").document(recipe.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String userName = documentSnapshot.getString("userName");
                                    String userIcon = documentSnapshot.getString("userIcon");
                                    recipe.setUserName(userName);
                                }
                                recipeList.postValue(recipe);
                            }
                        });
                    }
                }
            }
        });
    }

    public void handleLikeRecipe(boolean isChecked, String recipeID) {
        DocumentReference docRefUser = db.collection("User").document(myUserID);
        DocumentReference docRefRecipe = db.collection("recipe").document(recipeID);
        if (isChecked) {
            docRefUser.update("likeRecipe", FieldValue.arrayUnion(intentFromRecipe));
            docRefRecipe.update("likes", FieldValue.increment(1));

        } else {
            docRefUser.update("likeRecipe", FieldValue.arrayRemove(intentFromRecipe));
            docRefRecipe.update("likes", FieldValue.increment(-1));
        }
    }

    public LiveData<RecipeModel> getRecipeList() {
        return recipeList;
    }

}

