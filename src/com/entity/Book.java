package com.entity;

public class Book {
    public int id;
    public String title;
    public int publisherId;
    public String notes;
    public String userName;

    public Book(int id, String title, int publisherId, String notes, String userName) {
        this.id = id;
        this.title = title;
        this.publisherId = publisherId;
        this.notes = notes;
        this.userName = userName;
    }

}
