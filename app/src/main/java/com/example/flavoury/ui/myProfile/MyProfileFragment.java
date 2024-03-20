package com.example.flavoury.ui.myProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentLikesBinding;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.databinding.FragmentProfileBinding;
import com.example.flavoury.databinding.FragmentSettingBinding;
import com.example.flavoury.ui.likes.LikesFragment;
import com.google.android.material.tabs.TabLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    RecyclerView recyclerView_list;
    private RecyclerView MyProfileFragment;
    ImageButton sharingButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        MyProfileViewModel myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        /*View rootView = inflater.inflate(R.layout.fragment_my_profile,container,false);*/
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView_list = root.findViewById(R.id.recyclerView_list);
        MyProfile_recipes_Adapter adapter = new MyProfile_recipes_Adapter(generateMyProfile_RecyclerView_list());
        int spanCount=1;
        recyclerView_list.setLayoutManager(new GridLayoutManager(requireContext(),spanCount));
        recyclerView_list.setAdapter(adapter);



        return root;


    }



    private List<MyProfile_RecyclerView_list> generateMyProfile_RecyclerView_list() {
        List<MyProfile_RecyclerView_list> myProfileRecyclerViewLists = new ArrayList<>();
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon,"food","yummy"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.icecream,"burger","too sweet"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.juice ,"burger","good"));


        return myProfileRecyclerViewLists;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
