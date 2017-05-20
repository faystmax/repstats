package org.ams.repstats.controllers.admin.teams;

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
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.TeamTable;
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
 * Date: 22.04.2017
 * Time: 11:41
 */
public class TeamEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamEditController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private TableView teamTable;
    @FXML
    private TableColumn teamNameClmn;
    @FXML
    private TableColumn teamTechnolClmn;
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
    //endregion

    private HashMap<Integer, String> roles;    ///< id_role - name

    @FXML
    public void initialize() {
        showAllTeams();
        configureDevelopersTable();

        Utils.setEmptyTableMessage(teamTable);
        Utils.setEmptyTableMessage(developersTable);

        // добавили listener`a
        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int id_team = ((TeamTable) (teamTable.getSelectionModel().getSelectedItem())).getId();
                showDevelopersInTeam(id_team);
            }
        });
    }


    /**
     * Заполняем таблицу с командами
     */
    private void showAllTeams() {

        // region << Инициализируем колонки таблицы
        // Имя
        //Utils.configureStringColumnTeamTable(teamNameClmn,"name",MysqlConnector.updateNameTeam,LOGGER);
        teamNameClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("name"));
        teamNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamTable, String> t) {
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
                        //обновляем в базе
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
        //Utils.configureStringColumnTeamTable(teamTechnolClmn,"technology",MysqlConnector.updateTechnTeam,LOGGER);
        teamTechnolClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("technology"));
        teamTechnolClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamTechnolClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamTable, String> t) {
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
                        //обновляем в базе
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
     * Настраиваем колонки таблицы DevelopersTable
     */
    private void configureDevelopersTable() {

        // region << Инициализируем колонки таблицы
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

        // Телефон
        developerPhoneClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("phone"));
        developerPhoneClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        developerPhoneClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DeveloperTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DeveloperTable, String> t) {
                        //обновляем в базе
                        DeveloperTable changeable = ((DeveloperTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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


        //endregion

    }


    /**
     * Заполняем таблицу с разработчиками команды
     */
    private void showDevelopersInTeam(int idTeam) {

        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectDevelopersInTeam);
            preparedStatement.setInt(1, idTeam);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<DeveloperTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                //String role_name =
                data.add(new DeveloperTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
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
     * Добавляем  команду
     */
    public void addTeamButtonAction() {
        String newTeam = "Новая команда";
        String newTecnology = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewTeam);
            preparedStatement.setString(1, newTeam);
            preparedStatement.setString(2, newTecnology);
            MysqlConnector.executeUpdate();

            int newId = MysqlConnector.getInsertId();
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
            MysqlConnector.execute();

            teamTable.getItems().remove(selectedIndex);
            MysqlConnector.closeStmt();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            Utils.showError("Ошибка удаления", "Невозможно удалить выбранную команду! Возможно в ней ещё есть участники.",
                    "Невозможно удалить выбранную команду!", e);
        }

    }

    /**
     * Удаляем разработчика из команды
     */
    public void delDeveloperFromTeam() {
        int selectedIndex = developersTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((DeveloperTable) developersTable.getSelectionModel().getSelectedItem()).getId();
        if (Utils.conformationDialog("Удаление разработчика из группы", "Вы уверены,что хотите удалить " +
                "разработчика из выбранной группы?")) {
            try {
                PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDeveloperTeam);
                preparedStatement.setString(1, null);
                preparedStatement.setInt(2, selectedId);
                MysqlConnector.executeUpdate();

                developersTable.getItems().remove(selectedIndex);
                MysqlConnector.closeStmt();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                Utils.showError("Ошибка удаления", "Невозможно удалить разработчика из команды! ",
                        "", e);
            }
        }
    }

    /**
     * Добавляем ноаого разработчика в команду
     */
    public void addNewDevelopersInTeamButtonAction() {
        if (teamTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберие команду!");
            return;
        }
        String newName = "...";
        String newFam = "...";
        String newOtch = "...";
        int id_role = 0;
        int id_team = ((TeamTable) (teamTable.getSelectionModel().getSelectedItem())).getId();
        int newAge = 0;
        String newPhone = "...";
        String newRoleName = roles.get(id_team);
        String newGitname = "...";
        String newGitemail = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewDeveloper);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newFam);
            preparedStatement.setString(3, newOtch);
            preparedStatement.setInt(4, id_role);
            preparedStatement.setInt(5, id_team);
            preparedStatement.setInt(6, newAge);
            preparedStatement.setString(7, newPhone);
            preparedStatement.setString(8, newGitname);
            preparedStatement.setString(9, newGitemail);
            MysqlConnector.executeUpdate();
            int newId = MysqlConnector.getInsertId();

            DeveloperTable elem = new DeveloperTable(newId, newName, newFam, newOtch, id_role,
                    id_team, newAge, newPhone, newRoleName, newGitname, newGitemail);
            developersTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }


    /**
     * Окно "Добавление существующего разработчика"
     */
    public void developerInTeamAddButtonAction() {
        try {
            if (teamTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберие команду!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/teams/developerInTeamAddView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Добавление существующего разработчика в команду");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            DeveloperInTeamAddControlller controller = loader.getController();
            controller.setTeamEditController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Добавление существующего разработчика в команду по id
     *
     * @param id
     */
    public void existingDeveloperInTeamAdd(int id) {
        if (teamTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите команду!");
            return;
        }
        int id_team = ((TeamTable) (teamTable.getSelectionModel().getSelectedItem())).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateDeveloperTeam);
            preparedStatement.setInt(1, id_team);
            preparedStatement.setInt(2, id);

            MysqlConnector.executeUpdate();

            showDevelopersInTeam(id_team);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
