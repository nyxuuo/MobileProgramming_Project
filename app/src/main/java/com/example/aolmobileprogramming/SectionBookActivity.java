package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

//hitung tgl
import com.google.firebase.Timestamp;
import java.util.Calendar;


public class SectionBookActivity extends AppCompatActivity {
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvStock = findViewById(R.id.tvStock);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvAge = findViewById(R.id.tvAgeRating);

        Intent intent = getIntent();
        bookId = getIntent().getStringExtra("BOOK_ID");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("books")
                .document(bookId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    Book book = doc.toObject(Book.class);
                    if (book == null) return;

                    tvTitle.setText(book.getTitle());
                    tvAuthor.setText(book.getAuthor());
                    tvDescription.setText(book.getDescription());
                    tvStock.setText("Stock: " + book.getStock());
                    tvGenre.setText("Genre: " + book.getGenre());
                    tvAge.setText(book.getAge_rating() + "+");
                });


        Button btnBorrow = findViewById(R.id.btnBorrow);

        btnBorrow.setOnClickListener(v -> {

            db.collection("books")
                    .document(bookId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (!doc.exists()) return;

                        Book book = doc.toObject(Book.class);
                        if (book == null) return;

                        if (book.getStock() <= 0) {
                            Toast.makeText(this, "Book out of stock", Toast.LENGTH_SHORT).show();
                            btnBorrow.setEnabled(false);
                            btnBorrow.setText("Out of Stock");
                            btnBorrow.setAlpha(0.6f);
                            return;
                        }

                        //updet stok buku
                        BookDao.updateBook(
                                db,
                                bookId,
                                null, null, null, null,
                                -1,
                                book.getStock() - 1,
                                book.getBorrowed() + 1,
                                -1, -1
                        );

                        //hitung date
                        Timestamp borrowedDate = Timestamp.now();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(borrowedDate.toDate());
                        cal.add(Calendar.DAY_OF_MONTH, 7);
                        Timestamp dueDate = new Timestamp(cal.getTime());

                        // create logbook
                        Map<String, Object> log = new HashMap<>();
                        log.put("bookId", bookId);
                        log.put("title", book.getTitle());
                        log.put("author", book.getAuthor());
                        log.put("borrowedDate", borrowedDate);
                        log.put("dueDate", dueDate);
                        log.put("returnedDate", null);
                        log.put("status", "BORROWED");

                        db.collection("logbook").add(log).addOnSuccessListener(r ->
                                        Toast.makeText(this, "Book borrowed!", Toast.LENGTH_SHORT).show()
                                );

                        //button nya
                        if (book.getStock() - 1 <= 0) {
                            btnBorrow.setEnabled(false);
                            btnBorrow.setText("Out of Stock");
                            btnBorrow.setAlpha(0.2f);
                        }
                    });
        });



        tvTitle.setText(intent.getStringExtra("BOOK_TITLE"));
        tvAuthor.setText(intent.getStringExtra("BOOK_AUTHOR"));
        tvDescription.setText(intent.getStringExtra("BOOK_DESC"));


        setupBottomNavigation();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // balik ke page sebelumnya
        return true;
    }


    //bottom navigation
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setSelectedItemId(R.id.nav_book_section);
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
                return true;
            }
            //belum dibuat juga
            else if (id == R.id.nav_logbook) {
                startActivity(new Intent(this, LogBookActivity.class));
                return true;
            }

            return false;
        });
    }

    //buat profile sm notif
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
