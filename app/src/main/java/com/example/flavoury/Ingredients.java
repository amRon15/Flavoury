package com.example.flavoury;

import java.io.Serializable;

public class Ingredients implements Serializable {

    public Ingredients() {
    }

    private String ingredient, portion;

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
