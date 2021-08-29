package com.entity;

public class User {
    public String userName;
    public String password;
    public String description;

    public User(String name, String pass) {
        this.userName = name;
        this.password = pass;
    }

    public User(String name, String pass, String description) {
        this.userName = name;
        this.password = pass;
        this.description = description;
    }

}
