package com.example.flavoury.ui.addRecipe;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class AddRecipeIngredientAdapter extends RecyclerView.Adapter<AddRecipeIngredientAdapter.MyViewHolder> {
    boolean isRecipeReady;
    ArrayList<AddRecipeModel.Ingredient> ingredients;
    public void setAddRecipeIngredientAdapter(ArrayList<AddRecipeModel.Ingredient> ingredients, boolean isRecipeReady){
        this.ingredients = ingredients;
        this.isRecipeReady = isRecipeReady;
    }

    @NonNull
    @Override
    public AddRecipeIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_recipe_ingredient,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddRecipeIngredientAdapter.MyViewHolder holder, int position) {
        AddRecipeModel.Ingredient ingredient = ingredients.get(position);
        holder.bindData(ingredient,position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        EditText ingredient, portion;
        ImageButton removeBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.add_recipe_ingredient);
            portion = itemView.findViewById(R.id.add_recipe_portion);
            removeBtn = itemView.findViewById(R.id.add_recipe_ingredient_remove);
        }

        void bindData(AddRecipeModel.Ingredient ingredient, int position){
            this.ingredient.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ingredient.setIngredient(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            this.portion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ingredient.setPortion(charSequence.toString());
                    isRecipeReady = charSequence.length()!=0;
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if(position==0){
                removeBtn.setVisibility(View.INVISIBLE);
            }
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ingredients.remove(position);
                    notifyItemRangeChanged(position, ingredients.size());
                }
            });
        }
    }
}
