package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SectionBookActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private SectionBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_book);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recycler
        RecyclerView rvAllBooks = findViewById(R.id.rvAllBooks);
        rvAllBooks.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SectionBookAdapter(this);
        rvAllBooks.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // load ALL AVAILABLE BOOKS
        BookDao.readAvailableBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                adapter.setBooks(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        setupBottomNavigation();
    }

    // top navbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav, menu);
        return true;
    }

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

    //bottom nav
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
            else if (id == R.id.nav_logbook) {
                startActivity(new Intent(this, LogBookActivity.class));
                return true;
            }

            return false;
        });
    }
}
