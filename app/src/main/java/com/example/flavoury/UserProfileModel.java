package com.example.flavoury;

public class UserProfileModel {

    public UserProfileModel() {
    }

    String userName, userID, userIcon;
    int recipeNum, recipeLikes, followerNum, followingNum;

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

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public int getFollowingNum() {
        return followingNum;
    }

    public void setFollowingNum(int followingNum) {
        this.followingNum = followingNum;
    }
}
