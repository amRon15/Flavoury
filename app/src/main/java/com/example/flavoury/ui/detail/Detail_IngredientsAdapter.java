package com.example.flavoury.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class Detail_IngredientsAdapter extends RecyclerView.Adapter<Detail_IngredientsAdapter.MyviewHolder> {

    String[] food;
    public Detail_IngredientsAdapter(String [] food) {this.food = food;}

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_ingredients,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position){
        holder.food.setText(food[position]);
    }

    @Override
    public int getItemCount() {
        return food.length;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView food;
        public MyviewHolder(@NonNull View itemView){
            super(itemView);
            food = itemView.findViewById(R.id.detail_ingredients_name);
        }
   }
}
