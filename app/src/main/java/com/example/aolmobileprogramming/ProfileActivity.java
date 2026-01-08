package com.example.aolmobileprogramming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        TextView tvMenuSettings = findViewById(R.id.tvMenuSettings);
        TextView tvMenuAboutUs = findViewById(R.id.tvMenuAboutUs);
        TextView tvMenuHelp = findViewById(R.id.tvMenuHelp);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvUsername = findViewById(R.id.tvUsername);

        Button btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs =
                getSharedPreferences("BeeLibPrefs", MODE_PRIVATE);

        String username = prefs.getString("username", null);

        if (username != null) {
            tvName.setText(username);
            tvUsername.setText("@" + username);
        } else {
            tvName.setText("Unknown User");
            tvUsername.setText("@unknown");
        }

        // placeholder
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "edit profile placeholder", Toast.LENGTH_SHORT).show()
        );

        tvMenuSettings.setOnClickListener(v ->
                Toast.makeText(this, "settings placeholder", Toast.LENGTH_SHORT).show()
        );

        tvMenuAboutUs.setOnClickListener(v ->
                Toast.makeText(this, "about us placeholder", Toast.LENGTH_SHORT).show()
        );

        tvMenuHelp.setOnClickListener(v ->
                Toast.makeText(this, "help placeholder", Toast.LENGTH_SHORT).show()
        );

        //listener logout
        btnLogout.setOnClickListener(v -> {

            prefs.edit()
                    .putBoolean("isLoggedIn", false)
                    .remove("username")
                    .apply();

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, SignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });



        setupBottomNavigation();
    }

    // BOTTOM NAVIGATION
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
