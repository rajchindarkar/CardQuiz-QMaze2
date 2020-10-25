package com.dc.msu.maze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    public static Intent launch(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final EditText emailEdit = findViewById(R.id.ac_forgot_email_email);
        findViewById(R.id.ac_forgot_email_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailEdit.getText().toString().isEmpty()) {
                    emailEdit.setError("Enter Your email");
                    return;
                }
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setIndeterminate(true);
                dialog.show();
                // email reset Method
                mAuth.sendPasswordResetEmail(emailEdit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       dialog.dismiss();
                                                       // here we are checking email is valid or not
                                                       if (task.isSuccessful()) {
                                                           // here we are sending reset email request to user Email
                                                           Toast.makeText(getApplicationContext(), "Password reset link was sent your email address", Toast.LENGTH_SHORT).show();
                                                           finish();
                                                       } else {
                                                           Toast.makeText(ForgotPasswordActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               }
                        );
            }
        });


    }
}