package com.example.flavoury.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;

import java.util.ArrayList;

public class DetailIngredientsAdapter extends RecyclerView.Adapter<DetailIngredientsAdapter.MyviewHolder> {

    ArrayList<Ingredients> ingredients;
    int ingredientSize;
    public void setDetailIngredientsAdapter(ArrayList<Ingredients> ingredients, int ingredientSize) {
        this.ingredients = ingredients;
        this.ingredientSize = ingredientSize;
        }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_ingredients,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position){
        Ingredients ingredient = ingredients.get(position);
        holder.food.setText(ingredient.getPortion() + " "+ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientSize;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView food;
        public MyviewHolder(@NonNull View itemView){
            super(itemView);
            food = itemView.findViewById(R.id.detail_ingredients_name);
        }
    }
}
