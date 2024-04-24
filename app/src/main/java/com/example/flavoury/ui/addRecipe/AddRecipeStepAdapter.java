package com.example.flavoury.ui.addRecipe;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;
import java.util.BitSet;

public class AddRecipeStepAdapter extends RecyclerView.Adapter<AddRecipeStepAdapter.MyViewHolder> {
    ArrayList<String> steps;

    public void setAddRecipeStepAdapter(ArrayList<String> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_recipe_step, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String recipeStep = steps.get(position);
        holder.bindData(recipeStep,position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stepNum;
        EditText step;
        ImageButton removeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stepNum = itemView.findViewById(R.id.add_recipe_stepNum);
            step = itemView.findViewById(R.id.add_recipe_step);
            removeBtn = itemView.findViewById(R.id.add_recipe_step_remove);
        }

        void bindData(String recipeStep, int position) {
            this.stepNum.setText("Step " + (position + 1));
            this.step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    steps.set(position,charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            if (position == 0) {
                removeBtn.setVisibility(View.INVISIBLE);
            }

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    steps.remove(position);
                    step.setText("");
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,getItemCount());
                }
            });
        }
    }
}
