package com.dc.msu.maze.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dc.msu.maze.Adapters.HistoryAdapter;
import com.dc.msu.maze.BaseUtils;
import com.dc.msu.maze.Helpers.SimpleValueEventListener;
import com.dc.msu.maze.Models.HistoryModel;
import com.dc.msu.maze.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryAdapter historyAdapter;
    private List<HistoryModel> historyModels;
    TextView noHistoryTxt;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_history, container, false);
        RecyclerView recycler = view.findViewById(R.id.fr_history_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        historyModels = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyModels);
        recycler.setAdapter(historyAdapter);
        noHistoryTxt = view.findViewById(R.id.noHistoryTxt);
        loadData();
        return view;
    }

    public void loadData() {
        // checking user is logged in or not
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            BaseUtils.mineId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        // here we are getting user Quizes history from firebase
        FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).child("history").addValueEventListener(new SimpleValueEventListener() {
            @Override
            protected void onDataChanged(DataSnapshot dataSnapshot) {
                //this listener will be called first time after making request to firebase database server
                //it will automatically calls again when a change will be occurred in our firebase database, when a submit a new quiz result to database, it will be updated here automatically.
                historyModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        HistoryModel model = new HistoryModel();
                        model.setKey(snapshot.getKey());
                        model.setResult(snapshot.child("result").getValue(Integer.class));
                        model.setTotal(snapshot.child("total").getValue(Integer.class));
                        model.setDate(snapshot.child("date").getValue(String.class));
                        historyModels.add(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (historyModels.size() == 0) {
                    noHistoryTxt.setVisibility(View.VISIBLE);
                } else {
                    noHistoryTxt.setVisibility(View.GONE);
                }
                Collections.reverse(historyModels);
                historyAdapter.notifyDataSetChanged();
            }
        });
    }
}
