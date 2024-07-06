package com.example.flavoury.ui.search;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.UserModel;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {
    ArrayList<UserModel> userModelArrayList;
    String Uid;

    public SearchUserAdapter(ArrayList<UserModel> userModelArrayList, String Uid) {
        this.userModelArrayList = userModelArrayList;
        this.Uid = Uid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel = userModelArrayList.get(position);
        holder.bindData(userModel);
    }

    @Override
    public int getItemCount() {
        if (userModelArrayList==null){
            return 0;
        } else {
            return userModelArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView userIcon;
        TextView username;
        ToggleButton followBtn;
        boolean isUserFollowed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.list_search_user_icon);
            username = itemView.findViewById(R.id.list_search_user_name);
            followBtn = itemView.findViewById(R.id.list_search_user_followBtn);
        }

        void bindData(UserModel userModel) {
            username.setText(userModel.getUsername());
            setUserIcon(userModel.getIconid());
            isUserFollowed(userModel.getUid());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), ProfileActivity.class);
                intent.putExtra("otherUid", userModel.getUid());
                itemView.getContext().startActivity(intent);
            });

            followBtn.setOnClickListener(v -> {
                followUser(userModel.getUid(), followBtn.isChecked());
            });
        }

        private void setUserIcon(String iconId) {
            StorageReference userRef = FirebaseStorage.getInstance().getReference().child("user").child(iconId + ".jpg");
            userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                }
            });
        }

        private void isUserFollowed(String otherUid) {
            new Thread(() -> {
                try {
                    URL url = new URL("http://10.0.2.2/Flavoury/app_is_user_followed.php?Uid=" + Uid + "&Followid=" + otherUid);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("MyProfileGetRecipe", jsonResponseString + followBtn);

                    isUserFollowed = !jsonResponseString.equals("null");

                    followBtn.setChecked(isUserFollowed);


                } catch (Exception e) {
                    Log.d("MyProfileGetRecipe", "Catch error :" + e.toString());
                }
            }).start();
        }

        private void followUser(String otherUid, boolean isUserFollowed) {
            new Thread(() -> {
                HttpURLConnection connection = null;
                try {
                    URL url;
                    if (!isUserFollowed) {
                        url = new URL("http://10.0.2.2/Flavoury/app_follow_user.php");
                    } else {
                        url = new URL("http://10.0.2.2/Flavoury/app_delete_follow.php");
                    }
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    String followUserParams = "Uid=" + URLEncoder.encode(Uid, "UTF-8") +
                            "&Followid=" + URLEncoder.encode(otherUid, "UTF-8");

                    OutputStream outputStream = connection.getOutputStream();

                    outputStream.write(followUserParams.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        Log.d("ProfileFetchData", "HTTP OK");
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                        try {
                            JSONObject jsonResponse = new JSONObject(jsonResponseString);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if (status.equals("success")) {
                                Log.d("ProfileFetchData", message);
                            } else {
                                Log.d("ProfileFetchData", message);
                            }
                        } catch (JSONException e) {
                            Log.d("ProfileFetchData", e.toString());
                        }

                    }
                } catch (Exception e) {
                    Log.d("ProfileFetchData", e.toString());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }).start();
        }

    }
}
