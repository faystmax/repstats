package org.ams.repstats.controllers.admin.developers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.TeamObs;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 23.04.2017
 * Time: 13:45
 */
public class ChooseTeamForDeveloperController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseTeamForDeveloperController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private TableView teamTable;
    @FXML
    private TableColumn teamNameClmn;
    @FXML
    private TableColumn teamTechnolClmn;
    @FXML
    private TableColumn teamCountClmn;
    @FXML
    private Button btExit;
    //endregion

    private DevelopersEditController developersEditController;

    @FXML
    public void initialize() {
        showAllTeams();
        Utils.setEmptyTableMessage(teamTable);
    }

    /**
     * Заполняем таблицу с командами
     */
    private void showAllTeams() {

        // region << Инициализируем колонки таблицы
        // Имя
        teamNameClmn.setCellValueFactory(new PropertyValueFactory<TeamObs, String>("name"));
        teamNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamObs, String> t) {
                        TeamObs changeable = ((TeamObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
        teamTechnolClmn.setCellValueFactory(new PropertyValueFactory<TeamObs, String>("technology"));
        teamTechnolClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamTechnolClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamObs, String> t) {
                        TeamObs changeable = ((TeamObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setTechnology(t.getOldValue());
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

        // Кол-во разработчиков в группе
        teamCountClmn.setCellValueFactory(new PropertyValueFactory<TeamObs, String>("count"));

        //endregion

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllTeamsWithCount);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<TeamObs> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TeamObs(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }
            MysqlConnector.closeStmt();

            teamTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }


    public void setDevelopersEditController(DevelopersEditController developersEditController) {
        this.developersEditController = developersEditController;
    }

    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    public void teamSelectButtonAction() {
        if (teamTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите команду!");
            return;
        }
        int id_team = ((TeamObs) (teamTable.getSelectionModel().getSelectedItem())).getId();
        this.developersEditController.existingDeveloperInTeamAdd(id_team);
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }
}
