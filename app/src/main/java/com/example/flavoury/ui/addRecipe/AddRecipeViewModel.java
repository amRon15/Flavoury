package com.example.flavoury.ui.addRecipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.flavoury.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddRecipeViewModel extends ViewModel {

    private final MutableLiveData<String> myUserID = new MutableLiveData<>();
    private final FirebaseUser myUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchMyUserID() {
        myUserID.setValue(myUser.getUid());
    }

    public void addRecipeToDB(RecipeModel recipe, OnBackPressedCallback onBackPressedCallback, Context context){
        db.collection("recipe").add(recipe).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isComplete()){
                    onBackPressedCallback.handleOnBackPressed();
                    Toast.makeText(context,"Upload Recipe Successful!",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILED",e.toString());
            }
        });
    }

    public LiveData<String> getUserID() {
        return myUserID;
    }
}