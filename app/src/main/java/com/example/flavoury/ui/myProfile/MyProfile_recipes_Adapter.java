package com.example.flavoury.ui.myProfile;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;

import java.util.List;

public class MyProfile_recipes_Adapter extends RecyclerView.Adapter<MyProfile_recipes_Adapter.MyViewHolder> {
    private List<MyProfile_RecyclerView_list> myProfileRecyclerViewLists;

    public MyProfile_recipes_Adapter(List<MyProfile_RecyclerView_list> myProfileRecyclerViewLists) {
        this.myProfileRecyclerViewLists = myProfileRecyclerViewLists;
    }

    @NonNull
    @Override
    public MyProfile_recipes_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tab_recipes,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfile_recipes_Adapter.MyViewHolder holder, int position) {
        MyProfile_RecyclerView_list myProfileRecyclerViewList = myProfileRecyclerViewLists.get(position);
        holder.recipes_icon.setImageResource(myProfileRecyclerViewList.getRecipes_icon());
        holder.FoodId.setText(myProfileRecyclerViewList.getFoodId());
        holder.drcText.setText(myProfileRecyclerViewList.getDrcText());
    }

    @Override
    public int getItemCount() {
        return myProfileRecyclerViewLists.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodId,drcText;
        public ImageView recipes_icon;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            FoodId=itemView.findViewById(R.id.FoodId);
            drcText=itemView.findViewById(R.id.drcText);
            recipes_icon=itemView.findViewById(R.id.recipes_icon);
        }
    }
}
