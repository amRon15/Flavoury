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

    public AddRecipeStepAdapter(ArrayList<String> steps) {
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
        holder.bindData(recipeStep);
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

        void bindData(String recipeStep) {
            this.stepNum.setText(String.valueOf(getAdapterPosition() + 1));
            this.step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    steps.set(getAdapterPosition(), charSequence.toString());
                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



            if (getAdapterPosition() == 0) {
                removeBtn.setVisibility(View.INVISIBLE);
            }

            removeBtn.setOnClickListener(view -> {
                step.setText("");
                steps.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), steps.size());
                scaleAnim(view);
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
