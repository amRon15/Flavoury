package com.example.flavoury.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.Ingredients;
import com.example.flavoury.R;

import java.util.ArrayList;

public class DetailIngredientsAdapter extends RecyclerView.Adapter<DetailIngredientsAdapter.MyviewHolder> {

    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

    public DetailIngredientsAdapter(ArrayList<Ingredient> ingredientArrayList){
        this.ingredientArrayList = ingredientArrayList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_ingredients,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position){
        Ingredient ingredient = ingredientArrayList.get(position);
        holder.food.setText(ingredient.getPortion() + " "+ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (ingredientArrayList == null){
            return 0;
        }else {
            return ingredientArrayList.size();
        }
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView food;
        public MyviewHolder(@NonNull View itemView){
            super(itemView);
            food = itemView.findViewById(R.id.detail_ingredients_name);
        }
    }
}
