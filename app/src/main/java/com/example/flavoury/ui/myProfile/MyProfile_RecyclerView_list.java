package com.example.flavoury.ui.myProfile;
public class MyProfile_RecyclerView_list {
    private String FoodId;
    private String drcText;
    private int recipes_icon;

    public MyProfile_RecyclerView_list(int recipes_icon, String foodId, String drcText) {
        this.recipes_icon = recipes_icon;
        this.FoodId = foodId;
        this.drcText = drcText;
    }

    public String getFoodId() {
        return FoodId;
    }

    public String getDrcText() {
        return drcText;
    }

    public int getRecipes_icon() {
        return recipes_icon;
    }
}