package com.DAO;

import com.context.DBContext;
import com.entity.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {
    public List<Publisher> getAllPublishers() throws Exception {
        String query = "SELECT * FROM publisher";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        List<Publisher> publishers = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");

            Publisher publisher = new Publisher(id, name, address);
            publishers.add(publisher);
        }

        rs.close();
        connection.close();
        return publishers;
    }

    public Publisher getPublisherById(int pid) throws Exception {
        List<Publisher> publishers = getAllPublishers();
        for (Publisher publisher : publishers) {
            if (publisher.id == pid) {
                return publisher;
            }
        }
        return null;
    }
}
