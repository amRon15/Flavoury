package com.example.flavoury.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.ui.detail.DetailActivity;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FollowingFragment extends Fragment {
    RecipeModel recipe;
    ShapeableImageView userIcon, recipeImg;
    ToggleButton likeBtn;
    TextView description, userName, recipeName, category, cookTime, likeNum;
    ShimmerFrameLayout shimmerUserIcon, shimmerRecipeImg;
    String ipAddress, myUserId;
    boolean isUserLiked;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseHelper db;
    public FollowingFragment(RecipeModel recipe){
        this.recipe = recipe;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_follow_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getContext());
        db.onCreate(db.getReadableDatabase());
        myUserId = db.getUid();

        ipAddress = getResources().getString(R.string.ipAddress);

        userIcon = view.findViewById(R.id.follow_post_userIcon);
        userName = view.findViewById(R.id.follow_post_username);
        recipeImg = view.findViewById(R.id.follow_post_recipeImg);
        recipeName = view.findViewById(R.id.follow_post_recipeName);
        description = view.findViewById(R.id.follow_post_description);
        category = view.findViewById(R.id.follow_post_category);
        cookTime = view.findViewById(R.id.follow_post_minutes);
        likeNum = view.findViewById(R.id.follow_post_likeNum);
        likeBtn = view.findViewById(R.id.follow_post_likeBtn);
        shimmerRecipeImg = view.findViewById(R.id.follow_post_shimmer_recipeImg);
        shimmerUserIcon = view.findViewById(R.id.follow_post_shimmer_userIcon);

        recipeName.setText(recipe.getRName());
        userName.setText(recipe.getUsername());
        description.setText(recipe.getDescription());
        category.setText(recipe.getCategory());
        cookTime.setText(recipe.getCookTime());
        likeNum.setText(String.valueOf(recipe.getLikes()));

        LikedCc();

        userIcon.setOnClickListener(v->{
            intentToProfile();
        });

        userName.setOnClickListener(v->{
            intentToProfile();
        });

        recipeImg.setOnClickListener(v->{
            intentToRecipe();
        });

        recipeName.setOnClickListener(v->{
            intentToRecipe();
        });

        likeBtn.setOnClickListener(v->{
            if (likeBtn.isChecked()) {
                likeRecipe();
            } else {
                cancelLike();
            }
        });

        setImg();
    }

    private void intentToRecipe(){
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("Recipe", (Serializable) recipe);
        startActivity(intent);
    }

    private void intentToProfile(){
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("otherUid", recipe.getUid());
        startActivity(intent);
    }

    private void setImg(){
        StorageReference recipeRef = storageRef.child("recipe").child(recipe.getImgid()+".jpg");
        StorageReference userRef = storageRef.child("user").child(recipe.getIconid()+".jpg");

        recipeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(recipeImg);
                shimmerRecipeImg.stopShimmer();
                shimmerRecipeImg.setVisibility(View.GONE);
            }
        });
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
                shimmerUserIcon.stopShimmer();
                shimmerUserIcon.setVisibility(View.GONE);
            }
        });
    }


    private void likeRecipe() {
        Thread addRecipeThread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress + "app_like_recipe.php");

                String recipeParam = "Uid=" + URLEncoder.encode(myUserId, "UTF-8") +
                        "&Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(recipeParam);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    Log.d("Like", "HTTP OK");
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("Like", jsonResponseString);
                    getActivity().runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Log.d("Like", jsonObject.toString());
                            if (status.equals("success")) {
                                Toast.makeText(getContext(), "Liked!", Toast.LENGTH_LONG).show();
                                recipe.setLikes(recipe.getLikes()+1);
                                likeNum.setText(String.valueOf(recipe.getLikes()));
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Like", "JSON Error: " + e.getMessage());
                        }
                    });
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Like", "Exception: " + e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        addRecipeThread.start();
    }

    private void cancelLike() {
        Thread addRecipeThread = new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(ipAddress + "app_cancel_like_recipe.php");

                String recipeParam = "Uid=" + URLEncoder.encode(myUserId, "UTF-8") +
                        "&Rid=" + URLEncoder.encode(recipe.getRid(), "UTF-8");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(recipeParam);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder response = new StringBuilder();
                    String line;
                    Log.d("CancelLike", "HTTP OK");
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();

                    String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                    Log.d("CancelLike", jsonResponseString);
                    getActivity().runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponseString);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Log.d("CancelLike", jsonObject.toString());
                            if (status.equals("success")) {
                                Toast.makeText(getContext(), "unliked", Toast.LENGTH_LONG).show();
                                recipe.setLikes(recipe.getLikes()-1);
                                likeNum.setText(String.valueOf(recipe.getLikes()));
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("CancelLike", "JSON Error: " + e.getMessage());
                        }
                    });
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "HTTP Error: " + responseCode, Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("CancelLike", "Exception: " + e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        addRecipeThread.start();
    }

    private void LikedCc(){
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress + "app_liked_cc.php?Uid=" + myUserId + "&Rid=" + recipe.getRid());
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
                Log.d("LikeChecking", jsonResponseString);

                isUserLiked = !jsonResponseString.equals("null");
            } catch (Exception e) {
                Log.d("LikeChecking", e.toString());
            } finally {
                getActivity().runOnUiThread(() -> {
                    likeBtn.setChecked(isUserLiked);
                });
            }
        }).start();
    }

}
