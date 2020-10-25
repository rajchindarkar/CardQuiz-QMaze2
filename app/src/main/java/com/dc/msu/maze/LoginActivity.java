package com.dc.msu.maze;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent launch(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private AppCompatEditText etEmail, etPassword;
    private AppCompatButton btnLogin;
    private AppCompatTextView tvForgot;
    private LinearLayout btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgot);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            validateData();
        } else if (v == tvForgot) {
            startActivity(ForgotPasswordActivity.launch(LoginActivity.this));
        } else if (v == btnRegister) {
            startActivity(RegisterActivity.launch(this));
            finish();
        }
    }

    private void validateData() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (email.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_email_id));
        } else if (!new BaseUtils().isValidEmail(email)) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_correct_email_id));
        } else if (password.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_password));
        } else {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            // Here we are logging in
            // We will supply email and password to this method. This method will send request to firebase using inner SDK. Firebase server will check if any user auth account exists with supplied credential and then sends the result
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    dialog.dismiss();
                    //after checking accounts from firebase this code block will be executed
                    // if any account found with supplied credentials, then the task will be successful otherwise we will catch the error in else block
                    if (task.isSuccessful()) {
                        startActivity(MainActivity.launch(LoginActivity.this));
                        finish();
                    } else {
                        // user email password incorrect or email not valid
                        CustomToast.toastLong(LoginActivity.this, getResources().getString(R.string.msg_incorrect_email_password));
                    }
                }
            });
        }
    }
}
