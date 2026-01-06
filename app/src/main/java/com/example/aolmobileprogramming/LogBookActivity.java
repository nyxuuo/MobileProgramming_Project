package com.example.aolmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;


public class LogBookActivity extends AppCompatActivity {

    RecyclerView rvLogbook;
    LogBookAdapter adapter;
    TextView tabBorrowed, tabReturned;

    List<LogBookItem> borrowedList;
    List<LogBookItem> returnedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_book);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvLogbook = findViewById(R.id.rvLogbook);
        rvLogbook.setLayoutManager(new LinearLayoutManager(this));

        tabBorrowed = findViewById(R.id.tabBorrowed);
        tabReturned = findViewById(R.id.tabReturned);

        // buat borrow sm returnnya nti
        borrowedList = new ArrayList<>();
        returnedList = new ArrayList<>();

        adapter = new LogBookAdapter(borrowedList);
        rvLogbook.setAdapter(adapter);

        //pindah2 tab
        tabBorrowed.setOnClickListener(v -> {
            Log.d("LOGBOOK", "Borrowed size = " + borrowedList.size());
            Log.d("LOGBOOK", "Returned size = " + returnedList.size());

            adapter.setData(borrowedList);
            highlightTab(true);
        });

        tabReturned.setOnClickListener(v -> {
            adapter.setData(returnedList);
            highlightTab(false);
        });


        loadLogbook();

        //buat bottom navbarny
        setupBottomNavigation();
    }

    private void loadLogbook() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("logbook")
                .orderBy("borrowedDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {

                    borrowedList.clear();
                    returnedList.clear();

                    Timestamp now = Timestamp.now();

                    for (QueryDocumentSnapshot doc : query) {
                        LogBookItem item = doc.toObject(LogBookItem.class);

                        if (item == null || item.getStatus() == null) continue;

                        String status = item.getStatus();

                        if ("RETURNED".equals(status)) {
                            returnedList.add(item);
                        }
                        else if ("BORROWED".equals(status) || "OVERDUE".equals(status)) {

                            if (
                                    item.getDueDate() != null &&
                                            "BORROWED".equals(status) &&
                                            item.getDueDate().compareTo(now) < 0
                            ) {
                                doc.getReference().update("status", "OVERDUE");
                                item.status = "OVERDUE";
                            }

                            borrowedList.add(item);
                        }
                    }

                    Log.d("LOGBOOK", "Borrowed loaded = " + borrowedList.size());
                    Log.d("LOGBOOK", "Returned loaded = " + returnedList.size());

                    adapter.setAllData(new ArrayList<>(borrowedList));
                    adapter.setData(new ArrayList<>(borrowedList));
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLogbook();
    }



    //buat berubah warna saat pindah tab
    private void highlightTab(boolean borrowed) {
        tabBorrowed.setTextColor(borrowed ? Color.parseColor("#F4BC43") : Color.GRAY);
        tabReturned.setTextColor(borrowed ? Color.GRAY : Color.parseColor("#F4BC43"));
    }

    //buat topnavbar, esp search bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        View searchView = searchItem.getActionView();

        if (searchView != null) {
            EditText etSearch = searchView.findViewById(R.id.etMenuSearch);

            if (etSearch != null) {
                etSearch.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.filter(s.toString());
                    }
                });
            }
        }
        return true;
    }


    //bottom navigation
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        bottomNav.setSelectedItemId(R.id.nav_logbook);
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

            //ini book section blm ad paggenya?
            else if (id == R.id.nav_book_section) {
                startActivity(new Intent(this, SectionBookActivity.class));
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
}


