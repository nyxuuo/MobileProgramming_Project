package com.example.mobileprogramming_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final List<Book> bookList = new ArrayList<>();
    private final List<Book> bookListFull = new ArrayList<>();

    public void setBooks(List<Book> books) {
        bookList.clear();
        bookList.addAll(books);

        bookListFull.clear();
        bookListFull.addAll(books);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
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
                if (
                        book.getTitle().toLowerCase().contains(keyword) ||
                                book.getAuthor().toLowerCase().contains(keyword) ||
                                book.getGenre().toLowerCase().contains(keyword)
                ) {
                    bookList.add(book);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(android.R.id.text1);
            tvAuthor = itemView.findViewById(android.R.id.text2);
        }
    }
}