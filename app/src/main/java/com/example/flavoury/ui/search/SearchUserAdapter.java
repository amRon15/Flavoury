package com.example.flavoury.ui.search;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.UserModel;
import com.example.flavoury.ui.likes.LikesPostAdapter;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {
    ArrayList<UserModel> userModelArrayList;

    public SearchUserAdapter(ArrayList<UserModel> userModelArrayList){
        this.userModelArrayList = userModelArrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel = userModelArrayList.get(position);
        holder.bindData(userModel);
    }

    @Override
    public int getItemCount() {
        if (userModelArrayList.isEmpty()){
            return 0;
        }else {
            return userModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView userIcon;
        TextView username;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            userIcon = itemView.findViewById(R.id.list_user_icon);
            username = itemView.findViewById(R.id.list_username);
        }

        void bindData(UserModel userModel){
            username.setText(userModel.getUsername());
            setUserIcon(userModel.getIconid());

            itemView.setOnClickListener(v->{
                Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
                intent.putExtra("otherUid", userModel.getUid());
                itemView.getContext().startActivity(intent);
            });
        }

        private void setUserIcon(String iconId){
            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user").child(iconId+".jpg");
            userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                }
            });
        }
    }
}
