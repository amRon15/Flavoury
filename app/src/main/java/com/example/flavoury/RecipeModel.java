package com.example.flavoury;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class RecipeModel implements Serializable {

    //use for get recipe data
    public RecipeModel(){}

    //use for add / update recipe
    public RecipeModel(String recipeName, String userID, @Nullable String description, String cookingMinutes, String servingSize, ArrayList<String> steps, ArrayList<Ingredients> ingredients, Date createDate) {
        this.recipeName = recipeName;
        this.userID = userID;
        this.description = description;
        this.cookingMinutes = cookingMinutes;
        this.steps = steps;
        this.ingredients = ingredients;
        this.createDate = createDate;
        this.servingSize = servingSize;
    }

    private String category, recipeID ,recipeName, userID, recipeImg,userName,userIcon,description, servingSize, cookingMinutes;
    private int like;
    private boolean isPublic,isRecipeLike;
    private ArrayList<String> steps;
    private ArrayList<Ingredients> ingredients;
    private Date createDate;

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

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

    public String getCookingMinutes() {
        return cookingMinutes;
    }

    public void setCookingMinutes(String cookingMinutes) {
        this.cookingMinutes = cookingMinutes;
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
