package com.abhishek.resoluteai;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScannerActivity extends AppCompatActivity
{
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    FirebaseFirestore db;

    private static final int CAMERA_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        db = FirebaseFirestore.getInstance();

        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_GRANTED)
        {
            // Camera permission is granted
            // Run the QR scanner
            runCodeScanner();
        }
        else
        {
            // Camera permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Camera permission granted
                runCodeScanner();

            } else
            {
                // Camera permission denied
                Toast.makeText(ScannerActivity.this, "Please allow camera permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScannerActivity.this ,MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void runCodeScanner()
    {
        codeScannerView = findViewById(R.id.codeScannerView);
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(new DecodeCallback() {

            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        codeScanner.stopPreview();

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(100);
                        }


                        MaterialAlertDialogBuilder ad = new MaterialAlertDialogBuilder(ScannerActivity.this);
                        ad.setTitle("Message");
                        ad.setMessage(result.getText());
                        ad.setNegativeButton("CLOSE ", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                codeScanner.startPreview();
                            }
                        });
                        ad.setPositiveButton("RECORD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addData(result.getText());
                               codeScanner.startPreview();
                            }
                        });
                       ad.show();
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(codeScanner!=null)
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {

        if(codeScanner!=null)
        codeScanner.releaseResources();
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addData(String msg)
    {


        Map<String,Object> user = new HashMap<>();

        String userId =(FirebaseAuth.getInstance().getUid()).toString();

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());


        user.put("date",date);
        user.put("time",time);
        user.put("message",msg);

        db.collection(userId)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ScannerActivity.this,"Record added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(ScannerActivity.this,"Failed",Toast.LENGTH_SHORT).show();


                    }
                });


    }
}