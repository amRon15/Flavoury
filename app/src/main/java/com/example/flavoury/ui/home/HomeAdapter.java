package com.example.flavoury.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

    String[] categoryType;

    public HomeAdapter(String[] categoryType) {
        this.categoryType = categoryType;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_btn_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setText(categoryType[position]);
    }

    @Override
    public int getItemCount() {
        return categoryType.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.home_category_btn);
        }
    }
}
