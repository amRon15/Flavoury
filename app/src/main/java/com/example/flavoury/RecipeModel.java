package com.example.flavoury;

import android.util.Log;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class RecipeModel implements Serializable {

    public RecipeModel(){}

    String Rid, RName, Description, Uid, Serving, CookTime, Category, Imgid;
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

    public RecipeModel(JSONObject jsonObject){
        try {
            this.Rid = jsonObject.getString("Rid");
            this.Uid = jsonObject.getString("Uid");
            this.RName = jsonObject.getString("RName");
            this.Category = jsonObject.getString("Category");
            this.CookTime = jsonObject.getString("CookTime");
            this.Description = jsonObject.getString("Description");
            this.Likes = jsonObject.getInt("Likes");
            this.Serving = jsonObject.getString("Serving");
            this.Imgid = jsonObject.getString("Imgid");
        }catch (Exception e){
            Log.d("RecipeModel", "Failed to set recipeModel: " + e.toString());
        }
    }

    public String getRid() {
        return Rid;
    }

    public void setRid(String rid) {
        Rid = rid;
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
