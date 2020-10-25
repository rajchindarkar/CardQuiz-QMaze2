package com.dc.msu.maze;

import android.app.DatePickerDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public static Intent launch(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    private AppCompatEditText etFirstName;
    private AppCompatEditText etFamilyName;
    private AppCompatEditText etDOB;
    private AppCompatEditText etEmail;
    private AppCompatEditText etPassword;
    private AppCompatButton btnSignUp;
    private LinearLayout btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etFamilyName = findViewById(R.id.etFamilyName);
        etDOB = findViewById(R.id.etDOB);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        etDOB.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == etDOB) {
            // here we are using date picker to pick date
            showDatePicker();
        } else if (v == btnSignUp) {
            validateData();
        } else if (v == btnLogin) {
            startActivity(LoginActivity.launch(this));
            finish();
        }
    }

    private void validateData() {
        String name = etFirstName.getText().toString();
        String familyName = etFamilyName.getText().toString();
        String dob = etDOB.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_name));
        } else if (name.length() < 3) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_name_length_must_be));
        } else if (familyName.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_family_name));
        } else if (dob.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_dob));
        } else if (email.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_email_id));
        } else if (!new BaseUtils().isValidEmail(email)) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_correct_email_id));
        } else if (password.isEmpty()) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_enter_password));
        } else if (password.length() < 6) {
            CustomToast.toastLong(this, getResources().getString(R.string.msg_password_length_must_be));
        } else {
            //- store uid /password temp. as no DB is there
//            BaseUtils.Email = email;
//            BaseUtils.Password = password;
//            CustomToast.toastLong(this, getResources().getString(R.string.msg_registered_successfully));
//            startActivity(LoginActivity.launch(this));
//            finish();

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            // Registering New User Method
            // here we are registering new user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // here we are checking whether our account has been created or not. if true then we will save the user profile data to firebase database
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                        // our user account has been created, we will save user data to firebase database.
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("id", task.getResult().getUser().getUid());
                        data.put("name", name);
                        data.put("family_name", familyName);
                        data.put("birth_date", dob);
                        data.put("email", email);
                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                //after saving the data we will take the user to main screen
                                startActivity(MainActivity.launch(RegisterActivity.this));
                            }
                        });
                    } else {
                        dialog.dismiss();
                        String exception = "";
                        if (task.getException() != null && task.getException().getLocalizedMessage() != null) {
                            exception = task.getException().getLocalizedMessage();
                        }
                        CustomToast.toastLong(RegisterActivity.this, "Something went Wrong...\nError: " + exception);
                    }
                }
            });
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            //Make the date format look-like mm/dd/yy - and to make it two digit consistent in case of days and months
            String mMonth = "0";
            if ((monthOfYear + 1) < 10) {
                mMonth = mMonth + "" + (monthOfYear + 1);
            } else {
                mMonth = "" + (monthOfYear + 1);
            }
            String mDay = "0";
            if (dayOfMonth < 10) {
                mDay = mDay + "" + dayOfMonth;
            } else {
                mDay = mDay + dayOfMonth;
            }
            etDOB.setText(mMonth + "/" + mDay + "/" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - (10 * 365 *30 * 24 * 60 * 60 * 1 * 1000));
        datePickerDialog.show();
    }
}