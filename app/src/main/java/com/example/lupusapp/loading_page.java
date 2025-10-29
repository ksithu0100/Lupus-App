package com.example.lupusapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;

public class loading_page extends Activity {

    ImageButton imghomebttn, imgcalendarbttn, imgaddbttn, imgjournalbttn, imgprofilebttn;

    @ Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        imghomebttn = (ImageButton) findViewById(R.id.imghomebttn);
        imgcalendarbttn = (ImageButton) findViewById(R.id.imgcalendarbttn);
        imgaddbttn = (ImageButton) findViewById(R.id.imgaddbuttn);
        imgjournalbttn = (ImageButton) findViewById(R.id.imgjounralbttn);
        imgprofilebttn = (ImageButton) findViewById(R.id.imgprofilebttn);

        View.OnClickListener sharedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(loading_page.this, "You have clicked the button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(loading_page.this, notifications_page.class);
                startActivity(intent);
            }
        };

        imghomebttn.setOnClickListener(sharedClickListener);
        imgcalendarbttn.setOnClickListener(sharedClickListener);
        imgaddbttn.setOnClickListener(sharedClickListener);
        imgjournalbttn.setOnClickListener(sharedClickListener);
        imgprofilebttn.setOnClickListener(sharedClickListener);








}}
