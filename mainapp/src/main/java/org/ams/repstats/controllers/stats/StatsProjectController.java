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
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.gitapiwrapper.GitApi;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.AuthorTable;
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.utils.RepositoryDownloader;
import org.ams.repstats.utils.Utils;
import org.ams.repstats.view.ViewInterfaceAbstract;
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
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 30.04.2017
 * Time: 13:56
 */
public class StatsProjectController extends ViewInterfaceAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsProjectController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    public Label lbRepCount;
    @FXML
    private Label lbMainInfTitle;
    @FXML
    private Label lbTeamName;
    @FXML
    private Label lbDevelopersCount;
    @FXML
    private Label lbCountStrokAdd;
    @FXML
    private Label lbCountStrokRem;
    @FXML
    private Label lbTeamPokr;
    @FXML
    private Label lbMergedPullReq;
    @FXML
    private Label lbFixBugs;
    @FXML
    private Label lbCommitCount;
    @FXML
    private TableView repositoryTable;
    @FXML
    private TableColumn clmnUrlRep;
    @FXML
    private TableColumn clmnCommitCountRep;
    @FXML
    private TableColumn clmnLinesAddRep;
    @FXML
    private TableColumn clmnLinesDeleteRep;
    @FXML
    private TableColumn clmnNetContributionRep;
    @FXML
    private TableView projectsTable;
    @FXML
    private TableColumn projectNameClmn;
    @FXML
    private TableColumn projectPriorClmn;
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
    private Button btStart;
    //endregion

    private DirectoryChooser directoryChooser;
    private File projectDir;
    private Task<Boolean> task;

    private LineChart<Date, Number> dateLineChart;
    private LineChart<String, Number> numberLineChart;
    private GitApi gitApi = new GitApi();
    private int mergeCount;
    private int mergedOtherPullRequests;
    private int notMergedOtherPullRequests;
    private int bugFixes;
    private long authorLinesAffected;
    private long totalLinesAffected;


    @FXML
    public void initialize() {
        this.setUInterface((new UInterfaceFactory()).create(TypeUInterface.git));

        projectsTable.setEditable(false);
        configureAndShowProjectsClmn();
        Platform.runLater(() -> projectsTable.requestFocus());

        // Крепим свой placeholder
        Utils.setEmptyTableMessage(projectsTable);
        Utils.setEmptyTableMessage(repositoryTable);
        Utils.setEmptyTableMessage(avtorTable);

        // добавили listener`a
        projectsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    start();
                }
            }
        });

        // добавили listener`a
        avtorTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    ShowCommitsAvtorButtonAction(null);
                }
            }
        });

        // добавили listener`a
        repositoryTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    ShowCommitsInRepositorytButtonAction(null);
                }
            }
        });

        configureRepositoryTable();
        configureAuthorTable();
    }

    private void configureAndShowProjectsClmn() {

        ObservableList<ProjectTable> data = FXCollections.observableArrayList();
        // Название
        projectNameClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, String>("name"));

        // Приоритет
        projectPriorClmn.setCellValueFactory(new PropertyValueFactory<ProjectTable, Integer>("prior"));

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

    private void configureRepositoryTable() {
        clmnUrlRep.setCellValueFactory(new PropertyValueFactory<>("url"));
        clmnCommitCountRep.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAddRep.setCellValueFactory(new PropertyValueFactory<>("linesAdd"));
        clmnLinesDeleteRep.setCellValueFactory(new PropertyValueFactory<>("linesDelete"));
        clmnNetContributionRep.setCellValueFactory(new PropertyValueFactory<>("netContribution"));
    }

    private void configureAuthorTable() {
        clmnAvtorName.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        clmnAvtorEmail.setCellValueFactory(new PropertyValueFactory<>("gitemail"));
        clmnCommitCount.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAdd.setCellValueFactory(new PropertyValueFactory<>("linesAdded"));
        clmnLinesDelete.setCellValueFactory(new PropertyValueFactory<>("linesRemoved"));
        clmnNetContribution.setCellValueFactory(new PropertyValueFactory<>("netContribution"));
    }

    private void setNewRepDirectory(File file) {
        projectDir = file;
    }


    public LocalDate start;
    public LocalDate end;
    public ObservableList<DeveloperTable> developers;
    public int allCommits = 0;
    public int linesAdd = 0;
    public int linesDel = 0;

    public long totalLines = 0;
    ProjectTable selectedProject;
    Author selectedAuthor;

    /**
     * Кнопка начала анализа команд
     */
    @Override
    public void start() {
        if (projectsTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка", "Сначала выберите проект!");
            return;
        }

        // Инициализация
        selectedProject = (ProjectTable) projectsTable.getSelectionModel().getSelectedItem();

        start = end = null;
        allCommits = 0;
        linesAdd = 0;
        linesDel = 0;
        totalLines = 0;
        mergeCount = 0;
        authorLinesAffected = 0;
        totalLinesAffected = 0;

        // выбираем промежутток времени
        openDateForTeamrView();
        // Проверка на нажатия "Отмена"
        if (start == null || end == null) {
            //выход т.к не с чем работать
            return;
        }
        startMainTask();
    }


    /**
     * Основной метод анализа
     */
    private void startMainTask() {
        // начинаем анализ
        task = new Task<Boolean>() {


            @Override
            public Boolean call() throws GitAPIException {

                // извлекаем все репозитории проекта
                // извлекаем url всех репозиториев проекта и кладём их в projects
                try {
                    PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectAllUrlFromProject);
                    preparedStatement.setString(1, Integer.toString(selectedProject.getId()));
                    ResultSet rs = MysqlConnector.executeQuery();
                    while (rs.next()) {
                        selectedProject.getUrls().add(rs.getString(2));
                    }
                    MysqlConnector.closeStmt();

                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }

                if (selectedProject.getUrls().size() == 0) {
                    Platform.runLater(() -> {
                        Utils.showAlert("Ошибка", "В выбранном проекте нет репозиториев.");
                    });
                    return true;
                }

                // извлекаем всех разработчиков выбранной команды
                try {
                    PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectDevelopersFromProject);
                    preparedStatement.setString(1, Integer.toString(selectedProject.getId()));
                    ResultSet rs = MysqlConnector.executeQuery();

                    developers = FXCollections.observableArrayList();
                    while (rs.next()) {
                        developers.add(new DeveloperTable(rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                0, 0, 0, 0));
                    }
                    MysqlConnector.closeStmt();

                    avtorTable.setItems(developers);
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }

                if (developers.size() == 0) {
                    Platform.runLater(() -> {
                        Utils.showAlert("Ошибка", "К выбранному проекту не прикреплены разработчики!");
                    });
                    return true;
                }


                ObservableList<RepositoryTable> repositoryData = FXCollections.observableArrayList();
                RepositoryTable newRepositoryTable = null;

                for (String url : selectedProject.getUrls()) {
                    try {
                        closeRepository();
                        File destinationFile = RepositoryDownloader.downloadRepoContent(url, "master");
                        setNewRepDirectory(destinationFile);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> Utils.showAlert("Ошибка", "Ошибка загрузки репозитория!"));
                    }
                    // проверяем и начинаем анализ
                    if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
                        Platform.runLater(() -> Utils.showAlert("Ошибка", "Выбрана неверная директория!"));
                        setStart(false);
                        return false;
                    }
                    if (!getuInterface().startProjectAnalyze(start, end)) {
                        Platform.runLater(() -> Utils.showAlert("Ошибка", "Ошибка анализа файлов проекта!"));
                        setStart(false);
                        return false;
                    } else {
                        setStart(true);
                    }


                    newRepositoryTable = new RepositoryTable(url, null);

                    //сохраняем данные о репозитории
                    TableModel authors = getuInterface().getAuthors();
                    ObservableList<AuthorTable> data = FXCollections.observableArrayList();
                    for (int i = 0; i < authors.getRowCount(); i++) {
                        data.add(new AuthorTable((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
                                (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4),
                                (String) (authors.getValueAt(i, 5))));

                        for (DeveloperTable developerTable : developers) {
                            // автор совпал
                            if (data.get(i).getEmail().equals(developerTable.getGitemail())) {
                                newRepositoryTable.addCommitCount(data.get(i).getCommitCount());
                                newRepositoryTable.addLinesAdd(data.get(i).getLinesAdded());
                                newRepositoryTable.addLinesDelete(data.get(i).getLinesRemoved());

                                developerTable.addCommitCount(data.get(i).getCommitCount());
                                developerTable.addLinesAdd(data.get(i).getLinesAdded());
                                developerTable.addLinesDelete(data.get(i).getLinesRemoved());
                                developerTable.addNetContributiont(developerTable.getLinesAdded() - developerTable.getLinesRemoved());

                                Author author = getuInterface().getAuthorByName(data.get(i).getName());
                                newRepositoryTable.setCommits(getuInterface().getLastCommits(author));
                                developerTable.getCommits().addAll(newRepositoryTable.getCommits());
                            }
                        }
                    }

                    // подсчитывае покрытие
                    authorLinesAffected += newRepositoryTable.getLinesAdd() + newRepositoryTable.getLinesDelete();
                    totalLinesAffected += getuInterface().getTotalLinesAddedAll() + getuInterface().getTotalLinesRemovedAll();

                    newRepositoryTable.addNetContributiont(newRepositoryTable.getLinesAdd() - newRepositoryTable.getLinesDelete());

                    allCommits += newRepositoryTable.getCommitCount();
                    linesAdd += newRepositoryTable.getLinesAdd();
                    linesDel += newRepositoryTable.getLinesDelete();

                    totalLines += getuInterface().getTotalNumberOfLines();
                    repositoryData.add(newRepositoryTable);

                    // запоминаем  графики
                    //rememberGraphics(projectTable, newRepositoryTable);

                    //запоминаем кол-во merged pull`ов
                    calcMergeCount(url);

                    // DeveloperTable developerTable = (DeveloperTable) developersTable.getSelectionModel().getSelectedItem();
                    // Author selectedAuthor = getuInterface().getAuthorByEmail(developerTable.getGitemail());
                    bugFixes += getuInterface().getBugFixesCount();
                }

                repositoryTable.setItems(repositoryData);
                return true;
            }
        }

        ;

        task.setOnRunning((e) -> Utils.openLoadingWindow(task));
        task.setOnSucceeded((e) ->
        {
            Utils.closeLoadingWindow();
            // process return value again in JavaFX thread
            //выводим данные о репозитории в поток javafx
            Platform.runLater(() -> {
                showMainInf();
                showAvtors();
                showAllFiles();
                //   buildCommitsCountGraph(null);
                //    buildCommitsbyTimeGraph(null);
                //showCommitsChart();
                closeRepository();
            });
        });

        task.setOnFailed((e) ->
        {
            // eventual error handling by catching exceptions from task.get()
            task.getException().printStackTrace();
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
    private void openDateForTeamrView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/stats/dateForTeamView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Выбор промежутка времени");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            DateForTeamController dateForTeamController = loader.getController();
            dateForTeamController.setStatsProjectController(this);
            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /*
        private void rememberGraphics(ProjectTable projectTable, RepositoryTable newRepositoryTable) {
            // Извлекаем выбранного пользователя
            DeveloperTable developerTable = (DeveloperTable) developersTable.getSelectionModel().getSelectedItem();
            Author selectedAuthor = this.getuInterface().getAuthorByEmail(developerTable.getGitemail());
            ArrayList<Author> allAvtors = new ArrayList<Author>();
            allAvtors.addAll(this.getuInterface().getAllAuthors());
            if (selectedAuthor != null) {

                ArrayList<Author> allAvtorsEnd = new ArrayList<Author>();

                // Записываем нужных авторов
                for (Author author : allAvtors) {
                    if (author.getEmailAddress().equals(selectedAuthor.getEmailAddress())) {
                        allAvtorsEnd.add(author);
                    }
                }

                // Извлекаем коммиты
                HashMap<Author, ArrayList<Integer>> authorCommitsByWeek = this.getuInterface().getCommitsByWeek(allAvtorsEnd);
                HashMap<Author, ArrayList<Integer>> authorByDaysInCurMonth = this.getuInterface().getCommitsByDaysInCurMonth(allAvtorsEnd);
                HashMap<Author, ArrayList<Integer>> commitsByMonths = this.getuInterface().getCommitsByMonths(allAvtorsEnd);
                HashMap<Author, HashMap<LocalDate, Integer>> commitsByCustomDate = this.getuInterface().getCommitsByCustomDate(allAvtorsEnd);

                HashMap<Author, ArrayList<Integer>> authorCommitsByTime = this.getuInterface().getCommitsByTime(allAvtorsEnd);

                // Запомнили
                for (Author author : allAvtorsEnd) {
                    projectTable.addCommitsByWeek(authorCommitsByWeek.get(author));
                    projectTable.addCommitsByDaysInCurMonth(authorByDaysInCurMonth.get(author));
                    projectTable.addCommitsByMonths(commitsByMonths.get(author));
                    projectTable.addCommitsByCustomDate(commitsByCustomDate.get(author));
                    projectTable.addCommitsByTime(authorCommitsByTime.get(author));
                }
            }

        }
    */
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

        lbMainInfTitle.setText("Анализ проекта с " + formattedStartDateTime + " по " + formattedEndDateTime);

        lbTeamName.setText(selectedProject.getName());
        //lbGitNameEmail.setText(cur.getGitname() + "<" + cur.getGitemail() + ">");

        //
        lbDevelopersCount.setText(Integer.toString(developers.size()));
        lbRepCount.setText(Integer.toString(repositoryTable.getItems().size()));
        lbCommitCount.setText(Integer.toString(allCommits));
        lbCountStrokAdd.setText(Integer.toString(linesAdd));
        lbCountStrokRem.setText(Integer.toString(linesDel));
        double vklad = Math.abs(Math.ceil((((double) authorLinesAffected) / totalLinesAffected) * 100));
        if (vklad > 100) {
            vklad = 100;
        }
    /*   if (vklad == 0) {
            vklad = Math.abs(Math.ceil((((double) linesAdd) / totalLines) * 100));
        }*/
        lbTeamPokr.setText(String.valueOf(vklad) + "%");
        lbMergedPullReq.setText(String.valueOf(this.mergeCount));
        //  lbOtherMergedPullReq.setText(String.valueOf(this.mergedOtherPullRequests));
        //  lbOtherNotMergedPullReq.setText(String.valueOf(this.notMergedOtherPullRequests));
        lbFixBugs.setText(String.valueOf(this.bugFixes));
    }


    private int calcMergeCount(String url) {
        this.gitApi = new GitApi();
        String[] tmp = url.split("/");
        String repos = tmp[tmp.length - 1];
        String owner = tmp[tmp.length - 2];
        try {
            this.mergeCount += gitApi.countUserMergePullRequests(repos, owner);
            //  this.mergedOtherPullRequests = gitApi.countMergedOtherPullRequests(repos, owner, gitname);
            //  this.notMergedOtherPullRequests = gitApi.countNotMergedOtherPullRequests(repos, owner, gitname);
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Utils.showAlert("Ошибка", "Невозможно подтянуть \"pull requests\"!");
            });
        }
        return 0;
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


    /*    TableModel authors = getuInterface().getAuthors();
        ObservableList<AuthorTable> data = FXCollections.observableArrayList();
        for (int i = 0; i < authors.getRowCount(); i++) {
            data.add(new AuthorTable((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
                    (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4),
                    (String) (authors.getValueAt(i, 5))));
        }

        avtorTable.setItems(data);*/
    }


    /**
     * Показать коммиты разработчика в репозитории
     *
     * @param event
     */
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
                //controller.setLbName(selectedDeveloper.getGitname());
                controller.showCommits();
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowCommitsAvtorButtonAction(ActionEvent event) {
        if (isStart()) {
            DeveloperTable developerTable = (DeveloperTable) avtorTable.getSelectionModel().getSelectedItem();
            if (developerTable == null) {
                Utils.showAlert("Внимание", "Вы не выбрали автора!");
                return;
            }

            try {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/stats/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Коммиты");
                stage.setScene(new Scene(rootLayout));
                stage.getIcons().add(new Image("icons/gitIcon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);

                //Инициализируем
                CommitsController controller = loader.getController();
                controller.setAuthor(selectedAuthor);
                controller.setUInterface(this.getuInterface());
                //controller.setLbName(selectedAuthor.getAuthor());
                controller.setDeveloperTable(developerTable);
                controller.showCommits();
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Строит график коммитов
     *
     * @param event - событие
     */
  /*  public void buildCommitsCountGraph(ActionEvent event) {
        if (isStart()) {
            ArrayList<Author> allAvtors = new ArrayList<Author>();
            allAvtors.addAll(getuInterface().getAllAuthors());

            // Лочим датапикеры
            graphDatePickerStart.setDisable(true);
            graphDatePickerEnd.setDisable(true);

            // Освобождаем имеющиеся графики
            if (numberLineChart != null) {
                numberLineChart.setVisible(false);
                GraphAnchor.getChildren().remove(numberLineChart);
                numberLineChart = null;
            }
            if (dateLineChart != null) {
                dateLineChart.setVisible(false);
                GraphAnchor.getChildren().remove(dateLineChart);
                dateLineChart = null;
            }

            String selected = (String) comboGraph.getSelectionModel().getSelectedItem();

            if (selected.equals(LineChartCreator.options.get(0))) {

                numberLineChart = LineChartCreator.createNumberLineChart(selected, projectTable.getItems());

                AnchorPane.setLeftAnchor(numberLineChart, 0.0);
                AnchorPane.setTopAnchor(numberLineChart, 0.0);
                AnchorPane.setRightAnchor(numberLineChart, 0.0);
                AnchorPane.setBottomAnchor(numberLineChart, 53.0);
                GraphAnchor.getChildren().add(numberLineChart);

            } else if (selected.equals(LineChartCreator.options.get(1))) {
                numberLineChart = LineChartCreator.createNumberLineChart(selected, projectTable.getItems());

                AnchorPane.setLeftAnchor(numberLineChart, 0.0);
                AnchorPane.setTopAnchor(numberLineChart, 0.0);
                AnchorPane.setRightAnchor(numberLineChart, 0.0);
                AnchorPane.setBottomAnchor(numberLineChart, 53.0);
                GraphAnchor.getChildren().add(numberLineChart);

            } else if (selected.equals(LineChartCreator.options.get(2))) {
                numberLineChart = LineChartCreator.createNumberLineChart(selected, projectTable.getItems());

                AnchorPane.setLeftAnchor(numberLineChart, 0.0);
                AnchorPane.setTopAnchor(numberLineChart, 0.0);
                AnchorPane.setRightAnchor(numberLineChart, 0.0);
                AnchorPane.setBottomAnchor(numberLineChart, 53.0);
                GraphAnchor.getChildren().add(numberLineChart);

            } else if (selected.equals(LineChartCreator.options.get(3))) {
                graphDatePickerStart.setDisable(false);
                graphDatePickerEnd.setDisable(false);

                if (graphDatePickerStart.getValue() == null || graphDatePickerEnd.getValue() == null) {
                    return;
                }
                if (graphDatePickerStart.getValue().isAfter(graphDatePickerEnd.getValue())) {
                    Utils.showAlert("Ошибка ввода дат", "Начальная дата должна быть раньше конечной!");
                    return;
                }


                dateLineChart = LineChartCreator.createDateLineChart(selected, projectTable.getItems(),
                        graphDatePickerStart.getValue(), graphDatePickerEnd.getValue());
                AnchorPane.setLeftAnchor(dateLineChart, 0.0);
                AnchorPane.setTopAnchor(dateLineChart, 0.0);
                AnchorPane.setRightAnchor(dateLineChart, 0.0);
                AnchorPane.setBottomAnchor(dateLineChart, 53.0);
                GraphAnchor.getChildren().add(dateLineChart);

            }
        }
    }
*/
    /**
     * Строит график времени коммитов
     *
     * @param event - событие
     */
 /*   public void buildCommitsbyTimeGraph(ActionEvent event) {
        if (isStart()) {
            commitsByTimeChart.getData().clear();
            commitsByTimeChart.setTitle("Коммиты по времени");

            for (Object project : projectTable.getItems()) {
                ProjectTable selectedProject = (ProjectTable) project;
                ArrayList<Integer> commitsByTime = selectedProject.getCommitsByTime();
                XYChart.Series authorSeries = new XYChart.Series();

                for (int i = 0; i < commitsByTime.size(); i++) {
                    authorSeries.getData().add(new XYChart.Data(String.format("%02d", i) + ":00", commitsByTime.get(i)));
                }

                authorSeries.setName(selectedProject.getName());
                commitsByTimeChart.getData().add(authorSeries);
            }


        }
    }*/
}


