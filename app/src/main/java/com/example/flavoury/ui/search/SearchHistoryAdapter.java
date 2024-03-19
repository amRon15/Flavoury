package com.example.flavoury.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {
    String[] myArray;
    public SearchHistoryAdapter(String[] myArray) {
        this.myArray = myArray;
    }

    @NonNull
    @Override
    public SearchHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.MyViewHolder holder, int position) {
        holder.historyBtn.setText(myArray[position]);
    }

    @Override
    public int getItemCount() {
        return myArray.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button historyBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            historyBtn = itemView.findViewById(R.id.search_history_btn);
        }


    }
}
