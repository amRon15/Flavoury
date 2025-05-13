package com.example.flavoury.ui.detail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class DetailStepAdapter extends RecyclerView.Adapter<DetailStepAdapter.MyViewHolder> {

    ArrayList<String> steps = new ArrayList<>();

    public DetailStepAdapter(ArrayList<String> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_step, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.step.setText(String.valueOf(position+1));
        holder.detailStep.setText(steps.get(position));

    }

    @Override
    public int getItemCount() {
        if (steps== null){
            return 0;
        }else {
            return steps.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView step, detailStep;

        public MyViewHolder(View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.detail_step_stepNum);
            detailStep = itemView.findViewById(R.id.detail_step_stepDetail);
            Log.d("DetailActivityFetch", steps.size()+"");
        }
    }
}
