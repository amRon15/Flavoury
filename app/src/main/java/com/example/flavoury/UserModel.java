package com.example.flavoury;

import android.util.Log;

import org.json.JSONObject;


public class UserModel {
    public UserModel(){}
    public UserModel(JSONObject jsonObject){
        try{
            this.Uid = jsonObject.getString("Uid");
            this.Username = jsonObject.getString("Username");
            this.Iconid = jsonObject.getString("Iconid");
        }catch (Exception e){
            Log.d("UserModel", "Failed to set userModel: " + e.toString());
        }
    }

    public void UserInfoModel(JSONObject jsonObject){
        try{
            this.recipeNum = jsonObject.getInt("recipeNum");
            this.followerNum = jsonObject.getInt("followerNum");
            this.followingNum = jsonObject.getInt("followingNum");
        }catch (Exception e){
            Log.d("UserModel", "Failed to set userModel: " + e.toString());
        }
    }
    String Uid, Username, Email, Iconid;
    int recipeNum, followingNum, followerNum;

    public int getRecipeNum() {
        return recipeNum;
    }

    public void setRecipeNum(int recipeNum) {
        this.recipeNum = recipeNum;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIconid() {
        return Iconid;
    }

    public void setIconid(String iconid) {
        Iconid = iconid;
    }

    public int getFollowingNum() {
        return followingNum;
    }

    public void setFollowingNum(int followingNum) {
        this.followingNum = followingNum;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }
}
