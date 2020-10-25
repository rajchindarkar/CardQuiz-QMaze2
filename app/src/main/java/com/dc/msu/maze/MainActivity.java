package com.dc.msu.maze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.dc.msu.maze.Fragments.HistoryFragment;
import com.dc.msu.maze.Fragments.HomeFragment;
import com.dc.msu.maze.Fragments.LogoutFragment;
import com.dc.msu.maze.Fragments.MoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static Intent launch(Context context) {
        return new Intent(context, MainActivity.class);
    }

    int current_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.ac_main_container, HomeFragment.newInstance()).commit();
        // here we are setting bottom Navigation Bar, its click listener and changing the fragments based on selected navigation button
        BottomNavigationView navigationView = findViewById(R.id.ac_main_bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_main_home:
                        if (current_index != 0) {
                            current_index = 0;
                            getSupportFragmentManager().beginTransaction().replace(R.id.ac_main_container, HomeFragment.newInstance()).commit();
                        }
                        return true;
                    case R.id.menu_main_history:
                        if (current_index != 1) {
                            current_index = 1;
                            getSupportFragmentManager().beginTransaction().replace(R.id.ac_main_container, HistoryFragment.newInstance()).commit();
                        }
                        return true;
                    case R.id.menu_main_logout:
                        if (current_index != 2) {
                            current_index = 2;
                            getSupportFragmentManager().beginTransaction().replace(R.id.ac_main_container, LogoutFragment.newInstance()).commit();
                        }
                        return true;
//                    case R.id.menu_main_more:
//                        if (current_index != 2) {
//                            current_index = 2;
//                            getSupportFragmentManager().beginTransaction().replace(R.id.ac_main_container, MoreFragment.newInstance()).commit();
//                        }
//                        return true;
                }
                return false;
            }
        });
    }
}