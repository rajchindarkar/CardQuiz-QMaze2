package com.dc.msu.maze;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final EditText emailEdit = findViewById(R.id.ac_change_email_email);
        final EditText newEmailEdit = findViewById(R.id.ac_change_email_new_email);
        final EditText passwordEdit = findViewById(R.id.ac_change_email_password);

        findViewById(R.id.ac_change_email_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    finish();
                    return;
                }

                if (emailEdit.getText().toString().isEmpty()) {
                    emailEdit.setError("Enter current email");
                    return;
                }

                if (newEmailEdit.getText().toString().isEmpty()) {
                    newEmailEdit.setError("Enter new email");
                    return;
                }

                if (passwordEdit.getText().toString().isEmpty()) {
                    passwordEdit.setError("Enter password");
                    return;
                }

                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setIndeterminate(true);
                dialog.show();

                //  here we are adding new Email for user login

                AuthCredential credential = EmailAuthProvider.getCredential(emailEdit.getText().toString(), passwordEdit.getText().toString());
                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // here we are checking that task is successful or not
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().getCurrentUser().updateEmail(newEmailEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(BaseUtils.mineId).child("email").setValue(newEmailEdit.getText().toString());
                                        Toast.makeText(ChangeEmailActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        String exp = "";
                                        if (task.getException() != null) {
                                            exp = task.getException().getLocalizedMessage();
                                        }
                                        Toast.makeText(ChangeEmailActivity.this, "Failed to update...\nError: " + exp, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            dialog.dismiss();
                            String exp = "";
                            if (task.getException() != null) {
                                exp = task.getException().getLocalizedMessage();
                            }
                            Toast.makeText(ChangeEmailActivity.this, "Something went wrong...\nError: " + exp, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}