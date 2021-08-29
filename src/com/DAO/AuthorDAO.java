package com.DAO;

import com.context.DBContext;
import com.entity.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    static AuthorDAO instance = null;

    public static AuthorDAO getInstance() {
        if (instance == null) {
            instance = new AuthorDAO();
        }
        return instance;
    }

    public List<Author> getAllAuthors() throws Exception {
        String query = "SELECT * FROM author";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        List<Author> authors = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");

            Author author = new Author(id, name, address);
            authors.add(author);
        }

        rs.close();
        connection.close();
        return authors;
    }

    public Author getAuthorById(int id) throws Exception {
        List<Author> authors = getAllAuthors();
        for (Author author : authors) {
            if (author.id == id) {
                return author;
            }
        }
        return null;
    }

    public List<Author> getAuthorsByBookId(int bookId) throws Exception {
        String query = "SELECT * FROM author WHERE bookId = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Author> authors = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");

            Author author = new Author(id, name, address);
            authors.add(author);
        }

        resultSet.close();
        connection.close();
        return authors;
    }
}
