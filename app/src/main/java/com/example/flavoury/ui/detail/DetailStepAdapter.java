package com.example.flavoury.ui.detail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class DetailStepAdapter extends RecyclerView.Adapter<DetailStepAdapter.MyViewHolder> {

    String[] step;
    public DetailStepAdapter(String[] step){
        this.step = step;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_step,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.step.setText("Step"+(position+1));
        holder.detailStep.setText(step[position]);
    }

    @Override
    public int getItemCount() {
        return step.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView step,detailStep;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.detail_stepnumber);
            detailStep = itemView.findViewById(R.id.detail_detailofstep);
        }
    }
}
