package com.example.flavoury.ui.likes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LikesViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String myUserID = myUser.getUid();
    private MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<>();
    private ArrayList<RecipeModel> recipes = new ArrayList<>();
    private RecipeModel recipe = new RecipeModel();


    public void fetchRecipe() {
        db.collection("User").document(myUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> likeRecipes = (ArrayList<String>) documentSnapshot.get("likeRecipe");
                db.collection("recipe").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot recipeDocument : queryDocumentSnapshots){
                            for (String recipeID : likeRecipes){
                                if (!recipeID.isEmpty()) {
                                    if (recipeID.equals(recipeDocument.getId())) {
                                        recipe = recipeDocument.toObject(RecipeModel.class);
                                        recipe.setRecipeID(recipeDocument.getId());
                                        recipe.setRecipeLike(true);
                                        db.collection("User").document(recipe.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                recipe.setUserName(documentSnapshot.getString("userName"));
                                                Log.d("recipe", documentSnapshot.getString("userName"));
                                            }
                                        });
                                        recipes.add(recipe);
                                    }
                                }
                            }
                        }
                        recipeList.postValue(recipes);
                    }
                });
            }
        });
    }

    public void matchData(){

    }

    //reset data when current user leave this page
    public void resetDate(){
        recipes.clear();
        recipeList.postValue(recipes);
    }

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}