package com.example.mobileprogramming_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//nnati ku fix lgi
public class BorrowedBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_book);

        //header
        ImageView btnNotif = findViewById(R.id.btnNotif);
        ImageView btnProfile = findViewById(R.id.btnProfile);


        //ini comment dlu soalny beda branch, yg notif sama yg borrow book. biar ga merah2 w comment dl

        //buat placeholder notif, ni nanti pas di push baru bisa connect ke notif
//        btnNotif.setOnClickListener(v ->
//                startActivity(new Intent(this, NotificationActivity.class))
//        );

        //buat placeholder profile
//        btnProfile.setOnClickListener(v ->
//                startActivity(new Intent(this, ProfileActivity.class))
//        );

        // recyclerview bukunya
        RecyclerView rvBorrowedBooks = findViewById(R.id.rvBorrowedBooks);

        // bikin grid 2 kolom
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        rvBorrowedBooks.setLayoutManager(gridLayoutManager);

        // pasang adapter (dummy)
        BorrowedBookAdapter adapter = new BorrowedBookAdapter();
        rvBorrowedBooks.setAdapter(adapter);
    }
}
