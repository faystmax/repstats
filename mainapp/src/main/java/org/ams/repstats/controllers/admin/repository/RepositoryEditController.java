package org.ams.repstats.controllers.admin.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.utils.RepositoryTableDateEditingCell;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 21.04.2017
 * Time: 17:19
 */
public class RepositoryEditController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryEditController.class); ///< ссылка на логер

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
    private TableColumn reposDeskClmn;
    //endregion


    @FXML
    public void initialize() {
        configureRepositoryClmn();
        showRepository();
    }

    private void configureRepositoryClmn() {
        repositoryTable.setEditable(true);
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


    }

    private void showRepository() {
        // Извлекаем данные из базы
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllRepository);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<RepositoryTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new RepositoryTable(rs.getInt(1),
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
     * Удаляем репозиторий
     */
    public void delRepositoryButtonAction() {
        int selectedIndex = repositoryTable.getSelectionModel().getSelectedIndex();
        int selectedId = ((RepositoryTable) repositoryTable.getSelectionModel().getSelectedItem()).getId();
        if (Utils.conformationDialog("Удаление репозитория ", "Вы уверены,что хотите удалить " +
                "репозиторий?")) {
            try {
                PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.delRepository);
                preparedStatement.setInt(1, selectedId);
                MysqlConnector.execute();

                repositoryTable.getItems().remove(selectedIndex);
                MysqlConnector.closeStmt();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                Utils.showError("Ошибка удаления", "Невозможно удалить репозиторий! ",
                        "", e);
            }
        }
    }

    /**
     * Добавляем новый репозиторий
     */
    public void addNewRepositoryButtonAction() {
        String newName = "...";
        String newUrl = "...";
        Date cur = new Date();
        java.sql.Date startdateOfCreation = new java.sql.Date(cur.getTime());
        Integer idDeveloperResponsible = -1;
        String FIODeveloperResponsible = "";
        String description = "...";
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmtRetKey(MysqlConnector.insertNewRepository);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newUrl);
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
            preparedStatement.setDate(4, startdateOfCreation);

            MysqlConnector.executeUpdate();
            int newIdRepository = MysqlConnector.getinsertId();

            RepositoryTable elem = new RepositoryTable(newIdRepository, newName, newUrl, cur, idDeveloperResponsible,
                    FIODeveloperResponsible, description, -1);
            repositoryTable.getItems().add(elem);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * Окно "Изминение ответственного за репозиторий"
     */
    public void repositoryResponsibleButtonAction() {
        try {
            if (repositoryTable.getSelectionModel().getSelectedItem() == null) {
                Utils.showAlert("Ошибка добавления", "Сначала выберите репозиторий!");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/admin/repository/repositoryResponsibleSelectView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Изминение ответственного за репозиторий");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            RepositoryResponsibleSelectController controller = loader.getController();
            controller.setRepositoryResponsibleSelectController(this);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Изминение ответственного за репозиторий по id
     *
     * @param id_developer
     */
    public void repositoryResponsible(int id_developer) {
        if (repositoryTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка добавления", "Сначала выберите репозиторий!");
            return;
        }
        int id_repository = ((RepositoryTable) (repositoryTable.getSelectionModel().getSelectedItem())).getId();
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.updateResponsible);
            preparedStatement.setInt(1, id_developer);
            preparedStatement.setInt(2, id_repository);

            MysqlConnector.executeUpdate();

            showRepository();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
