package com.example.lupusapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class FirstLoginFragment extends AppCompatActivity {

    CheckBox trackSymptoms, connectDoctors, learnLupus, other;
    CheckBox symptom1, symptom2, symptom3, symptom4;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        trackSymptoms = findViewById(R.id.checkbox_track_symptoms);
        connectDoctors = findViewById(R.id.checkbox_connect_doctors);
        learnLupus = findViewById(R.id.checkbox_learn_lupus);
        other = findViewById(R.id.checkbox_other);

        symptom1 = findViewById(R.id.symptom1);
        symptom2 = findViewById(R.id.symptom2);
        symptom3 = findViewById(R.id.symptom3);
        symptom4 = findViewById(R.id.symptom4);

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(v -> {

            getSharedPreferences("prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("onboardingComplete", true)
                    .apply();

            goToMain();
        });
    }

    private void goToMain() {
        Intent intent = new Intent(FirstLoginFragment.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
