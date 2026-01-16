package com.example.aolmobileprogramming;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookDetailActivity extends AppCompatActivity {

    private String bookId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());


        bookId = getIntent().getStringExtra("BOOK_ID");
        db = FirebaseFirestore.getInstance();

//        buat cover
        ImageView imgCover = findViewById(R.id.imgCover);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvStock = findViewById(R.id.tvStock);
        TextView tvAge = findViewById(R.id.tvAge);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnBorrow = findViewById(R.id.btnBorrow);


        db.collection("books").document(bookId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    Book book = doc.toObject(Book.class);
                    if (book == null) return;

                    tvTitle.setText(book.getTitle());
                    tvAuthor.setText(book.getAuthor());
                    tvGenre.setText("Genre: " + book.getGenre());
                    tvStock.setText("Stock: " + book.getStock());
                    tvAge.setText(book.getAge_rating() + "+");

                    tvDescription.setText(book.getDescription());

                    //buat add cover
                    Glide.with(this).load(book.getImage_url()).placeholder(R.drawable.ic_placeholder_book).into(imgCover);

                    btnBorrow.setOnClickListener(v -> borrowBook(book));
                });
    }

    private void borrowBook(Book book) {
        if (book.getStock() <= 0) {
            Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT).show();
            return;
        }

        // update book stock
        BookDao.updateBook(
                db,
                bookId,
                null, null, null, null,
                -1,
                book.getStock() - 1,
                book.getBorrowed() + 1,
                -1, -1
        );

        // logbook
        Timestamp borrowedDate = Timestamp.now();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);

        Map<String, Object> log = new HashMap<>();
        log.put("bookId", bookId);
        log.put("title", book.getTitle());
        log.put("author", book.getAuthor());
        log.put("borrowedDate", borrowedDate);
        log.put("dueDate", new Timestamp(cal.getTime()));
        log.put("returnedDate", null);
        log.put("status", "BORROWED");

        db.collection("logbook").add(log);

        Toast.makeText(this, "Book borrowed!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
