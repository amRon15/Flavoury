package com.example.flavoury.ui.profile;

public class Profileitems {
    private String Food;
    private String Text;
    private int icon;

    public Profileitems(int icon, String Food, String Text) {
        this.icon = icon;
        this.Food = Food;
        this.Text = Text;
    }

    public String getFood() {
        return Food;
    }

    public String getText() {
        return Text;
    }

    public int getIcon() {
        return icon;
    }

}
