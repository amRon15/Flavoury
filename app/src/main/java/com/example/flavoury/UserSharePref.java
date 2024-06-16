package com.example.flavoury;

import android.content.SharedPreferences;
import android.util.Log;

public class UserSharePref {
    private final SharedPreferences sharePref;
    private final SharedPreferences.Editor shareEdit;
    private final String userPref = "com.example.flavoury.user.login.status";
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

}
