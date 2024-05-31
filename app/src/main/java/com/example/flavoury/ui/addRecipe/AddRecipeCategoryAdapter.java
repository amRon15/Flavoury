package com.example.flavoury.ui.addRecipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeCategoryAdapter extends RecyclerView.Adapter<AddRecipeCategoryAdapter.MyViewHolder> {
    private String[] categoryList;

    public AddRecipeCategoryAdapter(String[] categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String category = categoryList[position];
        holder.bindData(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button category;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.add_recipe_category);
        }

        void bindData(String category){
            this.category.setText(category);
        }
    }
}
