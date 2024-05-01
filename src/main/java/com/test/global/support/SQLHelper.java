package com.test.global.support;

import io.cucumber.datatable.DataTable;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
//        Statement statement = null;
//        ResultSet resultSet = null;

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

    public void deleteWorkingClassHeroRecordByNatid(String natid) throws SQLException {
        Connection conn = getConnection();

        try {
            Statement statement = conn.createStatement();

            String query = "delete from testdb.working_class_heroes where natid = '" + natid + "';";
            System.out.println("Deleting working class hero record: " + natid);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public void deleteVoucherByNatid(String natid) throws SQLException {
        Connection conn = getConnection();

        try {
            Statement statement = conn.createStatement();

            String query = "delete from testdb.vouchers where working_class_hero_id = (select id from testdb.working_class_heroes where natid = '" + natid + "');";
            System.out.println("Deleting voucher record: " + natid);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public void selectRecord(String statement, DataTable dataTable) throws Throwable {
        java.util.List<java.util.List<String>> expectedTable;
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String expectedValue;
        String actualValue;
        String errMsg = "";

        statement = webUI.extractToken(statement);

        int retries = 5;
        while (retries > 0) {
            errMsg = "";
            ResultSet rs = stmt.executeQuery(statement.replaceAll(";", ""));
            expectedTable = dataTable.asLists();
            System.out.println("Expected table: " + expectedTable);

            for (int i = 1; i < expectedTable.size(); i++) {
                if (rs.next()) {
                    for (int j = 0; j < expectedTable.get(i).size(); j++) {
                        try {
                            expectedValue = webUI.extractToken(expectedTable.get(i).get(j)).replaceAll("\\r", "").replaceAll("\\s+", " ").trim();

                            DateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
                            TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
                            dtFormat.setTimeZone(tz);

                            if (expectedValue.contains("<today_date>")) {
                                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));

                                expectedValue = expectedValue.replace("<today_date>", dtFormat.format(calendar.getTime()));
                            }

                            if (expectedValue.contains("<today+1_date>")) {
                                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));
                                calendar.add(Calendar.DATE, 1);
                                expectedValue = expectedValue.replace("<today+1_date>", dtFormat.format(calendar.getTime()));
                            }

                            if (expectedValue.contains("<today+30_date>")) {
                                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Singapore"));
                                calendar.add(Calendar.DATE, 30);
                                expectedValue = expectedValue.replace("<today+30_date>", dtFormat.format(calendar.getTime()));
                            }
                        } catch (NullPointerException e) {
                            expectedValue = "";
                        }

                        //check if data at database contains at least one character
                        if (rs.getString(expectedTable.get(0).get(j)) != null) {
                            actualValue = rs.getString(expectedTable.get(0).get(j));

                            System.out.println("Expected value are: " + expectedValue);
                            System.out.println("Actual value are: " + actualValue);
                        } else {
                            System.out.println("Actual value at database: null");
                            System.out.println("Expected value are: " + expectedValue);
                            actualValue = "null";
                        }

                        if (!expectedValue.equals(actualValue.trim())) {
                            errMsg = errMsg + "Expected value is \"" + expectedValue + "\", but actual value is \"" + actualValue + "\" in Row " + i + ", Column \"" + expectedTable.get(0).get(j) + "\".\r\n";
                        }
                    }

                } else {
                    errMsg = errMsg + "Expected table contains more rows than resultset.\r\n";
                }
            }

            if (errMsg.length() > 0) { // Expected != actual, retry
                retries--;
                Thread.sleep(1000);
                System.out.println("Retrying select record.\nRetries remaining: " + retries);
            } else break; // Expected = actual
        }

        // After retries and expected != actual
        if (errMsg.length() > 0) {
            closeConnection(conn);
            throw new Exception(errMsg);
        }

        closeConnection(conn);
    }
}
