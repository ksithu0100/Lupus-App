package com.example.lupusapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button loginButton;
    TextView registerButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            handleLoginRedirect();
        }

        //Sends users to register activity
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //Sign in logic with Firebase (Working!!!!)
        loginButton.setOnClickListener(view -> {
            String email = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        handleLoginRedirect();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(LoginActivity.this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }

    //Supposed to check if user has already completed first time onboarding
    private void handleLoginRedirect() {
        boolean done = getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("onboardingComplete", false);

        if (done) {
            goToMain();
        } else {
            goToFirstLogin();
        }
    }

    //Sends user to first time onboarding
    private void goToFirstLogin() {
        Intent intent = new Intent(LoginActivity.this, FirstLoginActivity.class);
        startActivity(intent);
        finish();
    }

    //Sends user to home page
    private void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
