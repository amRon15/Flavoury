package com.example.flavoury;

import com.firebase.ui.auth.data.model.User;

public class RecipeWithUser {
    private RecipeModel recipe;
    private UserModel user;

    public RecipeWithUser(RecipeModel recipe, UserModel user){
        this.recipe = recipe;
        this.user = user;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    public UserModel getUser() {
        return user;
    }
}
