package com.example.mobileprogramming_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
            // User is signed in, fetch their data from Firestore
            fetchUserData(currentUser.getUid(), tvName, tvUsername);
        } else {
            // No user is signed in, redirect to login and close this activity
            startActivity(new Intent(this, SignIn.class));
            finish();
            return; // Stop further execution
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Placeholder listeners
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

    }

    private void fetchUserData(String userId, TextView tvName, TextView tvUsername) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Document exists, get the username
                        String username = documentSnapshot.getString("username");
                        if (username != null && !username.isEmpty()) {
                            tvName.setText(username);
                            tvUsername.setText("@" + username);
                        } else {
                            // Field is null or empty, use email as fallback
                            String email = documentSnapshot.getString("email");
                            tvName.setText(email != null ? email.split("@")[0] : "User");
                            tvUsername.setText(email != null ? email : "@unknown");
                        }
                    } else {
                        // Document doesn't exist, handle this case
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                        tvName.setText("Unknown User");
                        tvUsername.setText("@unknown");
                    }
                })
                .addOnFailureListener(e -> {
                    // Error fetching data
                    Toast.makeText(this, "Failed to fetch profile data.", Toast.LENGTH_SHORT).show();
                    tvName.setText("Unknown User");
                    tvUsername.setText("@unknown");
                });
    }
}
