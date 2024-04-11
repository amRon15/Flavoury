package com.example.flavoury;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class RecipeModel implements Serializable {

    public RecipeModel(){}

    public RecipeModel(String recipeName, String userID, @Nullable String description, int cookingMinutes, ArrayList<String> steps, ArrayList<Ingredients> ingredients, Date createDate) {
        this.recipeName = recipeName;
        this.userID = userID;
        this.description = description;
        this.cookingMinutes = cookingMinutes;
        this.steps = steps;
        this.ingredients = ingredients;
        this.createDate = createDate;
    }

    private String category, recipeID ,recipeName, userID, recipeImg,userName,userIcon,description;
    private int cookingMinutes, cookingSeconds, like;
    private boolean isPublic,isRecipeLike;
    private ArrayList<String> steps;
    private ArrayList<Ingredients> ingredients;
    private Date createDate;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public boolean isRecipeLike() {
        return isRecipeLike;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean getIsRecipeLike() {
        return isRecipeLike;
    }

    public void setRecipeLike(boolean recipeLike) {
        isRecipeLike = recipeLike;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


}
