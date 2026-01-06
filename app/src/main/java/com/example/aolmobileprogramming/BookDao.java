package com.example.aolmobileprogramming;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDao {
    public static void createBook(FirebaseFirestore db, Book book) {
        DocumentReference newBookRef = db.collection("books").document();
        String myId = newBookRef.getId();
        book.setId(myId);
        newBookRef.set(book)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Book created. Internal ID " + book.getId() + " matches Doc ID " + myId);
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding book", e);
                });
    }
    public static void updateBook(FirebaseFirestore db, String docId, String newTitle, String newAuthor, String newDescription, String newGenre, int newPages, int newStock, int newBorrowed, int newAge_rating, int newYear_published) {
        Map<String, Object> updates = new HashMap<>();

        if (newTitle != null && !newTitle.isEmpty()) {
            updates.put("title", newTitle);
        }
        if (newAuthor != null && !newAuthor.isEmpty()) {
            updates.put("author", newAuthor);
        }
        if (newDescription != null && !newDescription.isEmpty()) {
            updates.put("description", newDescription);
        }
        if (newGenre != null && !newGenre.isEmpty()) {
            updates.put("genre", newGenre);
        }
        if (newPages != -1) {
            updates.put("pages", newPages);
        }
        if (newStock != -1) {
            updates.put("stock", newStock);
        }
        if (newBorrowed != -1) {
            updates.put("borrowed", newBorrowed);
        }
        if (newAge_rating != -1) {
            updates.put("age_rating", newAge_rating);
        }
        if (newYear_published != -1) {
            updates.put("year_published", newYear_published);
        }

        db.collection("books").document(docId)
                .update(updates)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Book successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error updating document", e));
    }


    public static void readBorrowedBooks(FirebaseFirestore db, BookCallBack callBack) {
        db.collection("books")
                .whereGreaterThan("borrowed", 0)
                .limit(3)
                .get()
                .addOnSuccessListener(query -> {
                    List<Book> books = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : query) {
                        Book book = doc.toObject(Book.class);
                        book.setId(doc.getId());
                        books.add(book);
                    }

                    callBack.onSuccess(books);
                })
                .addOnFailureListener(callBack::onError);
    }

    //buat all available book
    public static void readAvailableBooks(FirebaseFirestore db, BookCallBack callBack) {
        db.collection("books")
                .whereGreaterThan("stock", 0)
                .get()
                .addOnSuccessListener(query -> {
                    List<Book> books = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : query) {
                        Book book = doc.toObject(Book.class);
                        book.setId(doc.getId());
                        books.add(book);
                    }

                    callBack.onSuccess(books);
                })
                .addOnFailureListener(callBack::onError);
    }



    public static void deleteBook(FirebaseFirestore db, String docId) {
        db.collection("books").document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Book successfully deleted!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error deleting document", e));
    }

    public interface BookCallBack{
        void onSuccess(List<Book> books);
        void onError(Exception e);
    }
}
