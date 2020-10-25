package com.dc.msu.maze.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dc.msu.maze.Helpers.Views.CircleTextView;
import com.dc.msu.maze.QuestionActivity;
import com.dc.msu.maze.R;
import com.dc.msu.maze.ResultActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);
        // here we are taking the user to Questions Activity from below click listener
        view.findViewById(R.id.fr_home_startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), QuestionActivity.class));
            }
        });

        return view;
    }
}
