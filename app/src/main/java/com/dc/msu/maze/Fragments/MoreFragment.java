package com.dc.msu.maze.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dc.msu.maze.BaseUtils;
import com.dc.msu.maze.ChangeEmailActivity;
import com.dc.msu.maze.CustomToast;
import com.dc.msu.maze.Helpers.SimpleValueEventListener;
import com.dc.msu.maze.LoginActivity;
import com.dc.msu.maze.MainActivity;
import com.dc.msu.maze.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreFragment extends Fragment {

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fragment_more, container, false);

        final TextView nameText = layout.findViewById(R.id.fr_more_profile_name);
        final TextView emailText = layout.findViewById(R.id.fr_more_profile_email);
        final TextView familyText = layout.findViewById(R.id.fr_more_profile_family_name);
        final CircleImageView profileImage = layout.findViewById(R.id.fr_more_profile_img);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            BaseUtils.mineId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // here we are getting userDetails From Firebase Database
        FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).addValueEventListener(new SimpleValueEventListener() {
            @Override
            protected void onDataChanged(DataSnapshot dataSnapshot) {
                nameText.setText(dataSnapshot.child("name").getValue(String.class));
                familyText.setText(dataSnapshot.child("family_name").getValue(String.class));
                emailText.setText(dataSnapshot.child("email").getValue(String.class));
                try {
                    String img = dataSnapshot.child("image").getValue(String.class);
                    if (img != null) {
                        Glide.with(getContext()).load(img).into(profileImage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        layout.findViewById(R.id.fr_more_profile_img_editBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(getContext(), MoreFragment.this);
            }
        });
        
        ((CardView)nameText.getParent().getParent().getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View layout = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_dialog_update_profile_info, null, false);

                final EditText nmText = layout.findViewById(R.id.dialog_update_profile_info_text);
                nmText.setText(nameText.getText().toString());

                final ProgressBar progress = layout.findViewById(R.id.dialog_update_profile_info_progress);
                builder.setView(layout);
                final AlertDialog dialog = builder.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                }
                dialog.show();

                layout.findViewById(R.id.dialog_update_profile_info_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // here we are updating userName
                layout.findViewById(R.id.dialog_update_profile_info_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        String name = nmText.getText().toString();
                        if (name.isEmpty()) {
                            CustomToast.toastLong(v.getContext(), getResources().getString(R.string.msg_enter_name));
                            return;
                        }

                        progress.setVisibility(View.VISIBLE);

                        // here we are putting new userName in Firebase database
                        dbRef.child("users").child(BaseUtils.mineId).child("name").setValue(nmText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                } else {
                                    CustomToast.toastLong(v.getContext(), "Something went wrong while updating name");
                                }
                            }
                        });

                    }
                });
            }
        });

        ((CardView)familyText.getParent().getParent().getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View layout = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_dialog_update_profile_info, null, false);

                final EditText nmText = layout.findViewById(R.id.dialog_update_profile_info_text);
                nmText.setText(familyText.getText().toString());

                final ProgressBar progress = layout.findViewById(R.id.dialog_update_profile_info_progress);
                builder.setView(layout);
                final AlertDialog dialog = builder.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                }
                dialog.show();

                layout.findViewById(R.id.dialog_update_profile_info_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                // here we are updating userFamily Name
                layout.findViewById(R.id.dialog_update_profile_info_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        String name = nmText.getText().toString();
                        if (name.isEmpty()) {
                            CustomToast.toastLong(v.getContext(), getResources().getString(R.string.msg_enter_name));
                            return;
                        }

                        progress.setVisibility(View.VISIBLE);

                        dbRef.child("users").child(BaseUtils.mineId).child("family_name").setValue(nmText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                } else {
                                    CustomToast.toastLong(v.getContext(), "Something went wrong while updating name");
                                }
                            }
                        });

                    }
                });
            }
        });
        // here we are changing User Email
        ((CardView)emailText.getParent().getParent().getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ChangeEmailActivity.class));
            }
        });

        layout.findViewById(R.id.fr_more_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        if (getActivity() != null) {
                            startActivity(LoginActivity.launch(getActivity()));
                            getActivity().finish();
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

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                final ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Uploading image...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final StorageReference ref = FirebaseStorage.getInstance().getReference().child("UserImages/" + BaseUtils.mineId);
                ref.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri = String.valueOf(uri);
                                        FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).child("image").setValue(imageUri);
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Failed to upload Image", Toast.LENGTH_SHORT).show();;
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                dialog.setMessage("Uploading " + (int) progress + "%");
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), "Failed to retrieve Image", Toast.LENGTH_SHORT).show();;
            }
        }
    }
}
