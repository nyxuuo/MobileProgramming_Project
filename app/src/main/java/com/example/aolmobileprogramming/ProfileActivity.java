package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        setupBottomNavigation();
    }

    //bottom navigation
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            else if (id == R.id.nav_borrowed_book) {
                startActivity(new Intent(this, BorrowedBookActivity.class));
                return true;
            }


            else if (id == R.id.nav_book_section) {
                startActivity(new Intent(this, SectionBookActivity.class));
                return true;
            }

            else if (id == R.id.nav_logbook) {
                startActivity(new Intent(this, LogBookActivity.class));
                return true;
            }

            return false;
        });
    }
}
