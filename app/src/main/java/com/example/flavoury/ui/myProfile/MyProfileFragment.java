package com.example.flavoury.ui.myProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
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
import com.example.flavoury.ui.Setting.SettingActivity;

import com.example.flavoury.ui.likes.LikesFragment;
import com.example.flavoury.ui.profile.ProfileActivity;
import com.google.android.material.tabs.TabLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.fragment.app.FragmentManager;
public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    RecyclerView recyclerView_list;
    private RecyclerView MyProfileFragment;
    ToggleButton toggleButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        MyProfileViewModel myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        /*View rootView = inflater.inflate(R.layout.fragment_my_profile,container,false);*/
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView_list = root.findViewById(R.id.recyclerView_list);
        MyProfile_recipes_Adapter adapter = new MyProfile_recipes_Adapter(generateMyProfile_RecyclerView_list());
        int spanCount = 2;
        recyclerView_list.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
        recyclerView_list.setAdapter(adapter);


        ImageButton icon_setting = root.findViewById(R.id.icon_setting);
        ImageButton sharingButton = root.findViewById(R.id.sharingButton);


        icon_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        sharingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return root;




    }




    private List<MyProfile_RecyclerView_list> generateMyProfile_RecyclerView_list() {
        List<MyProfile_RecyclerView_list> myProfileRecyclerViewLists = new ArrayList<>();
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon,"food","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon,"burger","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon ,"burger","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon ,"burger","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon ,"burger","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon ,"burger","food • >60mins"));
        myProfileRecyclerViewLists.add(new MyProfile_RecyclerView_list(R.drawable.burgericon ,"burger","food • >60mins"));


        return myProfileRecyclerViewLists;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }





}
