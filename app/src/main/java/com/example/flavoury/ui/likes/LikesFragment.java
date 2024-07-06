package com.example.flavoury.ui.likes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.example.flavoury.RecipeModel;
import com.example.flavoury.databinding.FragmentLikesBinding;
import com.example.flavoury.ui.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LikesFragment extends Fragment {

    private FragmentLikesBinding binding;
    RecyclerView likesRecyclerView;
    LikesPostAdapter likesPostAdapter;
    String ipAddress, Uid;
    ArrayList<RecipeModel> recipeModels;
    DatabaseHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //layout: fragment_likes.xml
        binding = FragmentLikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DatabaseHelper(getContext());
        db.onCreate(db.getReadableDatabase());
        Uid = db.getUid();

        //10.0.2.2
        ipAddress = getResources().getString(R.string.ipAddress);


        //recycler view adapter: LikesPostAdapter
        likesRecyclerView = root.findViewById(R.id.likes_postList);


        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                return;
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);

//        likesRecyclerView = root.findViewById(R.id.likes_postList);
//        likesPostAdapter = new LikesPostAdapter();
//        likesRecyclerView.setAdapter(likesPostAdapter);
//        likesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        getLikeList();

        return root;
    }

    private void getLikeList() {
        new Thread(() -> {
            try {
                URL url = new URL(ipAddress + "app_view_like.php?Uid=" + Uid);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String jsonResponseString = response.toString().replaceAll("\\<*.?\\>", "");
                Log.d("LikesFragmentFetch", jsonResponseString);
                recipeModels = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonResponseString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    RecipeModel recipeModel = new RecipeModel();
                    recipeModel.setRecipeInList(jsonObject);
                    recipeModel.setLUid(jsonObject.getString("LUid"));
                    recipeModel.setLIconid(jsonObject.getString("LIconid"));
                    recipeModel.setLUsername(jsonObject.getString("LUsername"));
                    recipeModels.add(recipeModel);
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.d("LikesFragmentFetch", e.toString());
            } finally {
                getActivity().runOnUiThread(()->{
                    likesPostAdapter = new LikesPostAdapter(recipeModels);
                    likesRecyclerView.setAdapter(likesPostAdapter);
                    likesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                });
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLikeList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}