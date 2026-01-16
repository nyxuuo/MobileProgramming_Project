package com.example.aolmobileprogramming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class LogBookAdapter extends RecyclerView.Adapter<LogBookAdapter.ViewHolder> {

    private final List<LogBookItem> items = new ArrayList<>();
    private final List<LogBookItem> allItems = new ArrayList<>();

    public LogBookAdapter() {}

    // DIPANGGIL ACTIVITY
    public void setData(List<LogBookItem> newItems) {
        items.clear();
        allItems.clear();

        if (newItems != null) {
            items.addAll(newItems);
            allItems.addAll(newItems);
        }

        notifyDataSetChanged();
    }

    // FILTER SEARCH
    public void filter(String keyword) {
        items.clear();

        if (keyword == null || keyword.trim().isEmpty()) {
            items.addAll(allItems);
        } else {
            keyword = keyword.toLowerCase();
            for (LogBookItem item : allItems) {
                if (item.getTitle().toLowerCase().contains(keyword) ||
                        item.getAuthor().toLowerCase().contains(keyword)) {
                    items.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logbook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        LogBookItem item = items.get(position);

        h.tvTitle.setText(item.getTitle());
        h.tvAuthor.setText(item.getAuthor());
        h.tvBorrowed.setText("Borrowed: " + item.getBorrowedDate());

        if ("RETURNED".equals(item.getStatus())) {
            h.tvStatus.setText("Returned");
            h.tvReturned.setVisibility(View.VISIBLE);
            h.tvReturned.setText("Returned: " + item.getReturnedDate());
        } else {
            h.tvStatus.setText("Borrowed");
            h.tvReturned.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvBorrowed, tvStatus, tvReturned;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBorrowed = itemView.findViewById(R.id.tvBorrowed);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvReturned = itemView.findViewById(R.id.tvReturned);
        }
    }
}



