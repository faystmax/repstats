package org.ams.repstats;

import org.ams.repstats.utils.Utils;
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

    public static final String selectDevelopersInTeam = "select developer.id,developer.name,surname,middlename,id_role,id_team,age,phone,role.name from developer" +
            " inner JOIN role on role.id=developer.id_role" +
            " where id_team = ?";

    public static final String selectAllDevelopers = "select developer.id,developer.name,surname,middlename,id_role,id_team,age,phone,role.name from developer" +
            " inner JOIN role on role.id=developer.id_role";
    public static final String selectAllDevelopersWithTeam = "select developer.id,developer.name,surname,middlename,id_role,id_team,age,phone,role.name," +
            "(SELECT team.name from team WHERE team.id=developer.id_team) from developer,role " +
            "where  role.id=developer.id_role";

    public static final String insertNewDeveloper = "insert into developer (name,surname,middlename,id_role,id_team,age,phone) " +
            "values (?, ?, ?, ?, ?, ?, ?)";
    public static final String deleteDeveloper = "delete from developer WHERE id = ?";
    public static final String updateDeveloperTeam = "update developer set id_team = ? WHERE id = ?";
    public static final String updateNameDeveloper = "update developer set name = ? WHERE id = ?";
    public static final String updateSurnameDeveloper = "update developer set surname = ? WHERE id = ?";
    public static final String updateMidleNameDeveloper = "update developer set middlename = ? WHERE id = ?";
    public static final String updateAgeDeveloper = "update developer set age = ? WHERE id = ?";
    public static final String updatePhoneDeveloper = "update developer set phone = ? WHERE id = ?";
    public static final String updateRoleDeveloper = "update developer set id_role = ? WHERE id = ?";

    public static final String selectAllRoles = "select id,name from role";
    public static final String selectAllDevelopersWithNull = "select developer.id,developer.name,surname,middlename,id_role,id_team,age,phone,role.name from developer" +
            " inner JOIN role on role.id=developer.id_role" +
            " where id_team is NULL";
    public static final String selectAllTeamsWithCount = "SELECT team.id,team.name,team.technology,COUNT(developer.id_team)" +
            " FROM team,developer where developer.id_team=team.id || developer.id_team is null GROUP BY team.id";

    // project
    public static final String updateNameProject = "update project set name = ? WHERE id = ?";
    public static final String updateStartProject = "update project set start = ? WHERE id = ?";
    public static final String updateDeadlineProject = "update project set deadline = ? WHERE id = ?";
    public static final String updatePriorProject = "update project set priority = ? WHERE id = ?";
    public static final String selectAllProject = "select id,name,start,deadline,priority from project";
    public static final String insertNewProject = "insert into project (name, start, deadline, priority) values (?, ?, ?, ?)";
    public static final String deleteProject = "delete from project where id = ?";

    // repository
    public static final String updateNameRepository = "update repository set name = ? WHERE id = ?";
    public static final String updateUrlRepository = "update repository set url = ? WHERE id = ?";
    public static final String updateDateOfCreationProject = "update repository set date_of_creation = ? WHERE id = ?";
    public static final String updateDescriptoion = "update project_repository set description = ? WHERE id_repository = ?";
    public static final String updateResponsible = "update repository set repository.id_developer_responsible = ? WHERE id = ?";
    public static final String selectRepositoryInProjects = "SELECT repository.id,repository.name,url,date_of_creation,repository.id_developer_responsible," +
            "(SELECT concat(name,\" \",surname,\" \",middlename)  FROM developer WHERE  developer.id = repository.id_developer_responsible) as FIO," +
            "project_repository.description,project_repository.id " +
            "FROM repository,project_repository,project " +
            "WHERE " +
            "repository.id = project_repository.id_repository " +
            "and project_repository.id_project = project.id AND " +
            "project.id = ?";
    public static final String selectAllRepository = "SELECT repository.id,repository.name,url,date_of_creation,repository.id_developer_responsible," +
            "(SELECT concat(name,\" \",surname,\" \",middlename)  FROM developer WHERE  developer.id = repository.id_developer_responsible) as FIO " +
            "FROM repository ";
    public static final String insertNewProjectRepository = "insert into project_repository ( id_repository, id_project, description) " +
            "values (?, ?, ?)";
    public static final String insertNewRepository = "insert into repository (name, url, id_developer_responsible, date_of_creation) " +
            "values (?, ?, ?, ?)";

    public static final String delRepositoryFromProject = "delete from project_repository where id = ?";
    public static final String delRepository = "delete from repository where id = ?";

    // TODO role_in_the_project
    public static final String insertNewDeveloperProject = "insert into developer_project (id_developer,id_project,role_in_the_project)" +
            "values (?, ?, ?)";

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
                Utils.showError("Ошибка", "Ошибка подключения к базе данных", "Mysql драйвер не найден", ex);
                System.exit(-1);
            } catch (SQLException ex) {
                LOGGER.error("Failed to create the database connection.");
                Utils.showError("Ошибка", "Ошибка подключения к базе данных MySql", null, ex);
                System.exit(-1);
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
        return myStmt.executeUpdate();
    }

    /**
     * Получаем сгенерированный Id
     *
     * @return - сгенерированный id
     * @throws SQLException - ошибка запроса
     */
    public static int getinsertId() throws SQLException {
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return (int) generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
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
}
