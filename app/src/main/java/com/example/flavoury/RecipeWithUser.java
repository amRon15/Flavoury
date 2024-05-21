package com.example.flavoury;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.data.model.User;

public class RecipeWithUser {
    private String userID, userName, userIcon;

    public RecipeWithUser(String userID, String userName, @Nullable String userIcon) {
        this.userID = userID;
        this.userName = userName;
        this.userIcon = userIcon;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
}
