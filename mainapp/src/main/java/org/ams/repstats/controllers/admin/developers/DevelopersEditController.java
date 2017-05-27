package org.ams.repstats.controllers.admin.developers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.DeveloperObs;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 21.04.2017
 * Time: 16:18
 */
public class DevelopersEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevelopersEditController.class); ///< ссылка на логер

    //region << UI Компоненты
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
    private TableColumn developerPhoneClmn;
    @FXML
    private TableColumn developerRoleClmn;
    @FXML
    private TableColumn developerTeamClmn;
    @FXML
    private TableColumn developerGitName;
    @FXML
    private TableColumn developerGitEmail;
    //endregion

    private HashMap<Integer, String> roles;    ///< id_role - name

    @FXML
    public void initialize() {
        developersTable.setEditable(true);
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
        developerFamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("surname"));
        developerFamClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerFamClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
        developerOtchClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("middlename"));
        developerOtchClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerOtchClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
        developerAgeClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, Integer>("age"));
        developerAgeClmn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        developerAgeClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, Integer> t) {
                        //обновляем в базе
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
                        if (!Utils.isValidStringValue(t.getNewValue())) {
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
        //Команда

        developerTeamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("team_name"));

        // gitname
        developerGitName.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("gitname"));
        developerGitName.setCellFactory(TextFieldTableCell.forTableColumn());
        developerGitName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setGitname(t.getOldValue());
                            // обновляем колонку
                            developerGitName.setVisible(false);
                            developerGitName.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateGitName);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setGitname(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

        // gitemail
        developerGitEmail.setCellValueFactory(new PropertyValueFactory<DeveloperObs, String>("gitemail"));
        developerGitEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        developerGitEmail.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperObs, String> t) {
                        DeveloperObs changeable = ((DeveloperObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setGitemail(t.getOldValue());
                            // обновляем колонку
                            developerGitEmail.setVisible(false);
                            developerGitEmail.setVisible(true);
                            return;
                        }
                        //обновляем в базе
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateGitEmail);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setGitemail(t.getNewValue());
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
     * Заполняем таблицу с разработчиками
     */
    private void showDevelopersInTeam() {

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllDevelopersWithTeamName);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<DeveloperObs> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String team_name = rs.getString(10) == null ? "" : rs.getString(10);
                data.add(new DeveloperObs(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getString(8),
                        rs.getString(9),
                        team_name,
                        rs.getString(11),
                        rs.getString(12)));
            }
            MysqlConnector.closeStmt();

            developersTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
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
     * Добавляем нового разработчика
     */
    public void addNewDevelopersInTeamButtonAction() {
        String newName = "...";
        String newFam = "...";
        String newOtch = "...";
        int id_role = 0;
        int id_team = -1;
        int newAge = 0;
        String newPhone = "...";
        String newRoleName = "";
        String newTeamName = "";
        String newGitname = "...";
        String newGitemail = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewDeveloper);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newFam);
            preparedStatement.setString(3, newOtch);
            preparedStatement.setInt(4, id_role);
            preparedStatement.setString(5, null);
            preparedStatement.setInt(6, newAge);
            preparedStatement.setString(7, newPhone);
            preparedStatement.setString(8, newGitname);
            preparedStatement.setString(9, newGitemail);
            MysqlConnector.executeUpdate();
            int newId = MysqlConnector.getInsertId();

            DeveloperObs elem = new DeveloperObs(newId, newName, newFam, newOtch, id_role,
                    id_team, newAge, newPhone, newRoleName, newTeamName, newGitname, newGitemail);
            developersTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }


    /**
     * Удаляем разработчика
     */
    public void delDeveloper() {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка удаления", "Сначала выберите разработчика!");
            return;
        }
        int selectedIndex = developersTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((DeveloperObs) developersTable.getSelectionModel().getSelectedItem()).getId();
        if (Utils.conformationDialog("Удаление разработчика ", "Удалить всю информацию о выбранном разработчике?")) {
            try {
                PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.deleteDeveloper);
                preparedStatement.setInt(1, selectedId);
                MysqlConnector.executeUpdate();

                developersTable.getItems().remove(selectedIndex);
                MysqlConnector.closeStmt();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                Utils.showError("Ошибка удаления", "Невозможно удалить выбранного разработчика! ",
                        "", e);
            }
        }
    }

    /**
     * Окно "Выбор команды для разработчика"
     */
    public void developerInTeamAddButtonAction() {
        try {
            if (developersTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберие разработчика!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/developers/chooseTeamForDeveloperView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Выбор команды для разработчика");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            ChooseTeamForDeveloperController controller = loader.getController();
            controller.setDevelopersEditController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Добавление разработчика в команду по id команды
     *
     * @param id
     */
    public void existingDeveloperInTeamAdd(int id) {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите разработчика!");
            return;
        }
        int id_developer = ((DeveloperObs) (developersTable.getSelectionModel().getSelectedItem())).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDeveloperTeam);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id_developer);

            MysqlConnector.executeUpdate();
            Utils.informationDialog("Запись успешно изменена", "Выбранный разработчик успещно добавлен в команду!");
            showDevelopersInTeam();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
