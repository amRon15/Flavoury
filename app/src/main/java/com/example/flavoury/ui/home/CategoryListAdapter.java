package com.example.flavoury.ui.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>{

    String[] categoryType;
    int selectIndex = 0;

    public CategoryListAdapter(String[] categoryType) {
        this.categoryType = categoryType;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_category_btn,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position==5){
            holder.button.setText("Other");
        }else {
            holder.button.setText(categoryType[position]);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = holder.getAdapterPosition();
                notifyItemRangeChanged(0, categoryType.length);
                view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
            }
        });
        if(selectIndex==position){
            holder.button.setBackgroundResource(R.drawable.home_category_select);
        }else{
            holder.button.setBackgroundResource(R.drawable.home_category_unselect);
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.home_category_btn);
        }

    }
}
