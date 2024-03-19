package com.example.flavoury;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


import com.example.flavoury.ui.home.HomeFragment;
import com.example.flavoury.ui.likes.LikesFragment;
import com.example.flavoury.ui.myProfile.MyProfileFragment;
import com.example.flavoury.ui.search.SearchFragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flavoury.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BottomNavigationView bottomNavigationView;


    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        bottomNavigationView=findViewById(R.id.nav_view);
        //Hide Action Bar
        getSupportActionBar().hide();

        fragmentManager=getSupportFragmentManager();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                fragmentTransaction=fragmentManager.beginTransaction();
                if(id==R.id.navigation_home){
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,homeFragment);
                    fragmentTransaction.commit();
                } else if (id==R.id.navigation_search) {
                    SearchFragment searchFragment = new SearchFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,searchFragment);
                    fragmentTransaction.commit();
                }else  if(id==R.id.navigation_likes) {
                    LikesFragment likesFragment = new LikesFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,likesFragment);
                    fragmentTransaction.commit();
                }else if(id==R.id.navigation_myProfile) {
                    MyProfileFragment myprofileFragment = new MyProfileFragment();
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,myprofileFragment);
                    fragmentTransaction.commit();
                }
                return false;
            }
        });


//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_search, R.id.navigation_likes, R.id.navigation_profile)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }






}