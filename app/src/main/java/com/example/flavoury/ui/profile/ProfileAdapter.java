package com.example.flavoury.ui.profile;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flavoury.R;
import com.example.flavoury.ui.myProfile.MyProfile_recipes_Adapter;
import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private List<Profileitems> profileitemlists;

    public ProfileAdapter(List<Profileitems> profileitemlists) {
        this.profileitemlists=profileitemlists;
    }

    @NonNull
    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.MyViewHolder holder, int position) {
        Profileitems profileitems = profileitemlists.get(position);

        holder.foodtext.setText(profileitems.getFood());
        holder.fooddrc.setText(profileitems.getText());
        holder.fooditem.setImageResource(profileitems.getIcon());
    }

    @Override
    public int getItemCount() {
        return profileitemlists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView foodtext;
        public TextView fooddrc;
        public ImageView fooditem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodtext=itemView.findViewById(R.id.foodtext);
            fooddrc=itemView.findViewById(R.id.fooddrc);
            fooditem=itemView.findViewById(R.id.fooditem);
        }

    }
}
