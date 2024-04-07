package com.example.flavoury.ui.addRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flavoury.databinding.FragmentAddRecipeBinding;
import com.example.flavoury.databinding.FragmentHomeBinding;

public class addRecipeFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActionBar().hide();


    }

}