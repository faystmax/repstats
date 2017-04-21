package org.ams.repstats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 20.04.2017
 * Time: 0:38
 */

public class MysqlConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnector.class); ///< ссылка на логер

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/gitstats";
    private static final String user = "root";
    private static final String password = "";
    private static final String driverName = "com.mysql.jdbc.Driver";

    // JDBC variables for opening and managing connection
    private static Connection myConn = null;
    private static PreparedStatement myStmt = null;
    private static ResultSet myRs = null;

    /**
     * Инициализирует соединение с БД
     *
     * @return Connection to database
     */
    public static Connection getConnection() {
        if (myConn == null) {
            try {
                Class.forName(driverName);

                // Get a connection to database
                myConn = DriverManager.getConnection(url, user, password);

            } catch (ClassNotFoundException ex) {
                LOGGER.error("Driver not found.");
            } catch (SQLException ex) {
                LOGGER.error("Failed to create the database connection.");
            }
        }
        return myConn;
    }

    /**
     * Prepare statement
     *
     * @param query - строка запроса
     * @return PreparedStatement для заполнения параметров
     * @throws SQLException - ошибка запроса
     */
    public static PreparedStatement prepeareStmt(String query) throws SQLException {
        myStmt = myConn.prepareStatement(query);
        return myStmt;
    }

    /**
     * Execute SQL query
     *
     * @return ResultSet
     * @throws SQLException - ошибка запроса
     */
    public static ResultSet executeQuery() throws SQLException {
        myRs = myStmt.executeQuery();
        myStmt.close();
        return myRs;
    }

    /**
     * Execute SQL update
     *
     * @return ResultSet
     * @throws SQLException - ошибка запроса
     */
    public static int executeUpdate() throws SQLException {
        int rowUpdated = myStmt.executeUpdate();
        myStmt.close();
        return rowUpdated;
    }

/*
    public static void main(String[] args) throws SQLException {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 1. Get a connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "student", "student");

            // 2. Prepare statement
            myStmt = myConn.prepareStatement("select * from employees where salary > ? and department=?");

            // 3. Set the parameters
            myStmt.setDouble(1, 80000);
            myStmt.setString(2, "Legal");

            // 4. Execute SQL query
            myRs = myStmt.executeQuery();


        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (myRs != null) {
                myRs.close();
            }
            if (myStmt != null) {
                myStmt.close();
            }
            if (myConn != null) {
                myConn.close();
            }
        }
    }*/
}
