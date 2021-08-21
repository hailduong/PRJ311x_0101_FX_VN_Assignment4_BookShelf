package com.DAO;

import com.context.DBContext;
import com.entity.Author;
import com.entity.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
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
}
