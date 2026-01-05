package com.example.mobileprogramming_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        EditText etUsername = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        Button btnSignin = findViewById(R.id.btnSignin);
        TextView tvSignupHere = findViewById(R.id.tvSignupHere);

        btnSignin.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("BeeLibPrefs", MODE_PRIVATE);

            String savedUser = prefs.getString("username", "");
            String savedPass = prefs.getString("password", "");

            if (
                    etUsername.getText().toString().equals(savedUser) &&
                            etPassword.getText().toString().equals(savedPass)
            ) {
                prefs.edit().putBoolean("isLoggedIn", true).apply();

                startActivity(new Intent(SignIn.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignupHere.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });


    }
}