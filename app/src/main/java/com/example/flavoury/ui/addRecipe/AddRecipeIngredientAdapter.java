package com.example.flavoury.ui.addRecipe;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredient;
import com.example.flavoury.R;

import java.util.ArrayList;

public class AddRecipeIngredientAdapter extends RecyclerView.Adapter<AddRecipeIngredientAdapter.MyViewHolder> {
    ArrayList<Ingredient> ingredients;

    public AddRecipeIngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public AddRecipeIngredientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_recipe_ingredient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddRecipeIngredientAdapter.MyViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bindData(ingredient);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null){
            return 0;
        } else {
            return ingredients.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText editIngredient, editPortion;
        ImageButton removeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editIngredient = itemView.findViewById(R.id.add_recipe_ingredient);
            editPortion = itemView.findViewById(R.id.add_recipe_portion);
            removeBtn = itemView.findViewById(R.id.add_recipe_ingredient_remove);
        }

        void bindData(Ingredient ingredient) {
            editIngredient.setText(ingredient.getIngredient());
            editPortion.setText(ingredient.getPortion());
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
            if (getLayoutPosition() == 0) {
                removeBtn.setVisibility(View.INVISIBLE);
                removeBtn.setEnabled(false);
            }
            removeBtn.setOnClickListener(view -> {
                scaleAnim(view);
                editIngredient.setText("");
                editPortion.setText("");
                ingredients.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), ingredients.size());

            });
        }

        private void scaleAnim(View view) {
            view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    view.animate().scaleX(1).scaleY(1).setDuration(100);
                }
            });
        }
    }
}

