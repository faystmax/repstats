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
    private static final String url = "jdbc:mysql://localhost:3306/gitstats?characterEncoding=UTF-8";
    private static final String user = "root";
    private static final String password = "";
    private static final String driverName = "com.mysql.jdbc.Driver";

    // JDBC variables for opening and managing connection
    private static Connection myConn = null;
    private static PreparedStatement myStmt = null;
    private static ResultSet myRs = null;

    // Подготовленные запросы
    public static final String selectAllTeams = "select id,name,technology from team";
    public static final String insertNewTeam = "insert into team (name, technology) values (?, ?)";
    public static final String deleteTeam = "delete from team where id = ?";
    public static final String updateNameTeam = "update team set name = ? WHERE id = ?";
    public static final String updateTechnTeam = "update team set technology = ? WHERE id = ?";
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
     * Prepare statement with ret key
     *
     * @param query - строка запроса
     * @return PreparedStatement для заполнения параметров
     * @throws SQLException - ошибка запроса
     */
    public static PreparedStatement prepeareStmtRetKey(String query) throws SQLException {
        myStmt = myConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
        return myRs;
    }

    /**
     * Execute SQL update
     *
     * @return кол-во обновлённых строк или сгенерированный id
     * @throws SQLException - ошибка запроса
     */
    public static int executeUpdate() throws SQLException {
        int rowUpdated = myStmt.executeUpdate();
        return rowUpdated;
    }

    /**
     * Execute SQL
     *
     * @return Фдаг о выполнении
     * @throws SQLException - ошибка запроса
     */
    public static boolean execute() throws SQLException {
        boolean rez = myStmt.execute();
        return rez;
    }


    public static void closeStmt() {
        try {
            if (myStmt != null) {
                myStmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
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
