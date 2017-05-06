package org.ams.repstats.controllers.admin.projects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.utils.ProjectTableDateEditingCell;
import org.ams.repstats.utils.RepositoryTableDateEditingCell;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 21.04.2017
 * Time: 16:44
 */
public class ProjectEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectEditController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private TableView projectsTable;
    @FXML
    private TableColumn projectNameClmn;
    @FXML
    private TableColumn projectDateClmn;
    @FXML
    private TableColumn projectDeadlineClmn;
    @FXML
    private TableColumn projectPriorClmn;
    @FXML
    private TableView repositoryTable;
    @FXML
    private TableColumn reposNameClmn;
    @FXML
    private TableColumn reposUrlClmn;
    @FXML
    private TableColumn reposDateClmn;
    @FXML
    private TableColumn reposResponsClmn;
    @FXML
    private TableColumn reposDeskClmn;
    @FXML
    private TableView developersTable;
    @FXML
    private TableColumn developerFamClmn;
    @FXML
    private TableColumn developerNameClmn;
    @FXML
    private TableColumn developerOtchClmn;
    @FXML
    private TableColumn developerAgeClmn;
    @FXML
    private TableColumn developerRoleClmn;
    @FXML
    private TableColumn developerRoleINProjectClmn;
    //endregion

    private HashMap<Integer, String> roles;    ///< id_role - name

    @FXML
    public void initialize() {
        configureAndShowProjectsClmn();
        configureRepositoryClmn();
        configureDevelopersTable();
        // добавили listener`a
        projectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int id_proj = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
                showRepositoryInTable(id_proj);
                showDeveloperswithProject(id_proj);
            }
        });
    }

    private void configureAndShowProjectsClmn() {

        ObservableList<ProjectTable> data = FXCollections.observableArrayList();

        // Название
        projectNameClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, String>("name"));
        projectNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        projectNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProjectTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProjectTable, String> t) {
                        ProjectTable changeable = ((ProjectTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            projectNameClmn.setVisible(false);
                            projectNameClmn.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateNameProject);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setName(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // Date start
        Callback<TableColumn<ProjectTable, Date>, TableCell<ProjectTable, Date>> dateStartCellFactory
                = (TableColumn<ProjectTable, Date> param) -> new ProjectTableDateEditingCell();
        projectDateClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, Date>("dateStart"));
        projectDateClmn.setCellFactory(dateStartCellFactory);
        projectDateClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProjectTable, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProjectTable, Date> t) {
                        ProjectTable changeable = ((ProjectTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        /*
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            projectDateClmn.setVisible(false);
                            projectDateClmn.setVisible(true);
                            return;
                        }*/
                        //обновляем в базе
                        try {
                            // util Date to sql Date
                            java.sql.Date sqlDate = new java.sql.Date(t.getNewValue().getTime());
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateStartProject);
                            preparedStatement.setDate(1, sqlDate);
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setDateStart(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                });


        // deadline
        Callback<TableColumn<ProjectTable, Date>, TableCell<ProjectTable, Date>> dateDeadlineCellFactory
                = (TableColumn<ProjectTable, Date> param) -> new ProjectTableDateEditingCell();
        projectDeadlineClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, Date>("deadline"));
        projectDeadlineClmn.setCellFactory(dateDeadlineCellFactory);
        projectDeadlineClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProjectTable, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProjectTable, Date> t) {
                        ProjectTable changeable = ((ProjectTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        /*
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            projectDeadlineClmn.setVisible(false);
                            projectDeadlineClmn.setVisible(true);
                            return;
                        }*/
                        //обновляем в базе
                        try {
                            // util Date to sql Date
                            java.sql.Date sqlDate = new java.sql.Date(t.getNewValue().getTime());
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDeadlineProject);
                            preparedStatement.setDate(1, sqlDate);
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setDeadline(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                });
        // Приоритет
        projectPriorClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, Integer>("prior"));
        projectPriorClmn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        projectPriorClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProjectTable, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProjectTable, Integer> t) {
                        //обновляем в базе
                        ProjectTable changeable = ((ProjectTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setPrior(t.getOldValue());
                            // обновляем колонку
                            projectPriorClmn.setVisible(false);
                            projectPriorClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updatePriorProject);
                            preparedStatement.setInt(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setPrior(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllProject);
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                data.add(new ProjectTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getDate(3),
                        rs.getDate(4),
                        rs.getInt(5)));
            }
            MysqlConnector.closeStmt();

            projectsTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }


    }

    private void configureRepositoryClmn() {
        // Название
        reposNameClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("name"));
        reposNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        reposNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryTable, String> t) {
                        RepositoryTable changeable = ((RepositoryTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            reposNameClmn.setVisible(false);
                            reposNameClmn.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateNameRepository);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setName(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );
        // Url
        reposUrlClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("url"));
        reposUrlClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        reposUrlClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryTable, String> t) {
                        RepositoryTable changeable = ((RepositoryTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setUrl(t.getOldValue());
                            // обновляем колонку
                            reposUrlClmn.setVisible(false);
                            reposUrlClmn.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateUrlRepository);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setUrl(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // Date of creation
        Callback<TableColumn<RepositoryTable, Date>, TableCell<RepositoryTable, Date>> dateOfCreationCellFactory
                = (TableColumn<RepositoryTable, Date> param) -> new RepositoryTableDateEditingCell();
        reposDateClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, Date>("dateOfCreation"));
        reposDateClmn.setCellFactory(dateOfCreationCellFactory);
        reposDateClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryTable, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryTable, Date> t) {
                        RepositoryTable changeable = ((RepositoryTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        /*
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            projectDateClmn.setVisible(false);
                            projectDateClmn.setVisible(true);
                            return;
                        }*/
                        //обновляем в базе
                        try {
                            // util Date to sql Date
                            java.sql.Date sqlDate = new java.sql.Date(t.getNewValue().getTime());
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDateOfCreationProject);
                            preparedStatement.setDate(1, sqlDate);
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setDateOfCreation(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                });

        // Ответственный
        reposResponsClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("FIO"));

        // Описание
        reposDeskClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("description"));
        reposDeskClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        reposDeskClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryTable, String> t) {
                        RepositoryTable changeable = ((RepositoryTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setDescription(t.getOldValue());
                            // обновляем колонку
                            reposDeskClmn.setVisible(false);
                            reposDeskClmn.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDescriptoion);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setDescription(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

    }


    private void showRepositoryInTable(int id_proj) {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectRepositoryInProjects);
            preparedStatement.setInt(1, id_proj);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<RepositoryTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new RepositoryTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getInt(8)));
            }
            MysqlConnector.closeStmt();

            repositoryTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Настраиваем колонки таблицы DevelopersTable
     */
    private void configureDevelopersTable() {

        // Имя
        developerNameClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("name"));
        developerNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            developerNameClmn.setVisible(false);
                            developerNameClmn.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateNameDeveloper);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setName(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );
        // Фамилия
        developerFamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("surname"));
        developerFamClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerFamClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setSurname(t.getOldValue());
                            // обновляем колонку
                            developerFamClmn.setVisible(false);
                            developerFamClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateSurnameDeveloper);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setSurname(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // Отчество
        developerOtchClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("middlename"));
        developerOtchClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerOtchClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setMiddlename(t.getOldValue());
                            // обновляем колонку
                            developerOtchClmn.setVisible(false);
                            developerOtchClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateMidleNameDeveloper);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setMiddlename(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // Возраст
        developerAgeClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, Integer>("age"));
        developerAgeClmn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        developerAgeClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, Integer> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setAge(t.getOldValue());
                            // обновляем колонку
                            developerAgeClmn.setVisible(false);
                            developerAgeClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateAgeDeveloper);
                            preparedStatement.setInt(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setAge(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // роль
        developerRoleClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("role_name"));
        //
        // генерим хеш ролей
        this.readAllRoles();
        ObservableList<String> rolesOL = FXCollections.observableArrayList();
        rolesOL.addAll(FXCollections.observableArrayList(roles.values()));
        //
        developerRoleClmn.setCellFactory(ComboBoxTableCell.forTableColumn(rolesOL));
        developerRoleClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            Integer id_role = (Integer) Utils.getKeyFromValue(roles, t.getOldValue());
                            changeable.setId_role(id_role);
                            changeable.setRole_name(t.getOldValue());
                            // обновляем колонку
                            developerRoleClmn.setVisible(false);
                            developerRoleClmn.setVisible(true);
                            return;
                        }
                        try {
                            Integer id_role = (Integer) Utils.getKeyFromValue(roles, t.getNewValue());
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateRoleDeveloper);
                            preparedStatement.setInt(1, id_role);
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            if (id_role != null) {
                                changeable.setId_role(id_role);
                                changeable.setRole_name(t.getNewValue());
                            }

                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // роль в проекте
        developerRoleINProjectClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("role_in_project"));
        developerRoleINProjectClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerRoleINProjectClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setRole_in_project(t.getOldValue());
                            // обновляем колонку
                            developerRoleINProjectClmn.setVisible(false);
                            developerRoleINProjectClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateRoleInProject);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId_developer_project());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setRole_in_project(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );
    }

    /**
     * Считываем все роли в Хеш
     */
    private void readAllRoles() {
        roles = new HashMap<Integer, String>();
        roles.put(0, "111");

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllRoles);
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                roles.put(rs.getInt(1), rs.getString(2));
            }
            MysqlConnector.closeStmt();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Заполняем таблицу со всеми разработчиками
     */
    private void showDeveloperswithProject(int id_proj) {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllDevelopersInPoject);
            preparedStatement.setInt(1, id_proj);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<DeveloperTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new DeveloperTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getInt(8),
                        rs.getString(9)));
            }
            MysqlConnector.closeStmt();

            developersTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Добавляем  Проект
     */
    public void addProjectButtonAction() {
        String newName = "Новый проект";
        java.sql.Date startDate = new java.sql.Date(new Date().getTime());
        java.sql.Date deadline = startDate;
        Integer newPrior = 50;
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewProject);
            preparedStatement.setString(1, newName);
            preparedStatement.setDate(2, startDate);
            preparedStatement.setDate(3, deadline);
            preparedStatement.setInt(4, newPrior);
            MysqlConnector.executeUpdate();

            int newId = MysqlConnector.getInsertId();
            ProjectTable elem = new ProjectTable(newId, newName, startDate, deadline, newPrior);
            projectsTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * Удаляем Проект
     */
    public void delProjectButtonAction() {
        int selectedIndex = projectsTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((ProjectTable) projectsTable.getSelectionModel().getSelectedItem()).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.deleteProject);
            preparedStatement.setInt(1, selectedId);
            MysqlConnector.execute();

            projectsTable.getItems().remove(selectedIndex);
            MysqlConnector.closeStmt();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            Utils.showError("Ошибка удаления", "Невозможно удалить выбранный проект!\nВозможно к нему ещё привязаны участники.",
                    "Невозможно удалить выбранный проект!", e);
        }

    }

    /**
     * Удаляем репозиторий из проекта
     */
    public void delRepositoryFromProjectButtonAction() {
        int selectedIndex = repositoryTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((RepositoryTable) repositoryTable.getSelectionModel().getSelectedItem()).getId_project_repository();
        if (Utils.conformationDialog("Удаление репозитория из проекта", "Вы уверены,что хотите удалить " +
                "репозиторий из выбранного проекта?")) {
            try {
                PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.delRepositoryFromProject);
                preparedStatement.setInt(1, selectedId);
                MysqlConnector.execute();

                repositoryTable.getItems().remove(selectedIndex);
                MysqlConnector.closeStmt();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                Utils.showError("Ошибка удаления", "Невозможно удалить репозиторий из проекта! ",
                        "", e);
            }
        }
    }

    /**
     * Добавляем новый репозиторий в проект
     */
    public void addNewRepositoryInProjectButtonAction() {
        if (projectsTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберие проект!");
            return;
        }
        String newName = "...";
        String newUrl = "...";
        Date cur = new Date();
        java.sql.Date startdateOfCreation = new java.sql.Date(cur.getTime());
        Integer idDeveloperResponsible = -1;
        String FIODeveloperResponsible = "";
        int id_project = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
        String description = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewRepository);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newUrl);
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
            preparedStatement.setDate(4, startdateOfCreation);
            MysqlConnector.executeUpdate();
            int newIdRepository = MysqlConnector.getInsertId();

            //добавляем связь в промежуточную таблицу
            preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewProjectRepository);
            preparedStatement.setInt(1, newIdRepository);
            preparedStatement.setInt(2, id_project);
            preparedStatement.setString(3, description);
            MysqlConnector.executeUpdate();
            int newIdProjectRepository = MysqlConnector.getInsertId();

            RepositoryTable elem = new RepositoryTable(newIdRepository, newName, newUrl, cur, idDeveloperResponsible,
                    FIODeveloperResponsible, description, newIdProjectRepository);
            repositoryTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * Окно "Приерепить к команде"
     */
    public void projectConnectWithTeamWindowButtonAction() {
        try {
            if (projectsTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберие проект!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/projects/projectConnectWithTeamView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Прикрепление проекта к команде");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            ProjectConnectWithTeamController controller = loader.getController();

            controller.setTeamEditController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Прикрепление проекта к команде по id_team
     *
     * @param id_team
     */
    public void projectConnectWithTeamButtonAction(int id_team) {
        if (projectsTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите команду!");
            return;
        }
        int id_project = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
        try {
            // бежим по всем разработчикам в команде и крепим к ним проект
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectDevelopersInTeam);
            preparedStatement.setInt(1, id_team);
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                // получаем id разработчика
                int id_developer = rs.getInt(1);

                PreparedStatement preparedStatement2 = MysqlConnector.prepeareStmt(MysqlConnector.insertNewDeveloperProject);
                preparedStatement2.setInt(1, id_developer);
                preparedStatement2.setInt(2, id_project);
                preparedStatement2.setString(3, "...");

                MysqlConnector.executeUpdate();     // вставляем запись
            }
            // обновляем проекты
            configureAndShowProjectsClmn();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Окно "Прикрепление проекта к разработчику"
     */
    public void developerConnectWithProjecButtonAction() {
        try {
            if (projectsTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберите проект!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/projects/projectConnectWithDeveloperView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Прикрепление проекта к разработчику");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            ProjectConnectWithDeveloperController controller = loader.getController();
            controller.setProjectEditController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Прикрепление проекта к разработчику по id_developer
     *
     * @param id_developer -  id разработчика
     * @param role         - роль в проекте
     */
    public void developerConnectWithProject(int id_developer, String role) {
        if (projectsTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите проект!");
            return;
        }
        int id_project = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
        try {
            PreparedStatement preparedStatement2 = MysqlConnector.prepeareStmt(MysqlConnector.insertNewDeveloperProject);
            preparedStatement2.setInt(1, id_developer);
            preparedStatement2.setInt(2, id_project);
            preparedStatement2.setString(3, role);

            MysqlConnector.executeUpdate();

            showDeveloperswithProject(id_project);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * Окно "Добавление сущ. репозитория в проект"
     */
    public void addExistRepositoryInProjectButtonAction() {
        try {
            if (projectsTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберите проект!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/projects/projectAddExistRepositoryView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Прикрепление репозитория к проекту");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            ProjectAddExistRepositoryController controller = loader.getController();
            controller.setProjectEditController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Добавление сущ. репозитория в проект
     *
     * @param id_repository -  id разработчика
     * @param description   - роль в проекте
     */
    public void addExistRepositoryInProject(int id_repository, String description) {
        if (projectsTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите проект!");
            return;
        }
        int id_project = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
        try {
            //добавляем связь в промежуточную таблицу
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewProjectRepository);
            preparedStatement.setInt(1, id_repository);
            preparedStatement.setInt(2, id_project);
            preparedStatement.setString(3, description);

            MysqlConnector.executeUpdate();

            showRepositoryInTable(id_project);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Открепление разработчика от проекта
     *
     * @param event
     */
    public void undoDeveloperFromProject(ActionEvent event) {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите разработчика!");
            return;
        }
        int id_developer_project = ((DeveloperTable) (developersTable.getSelectionModel().getSelectedItem())).getId_developer_project();
        int id_project = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.deleteDeveloperProject);
            preparedStatement.setInt(1, id_developer_project);
            MysqlConnector.execute();
            MysqlConnector.closeStmt();

            showDeveloperswithProject(id_project);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
