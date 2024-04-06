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
    RecipeModel recipe = new RecipeModel();
    private ArrayList<String> myUserLikes = new ArrayList<>();

    public void fetchRecipe() {

        db.collection("recipe").document(intentFromRecipe).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot recipeDocument = task.getResult();
                    if (recipeDocument.exists()) {

                        Map<String, Object> instructions = (Map<String, Object>) recipeDocument.getData().get("instruction");
                        Map<String, Object> ingredients = (Map<String, Object>) recipeDocument.getData().get("ingredientID");

                        Long cookingMinutes = recipeDocument.getLong("cookingMinutes");
                        Long likes = recipeDocument.getLong("like");
                        if (cookingMinutes != null) {
                            recipe.setCookingMinutes(cookingMinutes.intValue());
                        }
                        if (likes != null) {
                            recipe.setLike(likes.intValue());
                        }
                        recipe.setRecipeName(recipeDocument.getString("recipeName"));
//                        recipe.setDescription(recipeDocument.getString(""));
                        String userID = recipeDocument.getString("userID");
                        recipe.setUserID(userID);

                        //convert step to tree map order by asc
                        if (instructions != null) {
                            Map<String, Object> instructionTreeMap = new TreeMap<>(instructions);
                            recipe.setStep(instructionTreeMap);
                        }

                        if (ingredients != null) {
                            Map<String, Object> ingredientTreeMap = new TreeMap<>(ingredients);
                            recipe.setIngredients(ingredientTreeMap);
                        }

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


                        assert userID != null;
                        db.collection("User").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public void handleLikeRecipe(boolean isChecked) {
        if (isChecked) {
            db.collection("User").document(myUserID).update("likeRecipe", FieldValue.arrayUnion(intentFromRecipe));
        } else {
            db.collection("User").document(myUserID).update("likeRecipe", FieldValue.arrayRemove(intentFromRecipe));
        }
    }

    public LiveData<RecipeModel> getRecipeList() {
        return recipeList;
    }

}

