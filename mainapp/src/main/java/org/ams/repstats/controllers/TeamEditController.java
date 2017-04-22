package org.ams.repstats.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.Utils;
import org.ams.repstats.fortableview.TeamTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 11:41
 */
public class TeamEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamEditController.class); ///< ссылка на логер

    @FXML
    private TableView teamTable;
    @FXML
    private TableColumn teamNameClmn;
    @FXML
    private TableColumn teamTechnolClmn;
    @FXML
    private TableColumn developersTable;
    @FXML
    private TableColumn developerFamClmn;
    @FXML
    private TableColumn developerNameClmn;
    @FXML
    private TableColumn developerOtchClmn;
    @FXML
    private TableColumn developerAgeClmn;
    @FXML
    private TableColumn developerPhoneClmn;
    @FXML
    private TableColumn developerRoleClmn;

    @FXML
    public void initialize() {
        showAllTeams();

    }

    /**
     * Заполняем таблицу с командами
     */
    private void showAllTeams() {

        // region << Инициализируем колонки таблицы
        // Имя
        teamNameClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("name"));
        teamNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamTable, String> t) {
                        //обновляем в базе
                        TeamTable changeable = ((TeamTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            teamNameClmn.setVisible(false);
                            teamNameClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateNameTeam);
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
        // Технология
        teamTechnolClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("technology"));
        teamTechnolClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamTechnolClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamTable, String> t) {
                        //обновляем в базе
                        TeamTable changeable = ((TeamTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            teamTechnolClmn.setVisible(false);
                            teamTechnolClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateTechnTeam);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setTechnology(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );
        //endregion

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllTeams);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<TeamTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TeamTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)));
            }
            MysqlConnector.closeStmt();

            teamTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Добавляем  команду
     */
    public void addTeamButtonAction() {
        String newTeam = "Новая команда";
        String newTecnology = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewTeam);
            preparedStatement.setString(1, newTeam);
            preparedStatement.setString(2, newTecnology);
            int newId = MysqlConnector.executeUpdate();

            TeamTable elem = new TeamTable(newId, "Новая команда", "...");
            teamTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * Удаляем команду
     */
    public void delTeamButtonAction() {
        int selectedIndex = teamTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((TeamTable) teamTable.getSelectionModel().getSelectedItem()).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.deleteTeam);
            preparedStatement.setInt(1, selectedId);
            boolean rez = MysqlConnector.execute();

            teamTable.getItems().remove(selectedIndex);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            Utils.showError("Ошибка удаления", "Невозможно удалить выбранную команду! Возможно в ней ещё есть участники.",
                    "Невозможно удалить выбранную команду!", e);
        }

    }

}
