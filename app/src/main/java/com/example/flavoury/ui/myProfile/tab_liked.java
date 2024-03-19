package com.example.flavoury.ui.myProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.flavoury.R;

public class tab_liked extends Fragment {

    View v;
public tab_liked() {
}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.fragment_likes,container,false);
            return v;
    }
}