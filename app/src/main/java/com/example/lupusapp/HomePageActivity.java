package com.example.lupusapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomePageActivity extends Activity {

    private ImageButton imgHomeBtn, imgCalendarBtn, imgAddBtn, imgJournalBtn, imgProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

//        imgHomeBtn = findViewById(R.id.imgHomeBtn);
//        imgCalendarBtn = findViewById(R.id.imgCalendarBtn);
//        imgAddBtn = findViewById(R.id.imgAddBtn);
//        imgJournalBtn = findViewById(R.id.imgJournalBtn);
//        imgProfileBtn = findViewById(R.id.imgProfileBtn);

        View.OnClickListener sharedClickListener = view -> {
            Toast.makeText(this, "You clicked a button", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NotificationsPageActivity.class));
        };

//        imgHomeBtn.setOnClickListener(sharedClickListener);
//        imgCalendarBtn.setOnClickListener(sharedClickListener);
//        imgAddBtn.setOnClickListener(sharedClickListener);
//        imgJournalBtn.setOnClickListener(sharedClickListener);
//        imgProfileBtn.setOnClickListener(sharedClickListener);
    }
}
