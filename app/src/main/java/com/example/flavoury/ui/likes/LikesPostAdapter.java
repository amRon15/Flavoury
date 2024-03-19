package com.example.flavoury.ui.likes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

public class LikesPostAdapter extends RecyclerView.Adapter<LikesPostAdapter.MyViewHolder> {

    String[] testArr;
    public LikesPostAdapter(String[] testArr){
        this.testArr = testArr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_post_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.postImageBtn.setImageResource(R.drawable.ic_launcher_foreground);
        holder.postImageBtn.setContentDescription(testArr[position]);
    }


    @Override
    public int getItemCount() {
        return testArr.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageButton postImageBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageBtn = itemView.findViewById(R.id.likes_post_list_image);
        }
    }

}
