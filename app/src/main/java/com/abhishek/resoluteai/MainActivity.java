package com.abhishek.resoluteai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    Button buttonScanQR;
    Button buttonRecords;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScanQR = findViewById(R.id.buttonScanQR);
        buttonRecords = findViewById(R.id.buttonLoadQR);
        mAuth = FirebaseAuth.getInstance();


        buttonScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ScannerActivity.class);
                startActivity(intent);
            }
        });

        buttonRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RecordsActivity.class);
                startActivity(intent);
            }
        });



        String username = mAuth.getCurrentUser().getDisplayName();

        TextView name = findViewById(R.id.name);

        name.setText("Welcome "+username);



    }
}