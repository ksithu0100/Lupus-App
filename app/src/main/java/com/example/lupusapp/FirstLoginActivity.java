package com.example.lupusapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FirstLoginActivity extends AppCompatActivity {

    //Checkboxes for preferences, first line is the "What do you want to use this app for", second is the symptoms they wanna track, third is the submit button
    CheckBox trackSymptoms, connectDoctors, learnLupus, other;
    CheckBox symptom1, symptom2, symptom3, symptom4, symptom5, symptom6, symptom7;
    Button submit;

    CheckBox[] symptomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first_login);

        trackSymptoms = findViewById(R.id.checkbox_track_symptoms);
        connectDoctors = findViewById(R.id.checkbox_connect_doctors);
        learnLupus = findViewById(R.id.checkbox_learn_lupus);
        other = findViewById(R.id.checkbox_other);
        symptom1 = findViewById(R.id.symptom1);
        symptom2 = findViewById(R.id.symptom2);
        symptom3 = findViewById(R.id.symptom3);
        symptom4 = findViewById(R.id.symptom4);
        symptom5 = findViewById(R.id.symptom5);
        symptom6 = findViewById(R.id.symptom6);
        symptom7 = findViewById(R.id.symptom7);
        submit = findViewById(R.id.submit);
        symptomList = new CheckBox[]{symptom1, symptom2, symptom3, symptom4, symptom5, symptom6, symptom7};

        //Logic to cap selection of symptoms at 5
        for (CheckBox cb : symptomList) {
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && getSelectedCount() > 5) {
                    buttonView.setChecked(false);
                    Toast.makeText(this, "You can select up to 5 symptoms.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        submit.setOnClickListener(v -> {
            PreferencesManager.setOnboardingComplete(this, true);
            saveSymptoms();
            goToMain();
        });
    }

    private int getSelectedCount() {
        int count = 0;
        for (CheckBox cb : symptomList) {
            if (cb.isChecked()) count++;
        }
        return count;
    }

    private void saveSymptoms() {
        PreferencesManager.saveSymptoms(this, symptom1.isChecked(), symptom2.isChecked(), symptom3.isChecked(), symptom4.isChecked(), symptom5.isChecked(), symptom6.isChecked(), symptom7.isChecked());
    }

    private void goToMain() {
        Intent intent = new Intent(FirstLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
