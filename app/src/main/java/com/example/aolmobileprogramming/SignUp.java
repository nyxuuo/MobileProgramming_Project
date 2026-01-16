package com.example.aolmobileprogramming;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView tvBirthday = findViewById(R.id.tvBirthday);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);

        EditText etEmail = findViewById(R.id.email);
        EditText etUsername = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        EditText etConfirmPassword = findViewById(R.id.confirm_password);
        Button btnSignup = findViewById(R.id.btnSignup);
        TextView tvSigninHere = findViewById(R.id.tvSigninHere);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        View.OnClickListener openCalendar = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this,
                        (view, y, m, d) -> tvBirthday.setText(String.format("%02d / %02d / %04d", d, m+1, y)),
                        year, month, day);

                dialog.show();
            }
        };

        tvBirthday.setOnClickListener(openCalendar);
        btnCalendar.setOnClickListener(openCalendar);

        btnSignup.setOnClickListener(v ->{
            String inputEmail = etUsername.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();
            String dob = tvBirthday.getText().toString();

            if (username.isEmpty() || pass.isEmpty() || inputEmail.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dob.equals("dd / mm / yyyy")) {
                Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
            }

            if (!pass.equals(confirm)) {
                etConfirmPassword.setError("Password not match");
                return;
            }

            mAuth.createUserWithEmailAndPassword(inputEmail, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", inputEmail);
                                user.put("username", username);
                                user.put("dob", dob);

                                db.collection("users").document(userId)
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(SignUp.this, "Sign up successful.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp.this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignUp.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                        });

                            }
                        } else {
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        tvSigninHere.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });

    }
}