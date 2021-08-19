package com.entity;

public class Author {
    int id;
    String name;
    String address;

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
