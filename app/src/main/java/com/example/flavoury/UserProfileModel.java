package com.example.flavoury;

public class UserProfileModel {

    public UserProfileModel(){

    }
   String userName, userID, userIcon;
   int recipeNum, recipeLikes;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getRecipeNum() {
        return recipeNum;
    }

    public void setRecipeNum(int recipeNum) {
        this.recipeNum = recipeNum;
    }

    public int getRecipeLikes() {
        return recipeLikes;
    }

    public void setRecipeLikes(int recipeLikes) {
        this.recipeLikes = recipeLikes;
    }
}
