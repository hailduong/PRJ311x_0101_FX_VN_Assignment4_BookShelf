package com.DAO;

import com.context.DBContext;
import com.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> getAllBooks() throws Exception {
        String query = "SELECT * FROM book";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        List<Book> books = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            int publisherId = rs.getInt("publisherId");
            String notes = rs.getString("notes");
            String userName = rs.getString("userName");

            Book book = new Book(id, title, publisherId, notes, userName);
            books.add(book);
        }

        rs.close();
        connection.close();
        return books;
    }

    public Book getBookById(int id) throws Exception {
        List<Book> books = getAllBooks();
        for (Book book : books) {
            if (book.id == id) {
                return book;
            }
        }
        return null;
    }
}
