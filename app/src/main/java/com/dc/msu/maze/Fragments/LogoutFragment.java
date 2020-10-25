package com.dc.msu.maze.Fragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dc.msu.maze.LoginActivity;
import com.dc.msu.maze.R;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_logout, container, false);

        view.findViewById(R.id.fr_logout_logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout and exit from the application?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        if (getActivity() != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getActivity().finishAndRemoveTask();
                            } else {
                                getActivity().finishAffinity();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        return view;
    }
}
