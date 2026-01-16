package com.example.aolmobileprogramming;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvStock.setText("Stock: " + book.getStock());

        String placeholder_image = "";

        Glide.with(holder.itemView.getContext())
                .load(book.getImage_url())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.broken_image)
                .into(holder.ivCover);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SectionBookActivity.class);

            intent.putExtra("BOOK_ID", book.getId());
            intent.putExtra("BOOK_TITLE", book.getTitle());
            intent.putExtra("BOOK_AUTHOR", book.getAuthor());
            intent.putExtra("BOOK_DESC", book.getDescription());
            intent.putExtra("BOOK_STOCK", book.getStock());

            v.getContext().startActivity(intent);
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
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvGenre, tvStock;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }

}