package com.example.mobileprogramming_project;

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

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        TextView tvBirthday = findViewById(R.id.tvBirthday);
        ImageView btnCalendar = findViewById(R.id.btnCalendar);

        EditText etUsername = findViewById(R.id.username);
        EditText etPassword = findViewById(R.id.password);
        EditText etConfirmPassword = findViewById(R.id.confirm_password);
        Button btnSignup = findViewById(R.id.btnSignup);
        TextView tvSigninHere = findViewById(R.id.tvSigninHere);

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
            String inputUsername = etUsername.getText().toString().trim();

            String pass = etPassword.getText().toString();
            String confirm = etConfirmPassword.getText().toString();

            if (inputUsername.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                etConfirmPassword.setError("Password not match");
                return;
            }

            SharedPreferences prefs = getSharedPreferences("BeeLibPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("username", inputUsername)
                    .putString("password", pass)
                    .putBoolean("isLoggedIn", true)
                    .apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        tvSigninHere.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        });

    }
}