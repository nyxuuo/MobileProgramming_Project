package com.example.mobileprogramming_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        TextView tvMenuSettings = findViewById(R.id.tvMenuSettings);
        TextView tvMenuAboutUs = findViewById(R.id.tvMenuAboutUs);
        TextView tvMenuHelp = findViewById(R.id.tvMenuHelp);

        //placeholder dulu
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "edit profile placeholder", Toast.LENGTH_SHORT).show()
        );

        //placeholder dulu
        tvMenuSettings.setOnClickListener(v ->
                Toast.makeText(this, "settings placeholder", Toast.LENGTH_SHORT).show()
        );

        //placeholder dulu
        tvMenuAboutUs.setOnClickListener(v ->
                Toast.makeText(this, "about us placeholder", Toast.LENGTH_SHORT).show()
        );

        //placeholder dulu
        tvMenuHelp.setOnClickListener(v ->
                Toast.makeText(this, "help placeholder", Toast.LENGTH_SHORT).show()
        );

    }
}
