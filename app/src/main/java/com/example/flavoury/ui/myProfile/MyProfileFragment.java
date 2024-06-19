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

import com.example.flavoury.R;
import com.example.flavoury.UserProfileModel;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    ImageButton bookmarkBtn, settingBtn, addRecipeBtn;
    ShapeableImageView userIcon;
    TextView userName;
    StorageReference storageRefUser = FirebaseStorage.getInstance().getReference().child("user");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settingBtn = root.findViewById(R.id.my_profile_setting);
        bookmarkBtn = root.findViewById(R.id.my_profile_bookmark);
        addRecipeBtn = root.findViewById(R.id.my_profile_add_recipe);
        userIcon = root.findViewById(R.id.my_profile_userIcon);

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

        getUInfo();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(),onBackPressedCallback);

//        myProfileRecipeRecyclerView = root.findViewById(R.id.my_profile_recipe_recyclerView);
//        myProfileRecipeAdapter = new MyProfileRecipeAdapter();
//        myProfileRecipeRecyclerView.setAdapter(myProfileRecipeAdapter);
//        myProfileRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return root;
    }

    private void getUInfo() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String Uid = databaseHelper.getUid();
        Log.v("LoginSaveUid", "UID: " + Uid);


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
                Log.d("UsernameFromDB", Uid );
                Log.d("UsernameFromDB", response.toString());


                String jsonResponseString = response.toString().replaceAll("\\<.*?\\>", "");
                JSONObject jsonObject = new JSONObject(jsonResponseString);
                final String Username = jsonObject.getString("Username");
                final String UserIconId = jsonObject.getString("Iconid");
                setUserIcon(UserIconId);

                Log.d("UsernameFromDB", jsonObject.toString());

                if (Username.isEmpty()){
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

    private void setView(View root, UserProfileModel userData) {
        TextView recipeNum = root.findViewById(R.id.my_profile_recipeNum);
        TextView followingNum = root.findViewById(R.id.my_profile_followingNum);
        TextView followerNum = root.findViewById(R.id.my_profile_followerNum);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().scaleX(1).scaleY(1).setDuration(100);
                    }
                });
                Intent intent = new Intent(requireActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        TextView userName = root.findViewById(R.id.my_profile_userName);
        userName.setText(userData.getUserName());
        recipeNum.setText(userData.getRecipeNum()+"");

    }

    private void setUserIcon(String imgId){
        StorageReference imgRef = storageRefUser.child(imgId+".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).centerCrop().fit().into(userIcon);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to load img", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
