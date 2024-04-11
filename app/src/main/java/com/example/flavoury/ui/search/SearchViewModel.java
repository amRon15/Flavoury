package com.example.flavoury.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<RecipeModel> recipes = new ArrayList<>();
    private final MutableLiveData<List<RecipeModel>> recipeList = new MutableLiveData<List<RecipeModel>>();

    public void fetchRecipe(){
        db.collection("recipe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    RecipeModel recipe = documentSnapshot.toObject(RecipeModel.class);
                    recipe.setRecipeID(documentSnapshot.getId());

                    db.collection("User").document(recipe.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            recipe.setUserName(documentSnapshot.getString("userName"));
                            recipes.add(recipe);
                        }
                    });
                }
                recipeList.postValue(recipes);
            }
        });
    }

    public void resetData(){
        recipes = new ArrayList<>();
        recipeList.postValue(recipes);
    }
    public LiveData<List<RecipeModel>> getRecipe() {
        return recipeList;
    }
}