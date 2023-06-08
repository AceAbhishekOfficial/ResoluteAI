package com.abhishek.resoluteai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity
{
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Initialize the handler
        handler = new Handler();

        // Schedule the intent after 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent and start the desired activity
                Intent intent = new Intent(SplashScreen.this, SignUpActivity.class);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        }, 2500); //
    }
}