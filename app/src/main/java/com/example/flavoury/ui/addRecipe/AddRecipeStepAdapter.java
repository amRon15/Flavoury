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

    boolean isRecipeReady;
    ArrayList<AddRecipeModel.Step> steps;

    public void setAddRecipeStepAdapter(ArrayList<AddRecipeModel.Step> steps, boolean isRecipeReady) {
        this.steps = steps;
        this.isRecipeReady = isRecipeReady;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_recipe_step, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AddRecipeModel.Step step = steps.get(position);
        holder.bindData(step, position);
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

        void bindData(AddRecipeModel.Step step, int position) {
            this.stepNum.setText("Step " + (position + 1));
            this.step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    step.setStep(charSequence.toString());
                    isRecipeReady = charSequence.length()!=0;
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
                    notifyDataSetChanged();
                }
            });
        }
    }
}
