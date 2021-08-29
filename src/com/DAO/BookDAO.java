package com.DAO;

import com.context.DBContext;
import com.entity.Author;
import com.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    static BookDAO instance = null;

    public static BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }

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

    public void deleteBook(int bookId) throws Exception {
        // Delete from authorBook
        String query = "DELETE FROM authorBook WHERE bookId = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Delete from book
        String query2 = "DELETE FROM book WHERE bookId = ?";
        Connection connection2 = new DBContext().getConnection();
        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        preparedStatement2.setInt(1, bookId);
        preparedStatement2.executeUpdate();
        preparedStatement2.close();
        connection.close();
    }

    public void editBook(Book book) throws Exception {
        // Update the `book` table
        String updateQuery = "UPDATE book SET title = ?, publisherId = ?, notes = ? WHERE id = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, book.title);
        preparedStatement.setInt(1, book.publisherId);
        preparedStatement.setString(1, book.notes);
        preparedStatement.setInt(1, book.id);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Update the `authorBook` table, remove the old author from the book
        String deleteQuery = "DELETE FROM authorBook WHERE bookId = ?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(deleteQuery);
        preparedStatement2.setInt(1, book.id);

        // Update the new author to the book
        List<Author> authors = book.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            Author author = authors.get(i);
            String insertQuery = "INSERT INTO authorBook values(?, ?, ?)";
            PreparedStatement preparedStatement3 = connection.prepareStatement(insertQuery);
            preparedStatement3.setInt(1, author.id);
            preparedStatement3.setInt(2, book.id);
            preparedStatement3.setInt(3, book.id);
            preparedStatement3.executeUpdate();
            preparedStatement3.close();
        }
        connection.close();
    }

    public void addBook(Book book) throws Exception {
        // Add this book into the book table
        String addQuery = "INSERT INTO book values(?, ?, ?, ?, ?)";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(addQuery);

        preparedStatement.setInt(1, book.id);
        preparedStatement.setString(2, book.title);
        preparedStatement.setInt(3, book.publisherId);
        preparedStatement.setString(3, book.notes);
        preparedStatement.setString(3, book.userName);

        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Add this book into the authorBook table
        List<Author> authorList = book.getAuthors();
        for (int i = 0; i < authorList.size(); i++) {
            Author author = authorList.get(i);
            String insertQuery = "INSERT INTO authorBook values(?, ?, ?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(addQuery);
            preparedStatement1.setInt(1, author.id);
            preparedStatement1.setInt(2, book.id);
            preparedStatement1.setInt(3, i);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        }
        connection.close();
    }
}
