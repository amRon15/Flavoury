package com.example.flavoury.ui.scan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.ui.home.RecipeListAdapter;
import com.example.flavoury.ui.search.SearchRecipeActivity;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {
    ArrayList<String> ingredients = new ArrayList<>();
    public IngredientAdapter(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_scan_ingredient, parent, false);
        return new IngredientAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ingredient = ingredients.get(position);
        holder.bindData(ingredient);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null){
            return 0;
        } else {
            return ingredients.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientBtn = itemView.findViewById(R.id.scan_ingredient);
        }

        void bindData(String ingredient){
            ingredientBtn.setText(ingredient);
            ingredientBtn.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), SearchRecipeActivity.class);
                intent.putExtra("searchText", ingredientBtn.getText());
                itemView.getContext().startActivity(intent);
            });
        }
    }

}
