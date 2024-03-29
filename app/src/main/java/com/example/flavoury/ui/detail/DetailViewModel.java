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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void setRecipeID(String recipeID){
        intentFromRecipe = recipeID;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<RecipeModel> recipeList = new MutableLiveData<RecipeModel>();
    RecipeModel recipe = new RecipeModel();


    public void fetchRecipe(){
        DocumentReference docRef = db.collection("recipe").document(intentFromRecipe);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot recipeDocument = task.getResult();
                    if(recipeDocument.exists()){

                        Map<String,Object> instructions = (Map<String, Object>) recipeDocument.getData().get("instruction");
                        Map<String,Object> ingredients = (Map<String, Object>) recipeDocument.getData().get("ingredientID");

                        Long cookingMinutes = recipeDocument.getLong("cookingMinutes");
                        Long likes = recipeDocument.getLong("like");
                        if(cookingMinutes!=null){
                            recipe.setCookingMinutes(cookingMinutes.intValue());
                        }
                        if(likes!=null){
                            recipe.setLike(likes.intValue());
                        }
                        recipe.setRecipeName(recipeDocument.getString("recipeName"));
//                        recipe.setDescription(recipeDocument.getString(""));
                        recipe.setUserID(recipeDocument.getString("userID"));

                        //convert step to tree map order by asc
                        if(instructions!=null) {
                            Map<String, Object> instructionTreeMap = new TreeMap<>(instructions);
                            recipe.setStep(instructionTreeMap);
                        }

                        if(ingredients!=null){
                            Map<String,Object> ingredientTreeMap = new TreeMap<>(ingredients);
                            recipe.setIngredients(ingredientTreeMap);
                            Log.d("recipe",recipe.getIngredients().toString());
                        }
                    }
                }

            }
        });
        db.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot userDocument : task.getResult()) {
                        String userName = userDocument.getString("userName");
                        String userID = userDocument.getId();
                        String userIcon = userDocument.getString("userIcon");
                        if(recipe.getUserID().equals(userID)){
                            recipe.setUserName(userName);
                        }
                    }
                    recipeList.postValue(recipe);
                }
            }
        });
    }

    public LiveData<RecipeModel> getRecipeList() {
        return recipeList;
    }

}

