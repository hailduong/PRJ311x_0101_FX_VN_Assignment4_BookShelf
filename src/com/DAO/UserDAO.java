package com.DAO;

import com.context.DBContext;
import com.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    static UserDAO instance = null;

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public List<User> getAllUser() throws Exception {
        String query = "SELECT * FROM user";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            String userName = rs.getString("userName");
            String password = rs.getString("password");
            String description = rs.getString("description");

            User newUser = new User(userName, password, description);
            users.add(newUser);
        }

        rs.close();
        connection.close();
        return users;
    }

    public User getUserByName(String name) throws Exception {
        String query = "SELECT * FROM user where userName = ?";
        Connection connection = new DBContext().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();

        User newUser = null;

        while (rs.next()) {
            String userName = rs.getString("userName");
            String password = rs.getString("password");
            String description = rs.getString("description");

            newUser = new User(userName, password, description);
        }

        rs.close();
        connection.close();
        return newUser;
    }

    public boolean validateUser(User user) throws Exception {
        User foundUser = getUserByName(user.userName);

        if (foundUser == null) {
            return false;
        }

        if (!foundUser.password.equals(user.password)) {
            return false;
        }

        return true;
    }
}
