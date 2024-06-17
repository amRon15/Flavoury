package com.example.flavoury.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {
    ArrayList<String> myArray;
    public SearchHistoryAdapter(ArrayList<String > myArray) {
        this.myArray = myArray;
    }

    @NonNull
    @Override
    public SearchHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.MyViewHolder holder, int position) {
//        holder.historyBtn.setText(myArray[position]);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView historyBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            historyBtn = itemView.findViewById(R.id.search_history_btn);
        }


    }
}
