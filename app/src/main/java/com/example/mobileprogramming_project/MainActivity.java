package com.example.mobileprogramming_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    BookAdapter bookAdapter;

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

//        BookDao.createBook(db, new Book("Title", "Author", "Description", "Genre", 100, 5, 2, 18, 2023));
//        BookDao.readBooks(db);
//        BookDao.updateBook(db, "xlXCEp8tn0IJj72h3bud", "New Title", "New Author", "New Description", "New Genre", 200, 10, 5, 16, 2022);
//        BookDao.deleteBook(db, "xlXCEp8tn0IJj72h3bud");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(layoutManager);

        TextView usernameText = findViewById(R.id.usrname);
        fetchAndSetUsername(currentUser.getUid(), usernameText);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);

        BookDao.readBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                bookAdapter.setBooks(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error loading books", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchAndSetUsername(String userId, final TextView usernameTextView) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        if (username != null) {
                            usernameTextView.setText(username);
                        } else {
                            usernameTextView.setText("User");
                        }
                    } else {
                        usernameTextView.setText("User");
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    usernameTextView.setText("User");
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.top_nav, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        View searchView = searchItem.getActionView();

        if (searchView != null){
            EditText etSearch = searchView.findViewById(R.id.etMenuSearch);

            if (etSearch != null) {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        bookAdapter.filter(s.toString());
                    }
                });
            }
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_profile){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}