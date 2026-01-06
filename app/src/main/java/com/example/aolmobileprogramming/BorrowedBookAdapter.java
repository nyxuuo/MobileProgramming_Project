package com.example.aolmobileprogramming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder> {

    private final List<Book> bookList = new ArrayList<>();
    private final List<Book> bookListFull = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = bookList.get(position);

        // ui
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.btnReturn.setText("Return");

        //pas mo return
        holder.btnReturn.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            BookDao.updateBook(
                    db,
                    book.getId(),
                    null, null, null, null,
                    -1,
                    book.getStock() + 1,
                    book.getBorrowed() - 1,
                    -1, -1
            );

            db.collection("logbook")
                    .whereEqualTo("bookId", book.getId())
                    .whereEqualTo("status", "BORROWED")
                    .limit(1)
                    .get()
                    .addOnSuccessListener(q -> {
                        if (!q.isEmpty()) {
                            q.getDocuments().get(0).getReference().update(
                                    "status", "RETURNED",
                                    "returnedDate", Timestamp.now()
                            );

                            // HAPUS ITEM DARI LIST (PENTING)
                            int pos = holder.getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                bookList.remove(pos);
                                notifyItemRemoved(pos);
                            }
                        }
                    });
        });

    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void filter(String keyword) {
        bookList.clear();

        if (keyword == null || keyword.trim().isEmpty()) {
            bookList.addAll(bookListFull);
        } else {
            keyword = keyword.toLowerCase();
            for (Book book : bookListFull) {
                if (book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword)) {
                    bookList.add(book);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setBooks(List<Book> books) {
        bookList.clear();
        bookListFull.clear();

        bookList.addAll(books);
        bookListFull.addAll(books);

        notifyDataSetChanged();
    }

    //set data dr firebase
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;
        Button btnReturn;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            btnReturn = itemView.findViewById(R.id.btnReturn);
        }
    }
}
