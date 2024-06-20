package com.example.flavoury.ui.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.flavoury.Ingredient;
import com.example.flavoury.RecipeModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    String description;
    ArrayList<String> stepArrayList;
    ArrayList<Ingredient> ingredientArrayList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, RecipeModel recipe){
        super(fragmentActivity);
        this.description = recipe.getDescription();
//        this.stepArrayList = recipe.getSteps();
//        this.ingredientArrayList = recipe.getIngredients();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0){
            return new FragmentDescription(description);
        }else if (position==1) {
            return new FragmentIngredient();
        } else {
            return new FragmentStep();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
