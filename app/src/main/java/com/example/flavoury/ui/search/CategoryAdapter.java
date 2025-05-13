package com.example.flavoury.ui.search;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    String[] arrayList;
    String categoryType;
    private int selectedPosition = -1;
    private OnItemClickListener listener;


    public CategoryAdapter(String[] arrayList, String category) {
        this.arrayList = arrayList;
        this.categoryType = category;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String category = arrayList[position];
        holder.bindData(category, position);
    }

    @Override
    public int getItemCount() {
        if (arrayList == null) {
            return 0;
        } else {
            return arrayList.length;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button categoryBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryBtn = itemView.findViewById(R.id.list_category_btn);
            itemView.setOnClickListener(v->{
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        void bindData(String category, int position) {
            categoryBtn.setText(category);
            categoryBtn.setOnClickListener(v -> {
                categoryType = category;
                selectedPosition = position;
                notifyDataSetChanged();
            });
            if (selectedPosition==position){
                categoryBtn.setBackgroundResource(R.drawable.rounded_rec_category_select);
                categoryBtn.setTextColor(itemView.getResources().getColor(R.color.white));
            } else {
                categoryBtn.setBackgroundResource(R.drawable.rounded_rec_category_unselect);
                categoryBtn.setTextColor(itemView.getResources().getColor(R.color.mainText_color));
            }
        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
