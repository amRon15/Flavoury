package com.example.flavoury.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.Map;

public class Detail_IngredientsAdapter extends RecyclerView.Adapter<Detail_IngredientsAdapter.MyviewHolder> {

    Map<String,Object> ingredients;
    int ingredientSize;
    public void setDetailIngredientsAdapter(Map<String,Object> ingredients, int ingredientSize) {
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
        String ingredientKey = (String) ingredients.keySet().toArray()[position];
        Map<String,String> valueOfIngredient = (Map<String, String>) ingredients.get(ingredientKey);
        String ingredient = (String) valueOfIngredient.get("name");
        holder.food.setText(ingredient);
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
