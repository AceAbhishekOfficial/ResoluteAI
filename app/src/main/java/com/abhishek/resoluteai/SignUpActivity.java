package com.abhishek.resoluteai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity
{
    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    TextInputEditText nameInput;
    Button buttonSignUp;

    private FirebaseAuth mAuth;

    TextView logIn;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        emailInput = (TextInputEditText) findViewById(R.id.email);
        passwordInput = (TextInputEditText) findViewById(R.id.password);
        nameInput = (TextInputEditText) findViewById(R.id.username);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        logIn = findViewById(R.id.login);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String name = nameInput.getText().toString();


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(SignUpActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name) // Set the desired username
                                            .build();
                                    user.updateProfile(profileUpdates);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    System.out.println("createUserWithEmail:failure"+task.getException());

                                    Toast.makeText(SignUpActivity.this, "Account Creation failed",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });



    }
}