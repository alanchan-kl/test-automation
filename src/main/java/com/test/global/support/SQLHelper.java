package com.test.global.support;

import java.sql.*;
import java.sql.DriverManager;

public class SQLHelper {
    private final WebUI webUI;

    public SQLHelper(WebUI webUI) {
        this.webUI = webUI;
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static Connection getConnection() {
        // Database URL
        String url = "jdbc:mysql://localhost:3306/testdb";

        // Database credentials
        String username = "user";
        String password = "userpassword";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(url, username, password);

            // Connection successful
            System.out.println("Connected to the database.");

//            // Create a statement
//            statement = connection.createStatement();
//
//            // Execute a query
//            String query = "select * from testdb.working_class_heroes order by created_at;";
//            resultSet = statement.executeQuery(query);
//
//            // Process the result set
//            while (resultSet.next()) {
//                System.out.println("TEST: " + resultSet.getString("id"));
//                System.out.println("TEST: " + resultSet.getString("natid"));
//                System.out.println("TEST: " + resultSet.getString("name"));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
