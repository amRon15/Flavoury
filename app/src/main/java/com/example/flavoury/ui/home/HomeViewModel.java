package com.example.flavoury.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.example.flavoury.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();
    private MutableLiveData<List<RecipeModel>> randomRecipeList = new MutableLiveData<List<RecipeModel>>();
    private final ArrayList<RecipeModel> recipes = new ArrayList<>();
    private ArrayList<RecipeModel> randomRecipes = new ArrayList<>();
    private ArrayList<String> likeRecipes = new ArrayList<>();
    private ArrayList<String> recipeUserIDs = new ArrayList<>();
    private final FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String myUserID = myUser.getUid();
    private RecipeModel recipe;

    public void fetchRecipes() {
        //fetching current user's Field likeRecipe recipeID
        db.collection("User").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    likeRecipes = (ArrayList<String>) documentSnapshot.get("likeRecipe");
                    //fetching 5 recipe from recipe Collection
                    db.collection("recipe").limit(5)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            recipe = document.toObject(RecipeModel.class);
                                            recipe.setRecipeID(document.getId());
                                            //loop recipeID of user's likeRecipe & if true set it to is like, so the heart btn will be filled
                                            for(String likeRecipe : likeRecipes){
                                                if(likeRecipe.contains(document.getId())){
                                                    recipe.setRecipeLike(true);
                                                }
                                            }
                                            //fetching the user of this recipe
                                            db.collection("User").document(recipe.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    recipe.setUserName(documentSnapshot.getString("userName"));
                                                }
                                            });
                                            randomRecipes.add(recipe);
                                            recipes.add(recipe);
                                        }
                                        //shuffle is to random the recipe to send it to the explore recipe list
                                        Collections.shuffle(randomRecipes);
                                        randomRecipeList.setValue(randomRecipes);
                                        recipeList.setValue(recipes);
                                    }
                                }
                            });
                }
            }
        });

    }

    public void matchData(){

    }

    //add / remove recipeID inside User collection -> current user document -> Field likesRecipe when toggle heart button
    public void handleLikeRecipe(boolean isChecked,String recipeID){
        DocumentReference docRefUser = db.collection("User").document(myUserID);
        DocumentReference docRefRecipe = db.collection("recipe").document(recipeID);
        if (isChecked) {
            docRefUser.update("likeRecipe", FieldValue.arrayUnion(recipeID));
            docRefRecipe.update("likes", FieldValue.increment(1));

        } else {
            docRefUser.update("likeRecipe", FieldValue.arrayRemove(recipeID));
            docRefRecipe.update("likes", FieldValue.increment(-1));
        }
    }

    //reset data if user leave the current page
    public void resetData(){
        recipes.clear();
        randomRecipes.clear();
        recipeList.postValue(recipes);
        randomRecipeList.postValue(randomRecipes);
    }

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }

    public LiveData<List<RecipeModel>> getRandomRecipeList() {
        return randomRecipeList;
    }

}
