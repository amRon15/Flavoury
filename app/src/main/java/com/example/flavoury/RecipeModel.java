package com.example.flavoury;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class RecipeModel {

    public RecipeModel(){}
    String RName, Description, Uid, Serving, CookTime, Category, Imgid;
    int Likes;
    ArrayList<Ingredient> ingredients;
    ArrayList<String> steps;

    public RecipeModel(String Uid, String RName, String Category, String CookTime, String Description, int Likes, String Serving, @Nullable String Imgid, ArrayList<Ingredient> ingredients, ArrayList<String> steps) {
        this.Uid = Uid;
        this.RName = RName;
        this.Category = Category;
        this.CookTime = CookTime;
        this.Description = Description;
        this.Likes = Likes;
        this.Serving = Serving;
        this.Imgid = Imgid;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getImgid() {
        return Imgid;
    }

    public void setImgid(String imgid) {
        Imgid = imgid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getCookTime() {
        return CookTime;
    }

    public void setCookTime(String cookTime) {
        this.CookTime = cookTime;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRName() {
        return RName;
    }

    public void setRName(String RName) {
        this.RName = RName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public String getServing() {
        return Serving;
    }

    public void setServing(String serving) {
        Serving = serving;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }
}
