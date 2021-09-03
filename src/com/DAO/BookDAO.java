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
        String query2 = "DELETE FROM book WHERE id = ?";
        Connection connection2 = new DBContext().getConnection();
        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        preparedStatement2.setInt(1, bookId);
        preparedStatement2.executeUpdate();
        preparedStatement2.close();
        connection.close();
    }

    public void editBook(Book newBook, List<Author> authors, int oldBookId) throws Exception {
        Connection connection = new DBContext().getConnection();

        // Update the `authorBook` table, remove the old author from the book
        String deleteQuery = "DELETE FROM authorBook WHERE bookId = ?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(deleteQuery);
        preparedStatement2.setInt(1, oldBookId);

        // Update the `book` table
        String updateQuery = "UPDATE book SET title = ?, publisherId = ?, notes = ?, id = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, newBook.title);
        preparedStatement.setInt(2, newBook.publisherId);
        preparedStatement.setString(3, newBook.notes);
        preparedStatement.setInt(4, newBook.id);
        preparedStatement.setInt(5, oldBookId);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Update the new author to the book
        for (int i = 0; i < authors.size(); i++) {
            Author author = authors.get(i);
            String insertQuery = "INSERT INTO authorBook (authorId, bookId) values(?, ?)";
            PreparedStatement preparedStatement3 = connection.prepareStatement(insertQuery);
            preparedStatement3.setInt(1, author.id);
            preparedStatement3.setInt(2, newBook.id);
            preparedStatement3.executeUpdate();
            preparedStatement3.close();
        }
        connection.close();
    }

    public void addBook(Book book, List<Author> authorList) throws Exception {
        // Add this book into the book table
        String addQuery = "INSERT INTO book (title, publisherId, notes, userName) values( ?, ?, ?, ?)";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(addQuery);

        preparedStatement.setString(1, book.title);
        preparedStatement.setInt(2, book.publisherId);
        preparedStatement.setString(3, book.notes);
        preparedStatement.setString(4, book.userName);

        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Add this book into the authorBook table
        for (int i = 0; i < authorList.size(); i++) {
            Author author = authorList.get(i);
            String insertQuery = "INSERT INTO authorBook (authorId, bookId) values(?, LAST_INSERT_ID())";
            PreparedStatement preparedStatement1 = connection.prepareStatement(insertQuery);
            preparedStatement1.setInt(1, author.id);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        }
        connection.close();
    }

    public List<Book> getBookBy(String columnName, String keyword) throws Exception {
        System.out.println("[BookDAO] > columnName: " + columnName);
        System.out.println("[BookDAO] > keyword: " + keyword);

        String columnQuery = "";
        switch (columnName) {
            case "book.id": {
                columnQuery = "id";
                break;
            }
            case "book.title": {
                columnQuery = "title";
                break;
            }
            default: {
                columnQuery = "title";
            }
        }

        System.out.println("[BookDAO] > columnQuery: " + columnQuery);
        String query = "SELECT * FROM book WHERE ? = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, columnQuery);
        if (columnQuery.equalsIgnoreCase("id")) {
            preparedStatement.setInt(2, Integer.parseInt(keyword));
        } else {
            preparedStatement.setString(2, keyword);
        }

        ResultSet rs = preparedStatement.executeQuery();

        List<Book> bookList = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            int publisherId = rs.getInt("publisherId");
            String notes = rs.getString("notes");
            String userName = rs.getString("userName");

            Book book = new Book(id, title, publisherId, notes, userName);
            bookList.add(book);
        }
        preparedStatement.close();
        connection.close();
        System.out.println("[BookDAO] > number of book found: " + bookList.size());
        return bookList;
    }
}
