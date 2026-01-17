package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private BookAdapter bookAdapter;
    private BookAdapter allBookAdapter;
    private BookAdapter searchAdapter;

    private List<Book> allBooks = new ArrayList<>();

    private RecyclerView rvSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, SignIn.class));
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ===== SEARCH RESULT OVERLAY =====
        rvSearchResult = findViewById(R.id.rvSearchResult);
        rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new BookAdapter();
        rvSearchResult.setAdapter(searchAdapter);
        rvSearchResult.setVisibility(View.GONE);

        // Borrowed books (horizontal)
        RecyclerView rvBorrowed = findViewById(R.id.recyclerView);
        rvBorrowed.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        bookAdapter = new BookAdapter();
        rvBorrowed.setAdapter(bookAdapter);

        // All books
        RecyclerView rvAllBooks = findViewById(R.id.rvAllBooks);
        rvAllBooks.setLayoutManager(new LinearLayoutManager(this));
        allBookAdapter = new BookAdapter();
        rvAllBooks.setAdapter(allBookAdapter);

        TextView usernameText = findViewById(R.id.usrname);
        fetchAndSetUsername(currentUser.getUid(), usernameText);

        BookDao.readAvailableBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                allBooks.clear();
                allBooks.addAll(books);
                allBookAdapter.setBooks(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        BookDao.readBorrowedBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                bookAdapter.setBooks(books);
                allBooks.addAll(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.btnSeeMore).setOnClickListener(v ->
                startActivity(new Intent(this, BorrowedBookActivity.class))
        );

        setupBottomNavigation();
    }

    private List<Book> filterBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (Book book : allBooks) {
            if (
                    book.getTitle().toLowerCase().contains(keyword) ||
                            book.getAuthor().toLowerCase().contains(keyword) ||
                            book.getGenre().toLowerCase().contains(keyword)
            ) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        View searchView = searchItem.getActionView();
        EditText etSearch = searchView.findViewById(R.id.etMenuSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {

                if (s.length() == 0) {
                    rvSearchResult.setVisibility(View.GONE);
                    return;
                }

                List<Book> filtered = filterBooks(s.toString());
                searchAdapter.setBooks(filtered);
                rvSearchResult.setVisibility(View.VISIBLE);
            }
        });

        return true;
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_borrowed_book) {
                startActivity(new Intent(this, BorrowedBookActivity.class));
                return true;
            }
            if (id == R.id.nav_book_section) {
                startActivity(new Intent(this, SectionBookActivity.class));
                return true;
            }
            if (id == R.id.nav_logbook) {
                startActivity(new Intent(this, LogBookActivity.class));
                return true;
            }
            return false;
        });
    }

    private void fetchAndSetUsername(String userId, TextView usernameTextView) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.getString("username") != null) {
                        usernameTextView.setText(doc.getString("username"));
                    } else {
                        usernameTextView.setText("User");
                    }
                })
                .addOnFailureListener(e -> usernameTextView.setText("User"));
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