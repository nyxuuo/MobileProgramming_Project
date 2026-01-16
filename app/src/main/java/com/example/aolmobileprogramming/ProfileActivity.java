package com.example.aolmobileprogramming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        TextView tvMenuSettings = findViewById(R.id.tvMenuSettings);
        TextView tvMenuAboutUs = findViewById(R.id.tvMenuAboutUs);
        TextView tvMenuHelp = findViewById(R.id.tvMenuHelp);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvUsername = findViewById(R.id.tvUsername);

        Button btnLogout = findViewById(R.id.btnLogout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            fetchUserData(currentUser.getUid(), tvName, tvUsername);
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
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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

        private void fetchUserData(String userId, TextView tvName, TextView tvUsername) {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            if (username != null && !username.isEmpty()) {
                                tvName.setText(username);
                                tvUsername.setText("@" + username);
                            } else {
                                String email = documentSnapshot.getString("email");
                                tvName.setText(email != null ? email.split("@")[0] : "User");
                                tvUsername.setText(email != null ? email : "@unknown");
                            }
                        } else {
                            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                            tvName.setText("Unknown User");
                            tvUsername.setText("@unknown");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch profile data.", Toast.LENGTH_SHORT).show();
                        tvName.setText("Unknown User");
                        tvUsername.setText("@unknown");
                    });
            }
        }


