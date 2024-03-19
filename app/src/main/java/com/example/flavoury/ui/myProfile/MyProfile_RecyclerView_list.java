package com.example.flavoury.ui.myProfile;

public class MyProfile_RecyclerView_list {
    private Integer image;
    private String recipes_name;

    public Integer getImage_pancake() {
        return image;
    }

    public String getPancake() {
        return recipes_name;
    }

    public MyProfile_RecyclerView_list(Integer image_pancake, String pancake) {
        this.image = image_pancake;
        this.recipes_name = pancake;
    }
}
