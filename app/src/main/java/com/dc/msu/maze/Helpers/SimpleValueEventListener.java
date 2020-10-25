package com.dc.msu.maze.Helpers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public abstract class SimpleValueEventListener implements ValueEventListener {

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        onDataChanged(dataSnapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    protected abstract void onDataChanged(DataSnapshot dataSnapshot);
}
