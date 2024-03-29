package com.example.flavoury;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class RecipeModel implements Serializable {

    public RecipeModel(){}
    private String catID, recipeID, recipeName, userID, recipeImg,userName,userIcon,description;
    private int cookingMinutes, cookingSeconds, like;
    private boolean isPublic;
    private Instruction instruction;

    private Map<String,Object> ingredients,step;

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Map<String, Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Object> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, Object> getStep() {
        return step;
    }

    public void setStep(Map<String, Object> step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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


    private Timestamp createDate;
    private Ingredient ingredientID;
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
