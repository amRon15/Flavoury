package com.example.flavoury;

public class Ingredient{
    public Ingredient(){}

    public Ingredient(String ingredient, String portion){
        this.ingredient = ingredient;
        this.portion = portion;
    }
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
