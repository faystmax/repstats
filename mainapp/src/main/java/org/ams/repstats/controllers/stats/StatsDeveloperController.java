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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.AuthorTable;
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.utils.Utils;
import org.ams.repstats.view.ViewInterfaceAbstract;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

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
    public Label lbMainInfTitle;
    @FXML
    public Label lbFIO;
    @FXML
    public Label lbGitNameEmail;
    @FXML
    public Label lbKolStrokAdd;
    @FXML
    public Label lbKolStrokDel;
    @FXML
    public Label lbPokr;
    @FXML
    public TableView projectTable;
    @FXML
    public TableColumn clmnProject;
    @FXML
    public TableColumn clmnCommitCountProj;
    @FXML
    public TableColumn clmnLinesAddProj;
    @FXML
    public TableColumn clmnLinesDeleteProj;
    @FXML
    public TableColumn clmnNetContributionProj;
    @FXML
    public TableView repositoryTable;
    @FXML
    public TableColumn clmnUrlRep;
    @FXML
    public TableColumn clmnCommitCountRep;
    @FXML
    public TableColumn clmnLinesAddRep;
    @FXML
    public TableColumn clmnLinesDeleteRep;
    @FXML
    public TableColumn clmnNetContributionRep;
    @FXML
    private TableView developersTable;
    @FXML
    private TableColumn developerFamClmn;
    @FXML
    private TableColumn developerNameClmn;
    @FXML
    private TableColumn developerGitEmail;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private Button btShowCommits;
    @FXML
    private Label lbKolCommit;
    @FXML
    private Button btStart;
    //endregion

    private DirectoryChooser directoryChooser;
    private File projectDir;


    @FXML
    public void initialize() {
        this.setUInterface((new UInterfaceFactory()).create(TypeUInterface.git));
        developersTable.setEditable(false);
        configureDevelopersTable();
        showDevelopersWithTeamName();
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
                    start();
                }
            }
        });
        configureProjectsTable();
        configureRepositoryTable();
    }

    /**
     * Настраиваем колонки таблицы DevelopersTable
     */
    private void configureDevelopersTable() {
        // Имя
        developerNameClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("name"));

        // Фамилия
        developerFamClmn.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("surname"));

        // gitname
        //developerGitName.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("gitname"));

        // gitemail
        developerGitEmail.setCellValueFactory(new PropertyValueFactory<DeveloperTable, String>("gitemail"));
    }

    private void configureProjectsTable() {
        clmnProject.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnCommitCountProj.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAddProj.setCellValueFactory(new PropertyValueFactory<>("linesAdd"));
        clmnLinesDeleteProj.setCellValueFactory(new PropertyValueFactory<>("linesDelete"));
        clmnNetContributionProj.setCellValueFactory(new PropertyValueFactory<>("netContribution"));
    }

    private void configureRepositoryTable() {
        clmnUrlRep.setCellValueFactory(new PropertyValueFactory<>("url"));
        clmnCommitCountRep.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAddRep.setCellValueFactory(new PropertyValueFactory<>("linesAdd"));
        clmnLinesDeleteRep.setCellValueFactory(new PropertyValueFactory<>("linesDelete"));
        clmnNetContributionRep.setCellValueFactory(new PropertyValueFactory<>("netContribution"));
    }

    /**
     * Заполняем таблицу с разработчиками
     */
    private void showDevelopersWithTeamName() {

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllDevelopersWithTeamName);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<DeveloperTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String team_name = rs.getString(10) == null ? "" : rs.getString(10);
                data.add(new DeveloperTable(rs.getInt(1),
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


    private void setNewRepDirectory(File file) {
        projectDir = file;
    }

    public LocalDate start;
    public LocalDate end;
    public ArrayList<ProjectTable> projects;
    public int allCommits = 0;
    public int linesAdd = 0;
    public int linesDel = 0;

    public long totalLines = 0;
    DeveloperTable selectedDeveloper;
    Author selectedAuthor;

    /**
     * Кнопка начала анализа разработчиков
     */
    @Override
    public void start() {
        if (developersTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка", "Сначала выберите Разработчика!");
            return;
        }

        // Инициализация
        selectedDeveloper = (DeveloperTable) developersTable.getSelectionModel().getSelectedItem();
        start = end = null;
        projects = null;
        allCommits = 0;
        linesAdd = 0;
        linesDel = 0;
        totalLines = 0;

        // Открываем projectForDeveloperView
        openProjectForDeveloperView();
        // Проверка на нажатия "Отмена"
        if (start == null || end == null || projects == null) {
            //выход т.к не с чем работать
            return;
        }

        // начинаем анализ
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() throws GitAPIException {

                //извлекаем url всех репозиториев проекта и кладём их в projects
                try {
                    for (ProjectTable projectTable : projects) {
                        PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllUrlFromProject);
                        preparedStatement.setString(1, Integer.toString(projectTable.getId()));
                        ResultSet rs = MysqlConnector.executeQuery();

                        while (rs.next()) {
                            projectTable.getUrls().add(rs.getString(2));
                        }
                        MysqlConnector.closeStmt();
                    }
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }


                ObservableList<ProjectTable> projectsData = FXCollections.observableArrayList();
                ObservableList<RepositoryTable> repositoryData = FXCollections.observableArrayList();
                projectsData.addAll(projects);


                //основной цикл по проектам и репозиториям
                for (ProjectTable projectTable : projects) {
                    int allCommitsInProject = 0;
                    int linesAddInProject = 0;
                    int linesDelInProject = 0;

                    int curCommits = 0;
                    int curlinesAdd = 0;
                    int curlinesDel = 0;
                    RepositoryTable newRepositoryTable = null;

                    for (String url : projectTable.getUrls()) {
                        downloadRepository(url);
                        // проверяем и начинаем анализ
                        if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
                            Utils.showAlert("Ошибка", "Выбрана неверная директория!");
                            setStart(false);
                            return false;
                        }
                        if (!getuInterface().startProjectAnalyze(start, end)) {
                            Utils.showAlert("Ошибка", "Ошибка анализа файлов проекта!");
                            setStart(false);
                            return false;
                        } else {
                            setStart(true);
                        }

                        curCommits = 0;
                        curlinesAdd = 0;
                        curlinesDel = 0;
                        selectedAuthor = getuInterface().getAuthorByEmail(selectedDeveloper.getGitemail());

                        //сохраняем данные о репозитории
                        DeveloperTable cur = ((DeveloperTable) developersTable.getSelectionModel().getSelectedItem());
                        TableModel authors = getuInterface().getAuthors();
                        ObservableList<AuthorTable> data = FXCollections.observableArrayList();
                        for (int i = 0; i < authors.getRowCount(); i++) {
                            data.add(new AuthorTable((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
                                    (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4),
                                    (String) (authors.getValueAt(i, 5))));

                            //автор совпал
                            if (Objects.equals(data.get(i).getEmail(), cur.getGitemail()) /*&& Objects.equals(data.get(i).getName(), cur.getGitname()*/) {

                                curCommits += data.get(i).getCommitCount();
                                curlinesAdd += data.get(i).getLinesAdded();
                                curlinesDel += data.get(i).getLinesRemoved();

                            }
                        }

                        allCommitsInProject += curCommits;
                        linesAddInProject += curlinesAdd;
                        linesDelInProject += curlinesDel;

                        totalLines += getuInterface().getTotalNumberOfLines();
                        newRepositoryTable = new RepositoryTable(url, curCommits, curlinesAdd, curlinesDel, curlinesAdd - curlinesDel);
                        repositoryData.add(newRepositoryTable);
                        if (selectedAuthor != null) {
                            newRepositoryTable.setCommits(getuInterface().getLastCommits(selectedAuthor));
                            projectTable.getCommits().addAll(newRepositoryTable.getCommits());
                        }
                    }


                    allCommits += allCommitsInProject;
                    linesAdd += linesAddInProject;
                    linesDel += linesDelInProject;


                    int finalAllCommitsInProject = allCommitsInProject;
                    int finalLinesAddInProject = linesAddInProject;
                    int finalLinesDelInProject = linesDelInProject;
                    Platform.runLater(() -> {
                        //сохраняем данные о проекте
                        projectTable.setCommitCount(finalAllCommitsInProject);
                        projectTable.setLinesAdd(finalLinesAddInProject);
                        projectTable.setLinesDelete(finalLinesDelInProject);
                        projectTable.setNetContribution(finalLinesAddInProject - finalLinesDelInProject);

                    });
                }

                //выводим данные о репозитории в поток javafx
                Platform.runLater(() -> {
                    projectTable.setItems(projectsData);
                    repositoryTable.setItems(repositoryData);
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
            task.getException().printStackTrace();
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
            controller.configureAndShowProjectsClmn();
            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Загрузка репозитория
     *
     * @param url репозитория
     * @throws GitAPIException
     */
    private void downloadRepository(String url) throws GitAPIException {
        File dir = new File("./cloneRep");
        if (dir.exists()) {
            setNewRepDirectory(null);
            Platform.runLater(this::closeRepository);
            closeRepository();
            Utils.deleteRecursive(dir);

            Git git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File("./cloneRep"))
                    .call();
            git.getRepository().close();
            projectDir = dir;
        }
        Platform.runLater(() -> {
            projectDir = dir;
        });
    }

    @Override
    public void closeRepository() {
        this.getuInterface().closeRepository();
    }

    @Override
    public void chooseProjectAction() {

    }

    @Override
    public void showMainInf() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedStartDateTime = start.format(formatter);
        String formattedEndDateTime = end.format(formatter);

        lbMainInfTitle.setText("Анализ разработчика с " + formattedStartDateTime + " по " + formattedEndDateTime);

        DeveloperTable cur = ((DeveloperTable) developersTable.getSelectionModel().getSelectedItem());
        lbFIO.setText(cur.getSurname() + " " + cur.getName() + " " + cur.getMiddlename());
        lbGitNameEmail.setText(cur.getGitname() + "<" + cur.getGitemail() + ">");

        //
        lbKolCommit.setText(Integer.toString(allCommits));
        lbKolStrokAdd.setText(Integer.toString(linesAdd));
        lbKolStrokDel.setText(Integer.toString(linesDel));
        lbPokr.setText(String.valueOf(Math.ceil((((double) linesAdd - linesDel) / totalLines) * 100)) + "%");
    }

    @Override
    public void showAllFiles() {
      /*  clmnPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        clmnIsBinary.setCellValueFactory(new PropertyValueFactory<>("isBinary"));
        clmnLOC.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));

        TableModel allFiles = getuInterface().getAllFiles();
        ObservableList<FilesTable> data = FXCollections.observableArrayList();
        for (int i = 0; i < allFiles.getRowCount(); i++) {
            data.add(new FilesTable((String) (allFiles.getValueAt(i, 0)),
                    (String) allFiles.getValueAt(i, 1),
                    (String) allFiles.getValueAt(i, 2)));
        }

        tableAllFiles.setItems(data);*/
    }

    @Override
    public void showAvtors() {
     /*   clmnAvtorName.setCellValueFactory(new PropertyValueFactory<>("name"));
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

        avtorTable.setItems(data);*/
    }


    /**
     * Показать коммиты разработчика
     *
     * @param event - событие
     */
    public void ShowCommitsInProjectButtonAction(ActionEvent event) {
        if (isStart()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/stats/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Коммиты по проекту");
                stage.setScene(new Scene(rootLayout));
                stage.getIcons().add(new Image("icons/gitIcon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);

                //Инициализируем
                CommitsController controller = loader.getController();
                controller.setAuthor(selectedAuthor);
                controller.setProjectTable((ProjectTable) projectTable.getSelectionModel().getSelectedItem());
                controller.setLbName(selectedDeveloper.getGitname());
                controller.showCommits();
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowCommitsInRepositorytButtonAction(ActionEvent event) {
        if (isStart()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/stats/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Коммиты по репозиторию");
                stage.setScene(new Scene(rootLayout));
                stage.getIcons().add(new Image("icons/gitIcon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);

                //Инициализируем
                CommitsController controller = loader.getController();
                controller.setAuthor(selectedAuthor);
                controller.setRepositoryTable((RepositoryTable) repositoryTable.getSelectionModel().getSelectedItem());
                controller.setLbName(selectedDeveloper.getGitname());
                controller.showCommits();
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
/*
    public CommitsController ShowCommits(String title) {
        if (isStart()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/stats/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle(title);
                stage.setScene(new Scene(rootLayout));
                stage.getIcons().add(new Image("icons/gitIcon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);

                //Инициализируем
                CommitsController controller = loader.getController();
                controller.setAuthor(selectedAuthor);
                //controller.setRepositoryTable((RepositoryTable) repositoryTable.getSelectionModel().getSelectedItem());
                controller.setLbName(selectedDeveloper.getGitname());
                controller.showCommits();
                stage.showAndWait();
                return controller;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */
}

