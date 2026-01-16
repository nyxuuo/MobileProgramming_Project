package com.example.aolmobileprogramming;

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

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    BookAdapter bookAdapter;
    BookAdapter allBookAdapter;


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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);

        db = FirebaseFirestore.getInstance();

        TextView usernameText = findViewById(R.id.usrname);

        fetchAndSetUsername(currentUser.getUid(), usernameText);

//        createDummy(db);

        BookDao.readBorrowedBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                bookAdapter.setBooks(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        BookDao.readAvailableBooks(db, new BookDao.BookCallBack() {
            @Override
            public void onSuccess(List<Book> books) {
                allBookAdapter.setBooks(books);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        //buat see more
        findViewById(R.id.btnSeeMore).setOnClickListener(v -> {
            startActivity(new Intent(this, BorrowedBookActivity.class));
        });


        //buat show all
        RecyclerView rvAllBooks = findViewById(R.id.rvAllBooks);
        rvAllBooks.setLayoutManager(new LinearLayoutManager(this));

        allBookAdapter = new BookAdapter();
        rvAllBooks.setAdapter(allBookAdapter);


        setupBottomNavigation();

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

    //bottom navigation
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
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


    //buat top (profile & notif)
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

//    // Dummy data generation
    void createDummy(FirebaseFirestore db) {
        // Fantasy
        BookDao.createBook(db, new Book("The Hobbit", "J.R.R. Tolkien", "Bilbo Baggins, a hobbit, journeys to the Lonely Mountain to reclaim a stolen treasure.", "Fantasy", "https://covers.openlibrary.org/b/isbn/9780547928227-L.jpg", 310, 12, 3, 10, 1937));
        BookDao.createBook(db, new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "A young wizard discovers his magical heritage and attends Hogwarts School of Witchcraft and Wizardry.", "Fantasy", "https://covers.openlibrary.org/b/isbn/9780590353427-L.jpg", 309, 20, 5, 10, 1997));
        BookDao.createBook(db, new Book("1984", "George Orwell", "A dystopian social science fiction novel and cautionary tale about the dangers of totalitarianism.", "Sci-Fi", "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg", 328, 8, 1, 16, 1949));
        BookDao.createBook(db, new Book("The Alchemist", "Paulo Coelho", "An Andalusian shepherd boy named Santiago travels in search of a worldly treasure.", "Adventure", "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg", 208, 15, 4, 13, 1988));

// Technology & Science
        BookDao.createBook(db, new Book("Clean Code", "Robert C. Martin", "A Handbook of Agile Software Craftsmanship.", "Education", "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg", 464, 10, 8, 18, 2008));
        BookDao.createBook(db, new Book("A Brief History of Time", "Stephen Hawking", "Explores cosmology, black holes, and the nature of the universe.", "Science", "https://covers.openlibrary.org/b/isbn/9780553380163-L.jpg", 256, 5, 0, 14, 1988));
        BookDao.createBook(db, new Book("The Pragmatic Programmer", "Andrew Hunt", "Your journey to mastery in software development.", "Education", "https://covers.openlibrary.org/b/isbn/9780201616224-L.jpg", 352, 18, 2, 18, 1999));

// Mystery & Thriller
        BookDao.createBook(db, new Book("Sherlock Holmes: The Complete Novels", "Arthur Conan Doyle", "A collection of the most famous detective stories in history.", "Mystery", "https://covers.openlibrary.org/b/isbn/9780553328257-L.jpg", 700, 6, 2, 13, 1927));
        BookDao.createBook(db, new Book("Gone Girl", "Gillian Flynn", "A thriller about a woman who disappears on her fifth wedding anniversary.", "Thriller", "https://covers.openlibrary.org/b/isbn/9780307588371-L.jpg", 422, 9, 7, 18, 2012));

// Romance & Drama
        BookDao.createBook(db, new Book("Pride and Prejudice", "Jane Austen", "A romantic novel of manners written in the early 19th century.", "Romance", "https://covers.openlibrary.org/b/isbn/9780141439518-L.jpg", 279, 11, 1, 12, 1813));
        BookDao.createBook(db, new Book("The Great Gatsby", "F. Scott Fitzgerald", "A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.", "Drama", "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg", 180, 14, 2, 14, 1925));

// Self Help
        BookDao.createBook(db, new Book("Atomic Habits", "James Clear", "An easy and proven way to build good habits and break bad ones.", "Self-Help", "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", 320, 25, 10, 15, 2018));
        BookDao.createBook(db, new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "A survey of the history of humankind from the Stone Age to the twenty-first century.", "History", "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg", 443, 7, 3, 16, 2011));
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
}


