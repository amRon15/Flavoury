package com.example.flavoury.ui.addRecipe;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class AddRecipeModel {

    public AddRecipeModel(){}
    String recipeName, description, userID, servingSize, cookingMinutes, category;
    int likes;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    public AddRecipeModel(String recipeName,@Nullable String description, String userID, String cookingMinutes, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, String servingSize ,@Nullable int likes, String category) {
        this.recipeName = recipeName;
        this.description = description;
        this.userID = userID;
        this.cookingMinutes = cookingMinutes;
        this.ingredients = ingredients;
        this.steps = steps;
        this.likes = likes;
        this.servingSize = servingSize;
        this.category = category;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCookingMinutes() {
        return cookingMinutes;
    }

    public void setCookingMinutes(String cookingMinutes) {
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
