package com.entity;

import com.DAO.AuthorDAO;
import com.DAO.PublisherDAO;

import java.util.List;

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

    public List<Author> getAuthors() throws Exception {
        List<Author> authors = AuthorDAO.getInstance().getAuthorsByBookId(this.id);
        return authors;
    }

    public Publisher getPublisher() throws Exception {
        List<Publisher> publisherList = PublisherDAO.getInstance().getAllPublishers();
        Publisher foundPublisher = null;
        for (Publisher publisher : publisherList) {
            if (publisher.id == this.publisherId) foundPublisher = publisher;
        }
        return foundPublisher;
    }
}
