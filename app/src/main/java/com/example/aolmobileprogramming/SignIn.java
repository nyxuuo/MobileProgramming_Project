package com.example.aolmobileprogramming;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText etUsername = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        Button btnSignin = findViewById(R.id.btnSignin);
        TextView tvSignupHere = findViewById(R.id.tvSignupHere);

        mAuth = FirebaseAuth.getInstance();

        btnSignin.setOnClickListener(v -> {
            String inputUsername = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            if (inputUsername.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(inputUsername, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignIn.this, "Invalid username or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        tvSignupHere.setOnClickListener(v -> {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }
    }
}