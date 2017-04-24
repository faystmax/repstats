package org.ams.repstats.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.utils.DateEditingCell;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
    public TableView projectsTable;
    @FXML
    public TableColumn projectNameClmn;
    @FXML
    public TableColumn projectDateClmn;
    @FXML
    public TableColumn projectDeadlineClmn;
    @FXML
    public TableColumn projectPriorClmn;
    @FXML
    public TableView repositoryTable;
    @FXML
    public TableColumn reposNameClmn;
    @FXML
    public TableColumn reposUrlClmn;
    @FXML
    public TableColumn reposDateClmn;
    @FXML
    public TableColumn reposResponsClmn;
    @FXML
    public TableColumn reposDeskClmn;
    //endregion

    @FXML
    public void initialize() {
        showAllProjects();
        configureProjectsClmn();
        // добавили listener`a
        projectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //int id_team = ((TeamTable) (teamTable.getSelectionModel().getSelectedItem())).getId();
                //showDevelopersInTeam(id_team);
            }
        });


    }

    private void configureProjectsClmn() {

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
                = (TableColumn<ProjectTable, Date> param) -> new DateEditingCell();
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
                = (TableColumn<ProjectTable, Date> param) -> new DateEditingCell();
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

    private void showAllProjects() {
    }
}
