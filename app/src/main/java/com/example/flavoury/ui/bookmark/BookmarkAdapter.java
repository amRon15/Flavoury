package com.example.flavoury.ui.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    public BookmarkAdapter(ArrayList<RecipeModel> recipeModelArrayList){
        this.recipeModelArrayList = recipeModelArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_profile_post,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeModel recipeModel = new RecipeModel();
        holder.bindData(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModelArrayList == null){
            return 0;
        } else{
            return recipeModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView recipeImg;
        TextView recipeName, category, cookTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        void bindData(RecipeModel recipeModel){

        }
    }
}
