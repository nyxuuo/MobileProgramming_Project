package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;


//nnati ku fix lgi
public class BorrowedBookActivity extends AppCompatActivity {


    BorrowedBookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvBorrowedBooks = findViewById(R.id.rvBorrowedBooks);
        rvBorrowedBooks.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new BorrowedBookAdapter();
        rvBorrowedBooks.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("books")
                .whereGreaterThan("borrowed", 0)
                .get()
                .addOnSuccessListener(query -> {
                    List<Book> borrowedBooks = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : query) {
                        Book book = doc.toObject(Book.class);
                        book.setId(doc.getId());
                        borrowedBooks.add(book);
                    }

                    adapter.setBooks(borrowedBooks);
                });


        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("books")
                .whereGreaterThan("borrowed", 0)
                .get()
                .addOnSuccessListener(query -> {
                    List<Book> borrowedBooks = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : query) {
                        Book book = doc.toObject(Book.class);
                        book.setId(doc.getId());
                        borrowedBooks.add(book);
                    }

                    adapter.setBooks(borrowedBooks);
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView(); // optional

        View searchView = searchItem.getActionView();
        EditText etSearch = searchView.findViewById(R.id.etMenuSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });

        return true;
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setSelectedItemId(R.id.nav_borrowed_book);
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

    //top navbar
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
