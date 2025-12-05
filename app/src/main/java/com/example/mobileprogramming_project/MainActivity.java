package com.example.mobileprogramming_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        BookDao.createBook(db, new Book("Title", "Author", "Description", "Genre", 100, 5, 2, 18, 2023));
//        BookDao.readBooks(db);
//        BookDao.updateBook(db, "xlXCEp8tn0IJj72h3bud", "New Title", "New Author", "New Description", "New Genre", 200, 10, 5, 16, 2022);
//        BookDao.deleteBook(db, "xlXCEp8tn0IJj72h3bud");
    }
}