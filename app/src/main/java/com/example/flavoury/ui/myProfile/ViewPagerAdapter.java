package com.example.flavoury.ui.myProfile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.flavoury.ui.myProfile.tab_liked;
import com.example.flavoury.ui.myProfile.tab_recipes;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new tab_recipes();
            case 1: return  new tab_liked();
            default:return new tab_recipes();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
