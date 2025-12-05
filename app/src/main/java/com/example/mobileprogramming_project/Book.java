package com.example.mobileprogramming_project;

public class Book {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }

    public int getAge_rating() {
        return age_rating;
    }

    public void setAge_rating(int age_rating) {
        this.age_rating = age_rating;
    }

    public int getYear_published() {
        return year_published;
    }

    public void setYear_published(int year_published) {
        this.year_published = year_published;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Book() {}

    public Book(String title, String author, String description, String genre, int pages, int stock, int borrowed, int age_rating, int year_published) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.pages = pages;
        this.stock = stock;
        this.borrowed = borrowed;
        this.age_rating = age_rating;
        this.year_published = year_published;
    }

    private String id;
    private String title;
    private String author;
    private String description;
    private String genre;


    private int pages;
    private int stock;
    private int borrowed;
    private int age_rating;
    private int year_published;
}
