package org.ams.repstats.controllers.stats;

import com.selesse.gitwrapper.myobjects.Author;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.AuthorTable;
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.FilesTable;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.utils.Utils;
import org.ams.repstats.view.ViewInterfaceAbstract;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 30.04.2017
 * Time: 13:56
 */
public class StatsDeveloperController extends ViewInterfaceAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsDeveloperController.class); ///< ссылка на логер


    //region << UI Компоненты
    @FXML
    private TableView developersTable;
    @FXML
    private TableColumn developerFamClmn;
    @FXML
    private TableColumn developerNameClmn;
    @FXML
    private TableColumn developerGitName;
    @FXML
    private TableColumn developerGitEmail;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private Button btShowCommits;
    @FXML
    private TableView tableAllFiles;
    @FXML
    private TableColumn clmnPath;
    @FXML
    private TableColumn clmnIsBinary;
    @FXML
    private TableColumn clmnLOC;
    @FXML
    private TableView avtorTable;
    @FXML
    private TableColumn clmnAvtorName;
    @FXML
    private TableColumn clmnAvtorEmail;
    @FXML
    private TableColumn clmnCommitCount;
    @FXML
    private TableColumn clmnLinesAdd;
    @FXML
    private TableColumn clmnLinesDelete;
    @FXML
    private TableColumn clmnNetContribution;
    @FXML
    private Button btOutRep;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Label lbNameRep;
    @FXML
    private Label lbNazv;
    @FXML
    private Label lbBranch;
    @FXML
    private Label lbNazvCur;
    @FXML
    private Label lbBranchCur;
    @FXML
    private Label lbKolCommit;
    @FXML
    private Label lbKolCommitCur;
    @FXML
    private Label lbRemoteName;
    @FXML
    private TextField tbProject;
    @FXML
    private ImageView imageView;
    @FXML
    private Button btChoose;
    @FXML
    private Button btStart;
    @FXML
    //endregion

    private DirectoryChooser directoryChooser;
    File projectDir;


    @FXML
    public void initialize() {
        this.setUInterface((new UInterfaceFactory()).create(TypeUInterface.git));
        developersTable.setEditable(false);
        configureDevelopersTable();
        showDevelopersInTeam();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                developersTable.requestFocus();
            }
        });

        // добавили listener`a
        developersTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    startDeveloperAnylyze();
                }
            }
        });
    }

    /**
     * Настраиваем колонки таблицы DevelopersTable
     */
    private void configureDevelopersTable() {

        // region << Инициализируем колонки таблицы
        // Имя
        developerNameClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("name"));

        // Фамилия
        developerFamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("surname"));


        // gitname
        developerGitName.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("gitname"));


        // gitemail
        developerGitEmail.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("gitemail"));

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

            ObservableList<DeveloperTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String team_name = rs.getString(10) == null ? "" : rs.getString(10);
                data.add(new DeveloperTable(rs.getInt(1),
                        rs.getString(3),
                        rs.getString(2),
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


    // TODO  в будущем
    public void showChartOnImageView() {
      /*  DiffChart diffChart = getuInterface().getChart();
        WritableImage wimg = new WritableImage(img.getWidth(), img.getHeight());
        SwingFXUtils.toFXImage(img, wimg);
        imageView.setImage(wimg);*/
    }

    /**
     * Кнопка выбора директории
     */
    @Override
    public void chooseProjectAction() {
        directoryChooser = new DirectoryChooser();
        projectDir = directoryChooser.showDialog(null);
        //выход если ничего не выбрали
        if (projectDir == null) {
            tbProject.setText("");
            return;
        }
        if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
            ShowAlert("Ошибка", "Выбрана неверная директория!");
            return;
        }
        setNewRepDirectory(projectDir);

    }

    public void setNewRepDirectory(File file) {
        projectDir = file;
        if (projectDir != null) {
            tbProject.setText(file.getAbsolutePath());
        } else {
            tbProject.setText("");
        }
    }

    /**
     * Кнопка начала анализа
     */
    @Override
    public void start() {
        projectDir = new File(tbProject.getText());
        if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
            ShowAlert("Ошибка", "Выбрана неверная директория!");
            this.setStart(false);
            return;
        }
        if (!getuInterface().startProjectAnalyze()) {
            ShowAlert("Ошибка", "Ошибка анализа файлов проекта!");
            this.setStart(false);
        } else {
            this.setStart(true);
        }

        //showChartOnImageView();
        showMainInf();
        showAvtors();
        showAllFiles();
    }

    public LocalDate start;
    public LocalDate end;
    public ArrayList<ProjectTable> projects;

    /**
     * Кнопка начала анализа команд
     */
    public void startDeveloperAnylyze() {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка", "Сначала выберите Разработчика!");
            return;
        }

        start = end = null;
        projects = null;

        // Открываем projectForDeveloperView
        openProjectForDeveloperView();
        if (start == null || end == null || projects == null) {
            //выход т.к не с чем работать
            return;
        }

        // начинаем анализ

        // here runs the JavaFX thread
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() throws GitAPIException {
                // do your operation in here
                if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
                    Utils.showAlert("Ошибка", "Выбрана неверная директория!");
                    setStart(false);
                    return false;
                }
                if (!getuInterface().startProjectAnalyze()) {
                    Utils.showAlert("Ошибка", "Ошибка анализа файлов проекта!");
                    setStart(false);
                } else {
                    setStart(true);
                }

                //выводим данные о репозитории в поток javafx
                Platform.runLater(() -> {
                    showMainInf();
                    showAvtors();
                    showAllFiles();
                    //showCommitsChart();
                    closeRepository();
                });


                return true;
            }
        };

        task.setOnRunning((e) -> Utils.openLoadingWindow());
        task.setOnSucceeded((e) -> {
            Utils.closeLoadingWindow();
            // process return value again in JavaFX thread
        });
        task.setOnFailed((e) -> {
            // eventual error handling by catching exceptions from task.get()
            LOGGER.error(task.getException().getMessage());
            Utils.showAlert("Ошибка", "Один из репозиториев не существует," +
                    " либо у вас отсутствует подключение к интернету");
            Utils.closeLoadingWindow();
        });
        new Thread(task).start();
    }

    /**
     * Окно для выбора промежутка времени и проектов
     */
    private void openProjectForDeveloperView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/stats/projectForDeveloperView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Выбор проектов");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            ProjectForDeveloperController controller = loader.getController();
            controller.setStatsDeveloperController(this);
            controller.setId_developer(((DeveloperTable) developersTable.getSelectionModel().getSelectedItem()).getId());
            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void closeRepository() {
        this.getuInterface().closeRepository();
    }

    @Override
    public void showAllFiles() {
        clmnPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        clmnIsBinary.setCellValueFactory(new PropertyValueFactory<>("isBinary"));
        clmnLOC.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));

        TableModel allFiles = getuInterface().getAllFiles();
        ObservableList<FilesTable> data = FXCollections.observableArrayList();
        for (int i = 0; i < allFiles.getRowCount(); i++) {
            data.add(new FilesTable((String) (allFiles.getValueAt(i, 0)),
                    (String) allFiles.getValueAt(i, 1),
                    (String) allFiles.getValueAt(i, 2)));
        }

        tableAllFiles.setItems(data);
    }

    @Override
    public void showAvtors() {
        clmnAvtorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnCommitCount.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAdd.setCellValueFactory(new PropertyValueFactory<>("linesAdded"));
        clmnLinesDelete.setCellValueFactory(new PropertyValueFactory<>("linesRemoved"));
        clmnNetContribution.setCellValueFactory(new PropertyValueFactory<>("netContribution"));
        clmnAvtorEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableModel authors = getuInterface().getAuthors();
        ObservableList<AuthorTable> data = FXCollections.observableArrayList();
        for (int i = 0; i < authors.getRowCount(); i++) {
            data.add(new AuthorTable((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
                    (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4),
                    (String) (authors.getValueAt(i, 5))));
        }

        avtorTable.setItems(data);
    }

    @Override
    public void showMainInf() {
        lbNameRep.setText(getuInterface().getRepName());
        lbNazv.setText("Название репозитория:");
        lbNazvCur.setText(getuInterface().getRepName());
        lbBranch.setText("Название ветки:");
        lbBranchCur.setText(getuInterface().getBranchName());
        lbKolCommit.setText("Всего кол-во коммитов:");
        lbKolCommitCur.setText(Integer.toString(getuInterface().getNumberOfCommits()));
        //lbRemoteName.setText(getuInterface().getRemoteName());
    }


    /**
     * Отображаем Окно с ошибкой
     *
     * @param title - заголовок
     * @param text  - текст сообщения
     */
    private void ShowAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("icons/errorIcon.png").toString()));
        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.setContentText(text);
        alert.showAndWait();

    }


    /**
     * Показать коммиты автора
     *
     * @param event - событие
     */
    public void ShowCommitsButtonAction(ActionEvent event) {
        if (isStart()) {
            AuthorTable tableAuthor = (AuthorTable) avtorTable.getSelectionModel().getSelectedItem();
            Author selectedAuthor = getuInterface().getAuthorByName(tableAuthor.getName());
            if (selectedAuthor == null) {
                ShowAlert("Внимание", "Вы не выбрали автора!");
                return;
            }

            try {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/stats/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Коммиты");
                stage.setScene(new Scene(rootLayout, 480, 370));
                stage.getIcons().add(new Image("icons/gitIcon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);

                //Инициализируем
                CommitsController controller = loader.getController();
                controller.setAuthor(selectedAuthor);
                controller.setUInterface(this.getuInterface());
                controller.setLbName(selectedAuthor.getName());
                controller.showCommits();
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

