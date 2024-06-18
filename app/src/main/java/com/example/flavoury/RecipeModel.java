package com.example.flavoury;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class RecipeModel {

    public RecipeModel(){}
    String RName, Description, Uid, Serving, CookTime, Category, Imgid;
    int Likes;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    public RecipeModel(String Uid, String RName, String Category, String CookTime, String Description, int Likes, String Serving, @Nullable String Imgid) {
        this.Uid = Uid;
        this.RName = RName;
        this.Category = Category;
        this.CookTime = CookTime;
        this.Description = Description;
        this.Likes = Likes;
        this.Serving = Serving;
        this.Imgid = Imgid;
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

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
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

    static class Ingredient{
        public Ingredient(){}
        String ingredient, portion;

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String getPortion() {
            return portion;
        }

        public void setPortion(String portion) {
            this.portion = portion;
        }
    }

    static class Step{
        String step;

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }
    }
}
