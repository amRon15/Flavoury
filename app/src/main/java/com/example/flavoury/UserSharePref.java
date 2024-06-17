package com.example.flavoury;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class UserSharePref {
    private final SharedPreferences sharePref;
    private final SharedPreferences.Editor shareEdit;
    private final String userPref = "com.example.flavoury.user.login.status";
    private final String recipeHistoryPref = "com.example.flavoury.user.search.recipe";
    private final String userHistoryPref= "com.example.flavoury.user.search.user";
    public UserSharePref(SharedPreferences sharePref){
        this.sharePref = sharePref;
        this.shareEdit = sharePref.edit();
    }

    public void setLoginStatus(boolean bool){
        shareEdit.putBoolean(userPref, bool);
        shareEdit.apply();
        Log.d("shareRef", "User Logged In: " + getLoginStatus());
    }

    public boolean getLoginStatus(){
        final boolean loginStatus = sharePref.getBoolean(userPref, false);
        Log.d("shareRef", "User Logged In: " + loginStatus);
        return loginStatus;

    }

    public void setRecipeHistory(Set<String> stringSet){
        shareEdit.putStringSet(recipeHistoryPref, stringSet);
        shareEdit.apply();
        Log.d("shareRef", "Recipe History: " + getRecipeHistory());
    }

    public Set<String> getRecipeHistory(){
        return sharePref.getStringSet(recipeHistoryPref, null);
    }

    public void setUserHistory(Set<String> stringSet){
        shareEdit.putStringSet(userHistoryPref, stringSet);
        shareEdit.apply();
        Log.d("shareRef", "User History: " + getUserHistory());
    }

    public Set<String> getUserHistory(){
        return sharePref.getStringSet(userHistoryPref, null);
    }

}
