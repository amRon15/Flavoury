package com.example.flavoury;

import android.net.Uri;

import java.sql.Timestamp;

import javax.annotation.Nullable;

public class UserModel {
    private String userName,password, email;
    private Uri userIcon;
    public UserModel(){}
    public UserModel(String userName, String password, String email, Timestamp createDate, @Nullable Uri userIcon){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.userIcon = userIcon;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
