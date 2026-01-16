package com.example.aolmobileprogramming;

import android.content.Context;
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

public class SectionBookAdapter extends RecyclerView.Adapter<SectionBookAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> books = new ArrayList<>();

    public SectionBookAdapter(Context context) {
        this.context = context;
    }

    public void setBooks(List<Book> data) {
        books.clear();
        if (data != null) {
            books.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Book book = books.get(position);

        h.tvTitle.setText(book.getTitle());
        h.tvAuthor.setText(book.getAuthor());
        h.tvStock.setText("Stock: " + book.getStock());

        Glide.with(context)
                .load(book.getImage_url())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.broken_image)
                .into(h.imgCover);

        // 👉 klik ke DETAIL
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle, tvAuthor, tvStock;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }
}
