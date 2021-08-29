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

        // Get the list of Author Ids
        String query = "SELECT * FROM authorBook WHERE bookId = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Integer> authorIdList = new ArrayList<>();
        while (resultSet.next()) {
            int authorId = resultSet.getInt("authorId");
            authorIdList.add(authorId);
        }
        resultSet.close();
        preparedStatement.close();

        // Get the list of author
        List<Author> authors = new ArrayList<>();

        for (int authorId : authorIdList) {
            String query1 = "SELECT * FROM author where id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, authorId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                int id = resultSet1.getInt("id");
                String name = resultSet1.getString("name");
                String address = resultSet1.getString("address");

                Author author = new Author(id, name, address);
                authors.add(author);
            }

            resultSet1.close();
            preparedStatement1.close();
        }

        connection.close();
        return authors;
    }
}
