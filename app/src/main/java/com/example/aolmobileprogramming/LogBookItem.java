package com.example.aolmobileprogramming;

import com.google.firebase.Timestamp;

public class LogBookItem {
    private String bookId;
    public String title;
    public String author;

    public LogBookItem(String bookId, String title, String author, String imgUrl, String status, Timestamp borrowedDate, Timestamp dueDate, Timestamp returnedDate) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.imgUrl = imgUrl;
        this.status = status;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public String imgUrl;

    public String status;
    public Timestamp borrowedDate;
    public Timestamp dueDate;
    public Timestamp returnedDate;

    public LogBookItem() {} // utk Firestore

    // GETTER SETTER
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }

    public Timestamp getBorrowedDate() {
        return borrowedDate;
    }
    public Timestamp getReturnedDate() {
        return returnedDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

}

