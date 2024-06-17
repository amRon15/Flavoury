package com.example.flavoury.ui.addRecipe;

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

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;

import java.util.ArrayList;

public class AddRecipeIngredientAdapter extends RecyclerView.Adapter<AddRecipeIngredientAdapter.MyViewHolder> {
    ArrayList<Ingredients> ingredients;

    public AddRecipeIngredientAdapter(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public AddRecipeIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_recipe_ingredient,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddRecipeIngredientAdapter.MyViewHolder holder, int position) {
        Ingredients ingredient = ingredients.get(position);
        holder.bindData(ingredient);

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        EditText editIngredient, editPortion;
        ImageButton removeBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editIngredient = itemView.findViewById(R.id.add_recipe_ingredient);
            editPortion = itemView.findViewById(R.id.add_recipe_portion);
            removeBtn = itemView.findViewById(R.id.add_recipe_ingredient_remove);
        }

        void bindData(Ingredients ingredient){
            editIngredient.addTextChangedListener(new TextWatcher() {
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
            editPortion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ingredient.setPortion(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if(getLayoutPosition()==0){
                removeBtn.setVisibility(View.INVISIBLE);
                removeBtn.setEnabled(false);
            }
            removeBtn.setOnClickListener(view -> {
                Log.d("AddAdapter", "Data: " + ingredients.get(getAdapterPosition()).getIngredient());
                ingredients.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            });
        }
    }
}
