package com.dc.msu.maze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ResultActivity extends AppCompatActivity {
    TextView progressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        progressText = findViewById(R.id.progressText);
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        int result = getIntent().getIntExtra("result", -1);
        int total = getIntent().getIntExtra("total", -1);
        // here we are getting results and showing in progressText
        if (result != -1 && total != -1) {
            progressText.setText(result + "/" + total);
        }

        findViewById(R.id.finishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}