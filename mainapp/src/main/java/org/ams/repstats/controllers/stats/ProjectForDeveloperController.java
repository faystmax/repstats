package org.ams.repstats.controllers.stats;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.ProjectObs;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.05.2017
 * Time: 20:01
 */
public class ProjectForDeveloperController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectForDeveloperController.class); ///< ссылка на логер

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
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private Button btExit;
    //endregion

    private StatsDeveloperController statsDeveloperController;      ///< ссылка на родительские контроллеры
    private int id_developer;                                       ///< id разработчика

    @FXML
    public void initialize() {
        datePickerStart.setValue(LocalDate.now().minusDays(1));
        datePickerEnd.setValue(LocalDate.now());
    }

    public void configureAndShowProjectsClmn() {

        projectsTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        // Название
        projectNameClmn.setCellValueFactory(new PropertyValueFactory<ProjectObs, String>("name"));

        // Date start
        projectDateClmn.setCellValueFactory(new PropertyValueFactory<ProjectObs, Date>("dateStart"));

        // deadline
        projectDeadlineClmn.setCellValueFactory(new PropertyValueFactory<ProjectObs, Date>("deadline"));

        // Приоритет
        projectPriorClmn.setCellValueFactory(new PropertyValueFactory<ProjectObs, Integer>("prior"));

        ObservableList<ProjectObs> data = FXCollections.observableArrayList();
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllDevelopersProjects);
            preparedStatement.setString(1, Integer.toString(id_developer));
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                data.add(new ProjectObs(rs.getInt(1),
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

    /**
     * Установка id
     *
     * @param id_developer
     */
    public void setId_developer(int id_developer) {
        this.id_developer = id_developer;
    }

    /**
     * Начало анализа
     *
     * @param event
     */
    public void startButtonAction(ActionEvent event) {
        if (projectsTable.getSelectionModel().getSelectedItem() == null || datePickerStart.getValue() == null || datePickerEnd.getValue() == null) {
            Utils.showAlert("Ошибка", "Выберите проект(ы) и выберите промежуток времени!");
            return;
        }

        if (datePickerStart.getValue().isAfter(datePickerEnd.getValue())) {
            Utils.showAlert("Ошибка", "Выберите корректный  промежуток времени!");
            return;
        }

        ObservableList<ProjectObs> selectedItems = projectsTable.getSelectionModel().getSelectedItems();

        //
        ArrayList<ProjectObs> selectedProjectObss = new ArrayList<ProjectObs>();
        for (ProjectObs row : selectedItems) {
            selectedProjectObss.add(row);
        }

        statsDeveloperController.projects = selectedProjectObss;
        statsDeveloperController.start = datePickerStart.getValue();
        statsDeveloperController.end = datePickerEnd.getValue();
        exitButtonAction(null);
    }

    /**
     * Закрытие окна
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * Установка ссылки на родителя
     *
     * @param statsDeveloperController
     */
    public void setStatsDeveloperController(StatsDeveloperController statsDeveloperController) {
        this.statsDeveloperController = statsDeveloperController;
    }
}
