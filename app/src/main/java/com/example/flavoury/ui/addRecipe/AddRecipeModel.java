package com.example.flavoury.ui.addRecipe;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AddRecipeModel {

    public AddRecipeModel(){}

    public AddRecipeModel(String recipeName, @Nullable String description, int cookingMinutes, ArrayList<Ingredient> ingredients, ArrayList<Step> steps,String userID) {
        this.recipeName = recipeName;
        this.description = description;
        this.cookingMinutes = cookingMinutes;
        this.ingredients = ingredients;
        this.steps = steps;
        this.userID = userID;
    }

    String recipeName, description, userID;
    int cookingMinutes;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCookingMinutes() {
        return cookingMinutes;
    }

    public void setCookingMinutes(int cookingMinutes) {
        this.cookingMinutes = cookingMinutes;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    static class Ingredient{
        public Ingredient(){}
        String ingredient, portion;

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String getPortion() {
            return portion;
        }

        public void setPortion(String portion) {
            this.portion = portion;
        }
    }

    static class Step{
        String step;

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }
    }
}
