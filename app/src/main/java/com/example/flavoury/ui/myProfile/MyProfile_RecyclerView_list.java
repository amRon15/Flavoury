package com.example.flavoury.ui.myProfile;

public class MyProfile_RecyclerView_list {
    private String FoodId;
    private String drcText;
    private int recipes_icon;

    public MyProfile_RecyclerView_list(int burgericon, String food , String drcText) {
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

    public MyProfile_RecyclerView_list(String foodId, String drcText, int recipes_icon) {
        FoodId = foodId;
        this.drcText = drcText;
        this.recipes_icon = recipes_icon;
    }
}
