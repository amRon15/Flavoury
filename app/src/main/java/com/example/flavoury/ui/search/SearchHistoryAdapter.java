package com.example.flavoury.ui.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {
    ArrayList<String> myArray;
    String type;
    public SearchHistoryAdapter(ArrayList<String > myArray, String type) {
        this.myArray = myArray;
        this.type = type;
    }

    @NonNull
    @Override
    public SearchHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.MyViewHolder holder, int position) {
        String text = myArray.get(position);
        holder.bindData(text);
    }

    @Override
    public int getItemCount() {
        if (myArray == null){
            return 0;
        } else {
            return myArray.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView historyBtn;
        ImageButton deleteBtn;
        View div;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            historyBtn = itemView.findViewById(R.id.search_history_btn);
            deleteBtn = itemView.findViewById(R.id.search_history_delete);
            div = itemView.findViewById(R.id.search_history_div);
        }

        void bindData(String text){
            DatabaseHelper db = new DatabaseHelper(itemView.getContext());
            db.onCreate(db.getWritableDatabase());
            historyBtn.setText(text);

            if (getAdapterPosition() == myArray.size()-1){
                div.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(v->{
                if (type.equals("recipe")){
                    db.deleteRecipeHistory(text);
                } else {
                    db.deleteUserHistory(text);
                }
                myArray.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

            historyBtn.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(),
                        type.equals("recipe") ? SearchRecipeActivity.class : SearchUserActivity.class);
                intent.putExtra("searchText", text);
            });
        }

    }
}
