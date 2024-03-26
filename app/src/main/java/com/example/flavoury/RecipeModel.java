package com.example.flavoury;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.util.Map;

public class RecipeModel {

    public RecipeModel(){}
    public RecipeModel(Map<String, Object> data){}
    private String catID, recipeID, recipeName, userID, recipeImg,userName,userIcon;
    private int cookingMinutes, cookingSeconds, like;
    private boolean isPublic;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getRecipeImg() {
        return recipeImg;
    }

    public void setRecipeImg(String recipeImg) {
        this.recipeImg = recipeImg;
    }

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Ingredient getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(Ingredient ingredientID) {
        this.ingredientID = ingredientID;
    }

    public Map<String, String> getInstruction() {
        return instruction;
    }

    public void setInstruction(Map<String, String> instruction) {
        this.instruction = instruction;
    }

    private Timestamp createDate;
    private Ingredient ingredientID;
    private Map<String,String> instruction;
}

class Ingredient{
    private String portion, name;
    private int calories;

    public int getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }

    public String getPortion() {
        return portion;
    }
}

class Instruction{
    private String step;

    public String getStep() {
        return step;
    }
}
