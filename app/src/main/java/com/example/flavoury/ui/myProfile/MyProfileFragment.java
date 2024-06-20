package com.example.flavoury.ui.myProfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.ui.addRecipe.AddRecipeActivity;
import com.example.flavoury.ui.bookmark.BookmarkActivity;
import com.example.flavoury.ui.setting.SettingActivity;
import com.example.flavoury.ui.sqlite.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    ImageButton bookmarkBtn, settingBtn, addRecipeBtn;
    ShapeableImageView userIcon;
    TextView userName, recipeNum;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    String Uid;
    ArrayList<RecipeModel> recipeModelArrayList = new ArrayList<>();
    MyProfileRecipeAdapter myProfileRecipeAdapter;
    RecyclerView recipeRecyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        Uid = databaseHelper.getUid();

        settingBtn = root.findViewById(R.id.my_profile_setting);
        bookmarkBtn = root.findViewById(R.id.my_profile_bookmark);
        addRecipeBtn = root.findViewById(R.id.my_profile_add_recipe);
        userIcon = root.findViewById(R.id.my_profile_userIcon);
        recipeNum = root.findViewById(R.id.my_profile_recipeNum);
        recipeRecyclerView = root.findViewById(R.id.my_profile_recipe_recyclerView);

        userName = root.findViewById(R.id.my_profile_userName);

        settingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });

        bookmarkBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookmarkActivity.class);
            startActivity(intent);
        });

        addRecipeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            startActivity(intent);
        });

        getUserInfo();
        getRecipe();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);

        myProfileRecipeAdapter = new MyProfileRecipeAdapter(recipeModelArrayList);
        recipeRecyclerView.setAdapter(myProfileRecipeAdapter);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return root;
    }

    private void getUserInfo() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/profile.php?Uid=" + Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.d("UsernameFromDB", Uid);
                Log.d("UsernameFromDB", response.toString());


                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                final String Username = jsonObject.getString("Username");
                final String UserIconId = jsonObject.getString("Iconid");
                if (!UserIconId.isEmpty()){
                    setUserIcon(UserIconId);
                }

                Log.d("UsernameFromDB", jsonObject.toString());

                if (Username.isEmpty()) {
                    Log.d("canfind", Username);
                    userName.setText("Undefined");
                }

                getActivity().runOnUiThread(() -> {
                    userName.setText(Username);
                });

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d("UsernameFromDB", e.getMessage());
                getActivity().runOnUiThread(() -> {
                    userName.setText("Undefined");
                });
            }
        }).start();
    }

    private void getRecipe() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/Flavoury/app_my_user_recipe.php?Uid=" + Uid);
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
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                if (!jsonResponseString.isEmpty()){
                    for (int i = 0; i < jsonArray.length(); i ++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RecipeModel recipeModel = new RecipeModel(jsonObject);
                        recipeModelArrayList.add(recipeModel);
                    }
               }
                recipeNum.setText(String.valueOf(recipeModelArrayList.size()));
                connection.disconnect();
            } catch (Exception e) {
                Log.d("MyProfileGetRecipe", "Catch error :"+e.toString());
            }
        }).start();
    }



    private void setUserIcon(String imgId) {
        StorageReference userRef = storageRef.child("user").child(imgId + ".jpg");
        userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
