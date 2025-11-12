package com.example.lupusapp;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends MainActivity {

    EditText username;
    EditText password;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
                    Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent(LoginPageActivity.this, HomePageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginPageActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
