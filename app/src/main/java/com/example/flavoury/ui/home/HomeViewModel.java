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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();
    private final ArrayList<RecipeModel> recipes = new ArrayList<>();
    private ArrayList<String> likeRecipes = new ArrayList<>();
    private ArrayList<String> recipeUserIDs = new ArrayList<>();
    private final FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String myUserID = myUser.getUid();

    public void fetchRecipes() {
        db.collection("User").document(myUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    likeRecipes = (ArrayList<String>) documentSnapshot.get("likeRecipe");
                }
            }
        });
        db.collection("recipe").limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecipeModel recipe = new RecipeModel();
                                recipe.setRecipeID(document.getId());
                                for(String likeRecipe : likeRecipes){
                                    if(likeRecipe.contains(document.getId())){
                                        recipe.setRecipeLike(true);
                                    }
                                }
                                String recipeUserID = document.getString("userID");
                                recipeUserIDs.add(recipeUserID);
                                recipe.setUserID(recipeUserID);
                                db.collection("User").document(recipeUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        recipe.setUserName(documentSnapshot.getString("userName"));
                                    }
                                });
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
//                                recipe.setRecipeImg(document.getString("recipeImg"));

                                recipes.add(recipe);
                            }
                            recipeList.postValue(recipes);
                        }
                    }
                });
    }

    public void handleLikeRecipe(boolean isChecked,String recipeID){
        if (isChecked) {
            db.collection("User").document(myUserID).update("likeRecipe", FieldValue.arrayUnion(recipeID));
        }else {
            db.collection("User").document(myUserID).update("likeRecipe", FieldValue.arrayRemove(recipeID));
        }
    }

    public LiveData<List<RecipeModel>> getRecipeList() {
        return recipeList;
    }
}
