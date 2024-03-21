package com.example.flavoury.ui.login;

import android.media.Image;
import android.widget.ImageButton;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RegistrationUserModel {
    private String userName,userId,email;
    private Timestamp createDate;
    private ImageButton userIcon;


    public RegistrationUserModel(String userId, String userName, String email, Timestamp createDate,ImageButton userIcon){
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.createDate = createDate;
        this.userIcon = userIcon;
    }

    public ImageButton getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(ImageButton userIcon) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
