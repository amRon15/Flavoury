package com.example.flavoury.ui.detail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.Ingredients;
import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;

import java.util.ArrayList;
import java.util.Map;

public class DetailStepAdapter extends RecyclerView.Adapter<DetailStepAdapter.MyViewHolder> {

    ArrayList<String> steps;
    int stepSize;


    public void setDetailStepAdapter(ArrayList<String> steps,int stepSize) {
        this.steps = steps;
        this.stepSize = stepSize;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_step, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.step.setText("Step "+(position+1));
        holder.detailStep.setText(steps.get(position));

    }

    @Override
    public int getItemCount() {
        return stepSize;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView step, detailStep;

        public MyViewHolder(View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.detail_step_stepNum);
            detailStep = itemView.findViewById(R.id.detail_step_stepDetail);
        }
    }
}
