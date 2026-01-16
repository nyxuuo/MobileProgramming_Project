package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class NotificationActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // buat interactive btns
     //   ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnProfile = findViewById(R.id.btnProfile);
//        ImageView btnSettings = findViewById(R.id.btnSettings);

        //buat click, kl di klik btn backnya bakal tutup activitynya
    //    btnBack.setOnClickListener(v ->finish());

        //buat button profile, bakal pindah page ke profile class
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );
    }


}
