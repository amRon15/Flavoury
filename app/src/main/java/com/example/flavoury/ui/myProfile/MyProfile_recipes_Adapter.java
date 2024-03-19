package com.example.flavoury.ui.myProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class MyProfile_recipes_Adapter extends RecyclerView.Adapter<MyProfile_recipes_Adapter.MyViewHolder> {
    @NonNull
    @Override
    public MyProfile_recipes_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tab_recipes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfile_recipes_Adapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageButton recipe_img;
        public MyViewHolder(View v){
            super(v);
            recipe_img=v.findViewById(R.id.myprofilerecipe_img);
        }
    }
}
