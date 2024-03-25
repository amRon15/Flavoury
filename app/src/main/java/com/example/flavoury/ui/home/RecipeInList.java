package com.example.flavoury.ui.home;

public class RecipeInList {
    private String user,recipeName;
    private int cal,likes;

    public RecipeInList(String user, String recipeName, int cal, int likes){
        this.cal = cal;
        this.user = user;
        this.recipeName = recipeName;
        this.likes = likes;
    }

    public int getCal() {
        return cal;
    }

    public int getLikes() {
        return likes;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUser() {
        return user;
    }
}
