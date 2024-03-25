package com.example.flavoury;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecipeModel {

    public RecipeModel(){}
    private String catID;
    private String recipeID;
    private String recipeName;
    private String recipeImg;
    private String userID;
    private int cookingMinutes, cookingSeconds, like;
    private Date createDate;
    private Map<String,Instruction> instruction;
    private Map<String,Map<String,Ingredient>> ingredient;
    private boolean isPublic;

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImg() {
        return recipeImg;
    }

    public void setRecipeImg(String recipeImg) {
        this.recipeImg = recipeImg;
    }

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

    public int getCookingSeconds() {
        return cookingSeconds;
    }

    public void setCookingSeconds(int cookingSeconds) {
        this.cookingSeconds = cookingSeconds;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Map<String, Instruction> getInstruction() {
        return instruction;
    }

    public void setInstruction(Map<String, Instruction> instruction) {
        this.instruction = instruction;
    }

    public Map<String, Map<String, Ingredient>> getIngredient() {
        return ingredient;
    }

    public void setIngredient(Map<String, Map<String, Ingredient>> ingredient) {
        this.ingredient = ingredient;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}

 class Ingredient {
    private String name, portion;
    String calories;

    public Ingredient() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public String getPortion() {
        return portion;
    }

    public String getCalories() {
        return calories;
    }
}

 class Instruction {
    String step;

    public Instruction() {
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}