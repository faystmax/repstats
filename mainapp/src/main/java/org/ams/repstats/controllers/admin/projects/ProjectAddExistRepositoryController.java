package org.ams.repstats.controllers.admin.projects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.ProjectObs;
import org.ams.repstats.entity.RepositoryObs;
import org.ams.repstats.utils.RepositoryTableDateEditingCell;
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
 * Date: 29.04.2017
 * Time: 12:07
 */
public class ProjectAddExistRepositoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectAddExistRepositoryController.class); ///< ссылка на логер


    //region << UI Компоненты
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
    private Button btExit;
    @FXML
    private TextField descriptionTextField;
    //endregion

    private ProjectEditController projectEditController;

    @FXML
    public void initialize() {
        configureRepositoryClmn();
        showRepository();

        Utils.setEmptyTableMessage(repositoryTable);
    }

    private void configureRepositoryClmn() {
        repositoryTable.setEditable(true);
        // Название
        reposNameClmn.setCellValueFactory(new PropertyValueFactory<RepositoryObs, String>("name"));
        reposNameClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        reposNameClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryObs, String> t) {
                        RepositoryObs changeable = ((RepositoryObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
        reposUrlClmn.setCellValueFactory(new PropertyValueFactory<RepositoryObs, String>("url"));
        reposUrlClmn.setCellFactory(TextFieldTableCell.forTableColumn());
        reposUrlClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryObs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryObs, String> t) {
                        RepositoryObs changeable = ((RepositoryObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
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
        Callback<TableColumn<RepositoryObs, Date>, TableCell<RepositoryObs, Date>> dateOfCreationCellFactory
                = (TableColumn<RepositoryObs, Date> param) -> new RepositoryTableDateEditingCell();
        reposDateClmn.setCellValueFactory(new PropertyValueFactory<ProjectObs, Date>("dateOfCreation"));
        reposDateClmn.setCellFactory(dateOfCreationCellFactory);
        reposDateClmn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<RepositoryObs, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<RepositoryObs, Date> t) {
                        RepositoryObs changeable = ((RepositoryObs) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        /*
                        if (!Utils.isValidStringValue(t.getNewValue())) {
                            Utils.showAlert("Ошибка ввода!", "Неверное значение поля");
                            changeable.setAuthor(t.getOldValue());
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
        reposResponsClmn.setCellValueFactory(new PropertyValueFactory<RepositoryObs, String>("FIO"));


    }

    private void showRepository() {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllRepository);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<RepositoryObs> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new RepositoryObs(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getInt(5),
                        rs.getString(6),
                        "...",
                        0));
            }
            MysqlConnector.closeStmt();

            repositoryTable.setItems(data);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Выход
     *
     * @param event
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * Сеттер родительмкого контроллера
     *
     * @param projectEditController
     */
    public void setProjectEditController(ProjectEditController projectEditController) {
        this.projectEditController = projectEditController;
    }

    /**
     * Выбор репозитория
     */
    public void repositorySelectButtonAction() {
        if (repositoryTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите репозиторий!");
            return;
        }
        if (!Utils.isValidStringValue(descriptionTextField.getText())) {
            Utils.showAlert("Ошибка добавления", "Введённое вами описание не корректно!");
            return;
        }
        int id_repository = ((RepositoryObs) (repositoryTable.getSelectionModel().getSelectedItem())).getId();
        this.projectEditController.addExistRepositoryInProject(id_repository, descriptionTextField.getText());
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }


}
