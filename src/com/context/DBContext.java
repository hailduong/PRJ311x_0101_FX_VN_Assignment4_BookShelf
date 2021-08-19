package com.context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    private final String serverName = "localhost";
    private final String dbName = "book_shelf";
    private final String port = "3306";
    private final String userId = "root";
    private final String password = "";

    public Connection getConnection() throws Exception {
        String url = "jdbc:mysql://" + serverName + ":" + port + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, userId, password);
        return connection;
    }
}
