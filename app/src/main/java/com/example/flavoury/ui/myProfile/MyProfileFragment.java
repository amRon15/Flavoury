package com.example.flavoury.ui.myProfile;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flavoury.MainActivity;
import com.example.flavoury.R;
import com.example.flavoury.databinding.FragmentMyProfileBinding;
import com.example.flavoury.databinding.FragmentProfileBinding;
import com.example.flavoury.databinding.FragmentSettingBinding;
import com.example.flavoury.ui.home.HomeFragment;
import com.example.flavoury.ui.profile.ProfileFragment;
import com.google.android.material.tabs.TabLayout;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageButton sharingButton;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tabLayout = binding.tabLayout;
     /*   viewPager = binding.viewPager;*/

        viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPager2.setAdapter(viewPagerAdapter);

        sharingButton = binding.sharingButton;

        sharingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new ProfileFragment()).commit();
                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}