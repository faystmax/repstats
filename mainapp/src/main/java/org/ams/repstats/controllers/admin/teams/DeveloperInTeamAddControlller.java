package org.ams.repstats.controllers.admin.teams;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.DeveloperObs;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 23.04.2017
 * Time: 0:09
 */
public class DeveloperInTeamAddControlller {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloperInTeamAddControlller.class); ///< ссылка на логер

    //region << UI Компоненты
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
    private TableView developersTable;
    @FXML
    private Button btExit;
    //endregion

    private TeamEditController teamEditController;
    private HashMap<Integer, String> roles;         ///< id_role - name

    @FXML
    public void initialize() {
        configureDevelopersTable();
        showDevelopersInTeam();

        Utils.setEmptyTableMessage(developersTable);
    }

    /**
     * Настраиваем колонки таблицы DevelopersTable
     */
    private void configureDevelopersTable() {

        // region << Инициализируем колонки таблицы
        // Имя
        developerNameClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("name"));
        developerNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
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
        developerFamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("surname"));
        developerFamClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerFamClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
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
        developerOtchClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("middlename"));
        developerOtchClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerOtchClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
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
        developerAgeClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, Integer>("age"));
        developerAgeClmn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        developerAgeClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, Integer> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
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

        // Телефон
        developerPhoneClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("phone"));
        developerPhoneClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerPhoneClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setPhone(t.getOldValue());
                            // обновляем колонку
                            developerPhoneClmn.setVisible(false);
                            developerPhoneClmn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updatePhoneDeveloper);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setPhone(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // роль
        developerRoleClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("role_name"));
        //
        // генерим хеш ролей
        this.readAllRoles();
        ObservableList<String> rolesOL = FXCollections.observableArrayList();
        rolesOL.addAll(FXCollections.observableArrayList(roles.values()));
        //
        developerRoleClmn.setCellFactory(ComboBoxTableCell.forTableColumn(rolesOL));
        developerRoleClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidValue(t.getNewValue())) {
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


        //endregion

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
    private void showDevelopersInTeam() {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllDevelopersWithNull);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<DeveloperObs> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new DeveloperObs(rs.getInt(1),
                        rs.getString(3),
                        rs.getString(2),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9)));
            }
            MysqlConnector.closeStmt();

            developersTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * сеттер родительского контроллера
     *
     * @param teamEditController
     */
    public void setTeamEditController(TeamEditController teamEditController) {
        this.teamEditController = teamEditController;
    }

    /**
     * Выход
     * @param event
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * Выбок разработчика
     */
    public void developerSelectButtonAction() {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите разработчика!");
            return;
        }
        int id_team = ((DeveloperObs) (developersTable.getSelectionModel().getSelectedItem())).getId();
        this.teamEditController.existingDeveloperInTeamAdd(id_team);
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }
}
