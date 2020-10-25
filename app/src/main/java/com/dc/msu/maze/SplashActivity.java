package com.dc.msu.maze;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initViews();
        startNextActivity(this);
    }

    private void initViews() {
        try {
            AppCompatTextView tvVersion = findViewById(R.id.tvVersion);
            String versionName = "Version" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText(versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startNextActivity(final Context context) {
        new Handler().postDelayed(() -> runOnUiThread(() -> {
            // Here we are checking that user is Already login or not
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // User is Already login
                BaseUtils.mineId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                startActivity(MainActivity.launch(context));
            } else {
                // User Not Login
                startActivity(LoginActivity.launch(context));
            }
            finish();
        }), 1000);
    }
}