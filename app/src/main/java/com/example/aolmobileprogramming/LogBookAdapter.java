package com.example.aolmobileprogramming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;


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

        Glide.with(h.itemView.getContext()).load(item.getImgUrl())
                        .placeholder(R.drawable.placeholder_image).error(R.drawable.broken_image).into(h.ivCover);

        h.tvTitle.setText(item.getTitle());
        h.tvAuthor.setText(item.getAuthor());
        h.tvBorrowed.setText("Borrowed: "+ formatDate(item.getBorrowedDate()));
        if(item.getDueDate()!=null){
            h.tvDue.setText("Due: "+ formatDate(item.getDueDate()));
        } else{
            h.tvDue.setText("Due: null");
        }


        if ("RETURNED".equals(item.getStatus())) {
            h.tvStatus.setText("Returned");
            h.tvReturned.setVisibility(View.VISIBLE);
            h.tvReturned.setText("Returned: " + formatDate(item.getBorrowedDate()));
        } else if ("OVERDUE".equals(item.getStatus())){
            h.tvStatus.setText("Overdue");

            h.tvStatus.setTextColor(h.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            h.tvReturned.setVisibility(View.GONE);
        } else{
            h.tvStatus.setText("Borrowed");
            h.tvStatus.setTextColor(h.itemView.getContext().getResources().getColor(android.R.color.black));
            h.tvReturned.setVisibility(View.GONE);
        }

        String imageUrl = item.getImgUrl();

        if (imageUrl!=null && !imageUrl.isEmpty()){
            Glide.with(h.itemView.getContext()).load(item.getImgUrl())
                    .placeholder(R.drawable.placeholder_image).error(R.drawable.broken_image).into(h.ivCover);
        } else{
            h.ivCover.setImageResource(R.drawable.placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";

        java.util.Date date = timestamp.toDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvBorrowed, tvStatus, tvReturned, tvDue;
        ImageView ivCover;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDue = itemView.findViewById(R.id.tvDue);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBorrowed = itemView.findViewById(R.id.tvBorrowed);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvReturned = itemView.findViewById(R.id.tvReturned);
        }
    }
}



