package com.example.flavoury;

import android.net.Uri;

import java.sql.Timestamp;

import javax.annotation.Nullable;

public class UserModel {
    private String userName,email;
    private Timestamp createDate;
    private Uri userIcon;

    public UserModel(){}
    public UserModel(String userName, String email, Timestamp createDate, @Nullable Uri userIcon){
//        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.createDate = createDate;
        this.userIcon = userIcon;
    }

    public Uri getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Uri userIcon) {
        this.userIcon = userIcon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

//    public String getUserId() {
//        return userId;
//    }

//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
