package com.example.forager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        // pressing back button would send us back to splash page, finish() prevents it
        finish();
    }
}
