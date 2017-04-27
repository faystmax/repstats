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
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.utils.ProjectTableDateEditingCell;
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
        configureAndShowProjectsClmn();
        configureRepositoryClmn();
        // добавили listener`a
        projectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int id_proj = ((ProjectTable) (projectsTable.getSelectionModel().getSelectedItem())).getId();
                showRepositoryInTable(id_proj);
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

            int newId = MysqlConnector.getinsertId();
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
            int newIdRepository = MysqlConnector.getinsertId();

            //добавляем связь в промежуточную таблицу
            preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewProjectRepository);
            preparedStatement.setInt(1, newIdRepository);
            preparedStatement.setInt(2, id_project);
            preparedStatement.setString(3, description);
            MysqlConnector.executeUpdate();
            int newIdProjectRepository = MysqlConnector.getinsertId();

            RepositoryTable elem = new RepositoryTable(newIdRepository, newName, newUrl, cur, idDeveloperResponsible,
                    FIODeveloperResponsible, description, newIdProjectRepository);
            repositoryTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }

}
