package org.ams.repstats.controllers.stats;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Branch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.ams.gitapiwrapper.GitApi;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.entity.*;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.utils.LineChartCreator;
import org.ams.repstats.utils.RepositoryDownloader;
import org.ams.repstats.utils.Utils;
import org.ams.repstats.view.ViewInterfaceAbstract;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 25.12.2016
 * Time: 16:00
 */
public class StatsRepositoryController extends ViewInterfaceAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsRepositoryController.class); ///< ссылка на логер


    //region << UI Компоненты
    @FXML
    private Label lbKolStrokAdd;
    @FXML
    private Label lbKolStrokDel;
    @FXML
    private TableColumn clmnPullAffectedFiles;
    @FXML
    private TableColumn clmnPullIsMerged;
    @FXML
    private TableView tableIssues;
    @FXML
    private TableColumn clmnIssuesNumber;
    @FXML
    private TableColumn clmnIssuesTitle;
    @FXML
    private TableColumn clmnIssuesAvtor;
    @FXML
    private TableColumn clmnIssuesDateCreated;
    @FXML
    private TableColumn clmnIssuesState;
    @FXML
    private TableView tablePullRequests;
    @FXML
    private TableColumn clmnPullNumber;
    @FXML
    private TableColumn clmnPullTitle;
    @FXML
    private TableColumn clmnPullAvtor;
    @FXML
    private TableColumn clmnPullDateCreated;
    @FXML
    private AnchorPane GraphAnchor;
    @FXML
    private DatePicker graphDatePickerStart;
    @FXML
    private DatePicker graphDatePickerEnd;
    @FXML
    private ComboBox comboGraph;
    @FXML
    private TableView repositoryTable;
    @FXML
    private TableColumn reposNameClmn;
    @FXML
    private TableColumn reposUrlClmn;
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
    private TableColumn clmnAvtorFIO;
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
    private Label lbNameRep;
    @FXML
    private Label lbNazvCur;
    @FXML
    private Label lbBranchCur;
    @FXML
    private Label lbKolCommitCur;
    @FXML
    private Label lbNew;
    @FXML
    private TextField tbProject;
    @FXML
    private Button btChoose;
    @FXML
    private Button btStart;
    @FXML
    private Label lbKolStrokCur;
    @FXML
    private TableView tableTecBranches;
    @FXML
    private TableColumn clmnTecName;
    @FXML
    private TableColumn clmnTecId;
    @FXML
    private TableView tableMergedBranches;
    @FXML
    private TableColumn clmnMergedName;
    @FXML
    private TableColumn clmnMergedId;
    //endregion

    private DirectoryChooser directoryChooser;
    private File projectDir;
    private String url;
    private Task<Boolean> task;

    private LineChart<Date, Number> dateLineChart;
    private LineChart<String, Number> numberLineChart;
    private List<PullRequest> pullRequests;
    private List<Issue> issues;
    private GitApi gitApi;

    @FXML
    public void initialize() {
        // По дефолту исп-ем гит
        this.setUInterface((new UInterfaceFactory()).create(TypeUInterface.git));

        // Отключаем редактирование таблицы
        repositoryTable.setEditable(false);

        // Крепим свой placeholder
        Utils.setEmptyTableMessage(avtorTable);
        Utils.setEmptyTableMessage(tableAllFiles);
        Utils.setEmptyTableMessage(tableIssues);
        Utils.setEmptyTableMessage(tablePullRequests);
        Utils.setEmptyTableMessage(repositoryTable);
        Utils.setEmptyTableMessage(repositoryTable);
        Utils.setEmptyTableMessage(tableTecBranches);
        Utils.setEmptyTableMessage(tableMergedBranches);

        // Отображаем репозитории
        configureRepositoryClmn();
        showRepository();

        Platform.runLater(() -> repositoryTable.requestFocus());

        // добавили listener`a
        repositoryTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    startRepositoryAnalyze();
                }
            }
        });

        // добавили listener`a
        avtorTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    ShowCommitsButtonAction(null);
                }
            }
        });

        btStart.defaultButtonProperty().bind(btStart.focusedProperty());

        // Заполняем  Combobox
        comboGraph.getItems().addAll(LineChartCreator.options);
        comboGraph.setValue(LineChartCreator.options.get(0));

    }

    private void configureRepositoryClmn() {
        repositoryTable.setEditable(true);
        // Название
        reposNameClmn.setCellValueFactory(new PropertyValueFactory<RepositoryObs, String>("name"));

        // Url
        reposUrlClmn.setCellValueFactory(new PropertyValueFactory<RepositoryObs, String>("url"));
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

    // TODO  в будущем
    public void showChartOnImageView() {
      /*  BufferedImage img = getuInterface().getChart();
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
            Utils.showAlert("Ошибка", "Выбрана неверная директория!");
            return;
        }
        setNewRepDirectory(projectDir);
        Platform.runLater(() -> btStart.requestFocus());

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
        task = new Task<Boolean>() {
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
                url = getuInterface().getUrl();
                calcIssuesAndPullRequests();

                //выводим данные о репозитории в потоку javafx
                Platform.runLater(() -> {

                    showMainInf();
                    showAvtors();
                    showAllFiles();
                    showAllBranches();
                    buildGraph(null);
                    showChartOnImageView();
                    closeRepository();
                });
                return true;
            }

        };

        task.setOnRunning((e) -> Utils.openLoadingWindow(task));
        task.setOnSucceeded((e) -> {
            Utils.closeLoadingWindow();
            // process return value again in JavaFX thread
        });
        task.setOnFailed((e) -> {
            // eventual error handling by catching exceptions from task.get()
            LOGGER.error(task.getException().getMessage());
            Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                    " либо у вас отсутствует подключение к интернету");
            Utils.closeLoadingWindow();
        });
        new Thread(task).start();
    }


    public LocalDate start;
    public LocalDate end;

    /**
     * Кнопка начала анализа Репозитория
     */
    public void startRepositoryAnalyze() {
        if (repositoryTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка", "Сначала выберите репозиторий!");
            return;
        }

        start = end = null;

        // выбираем промежутток времени
        openDateForTeamrView();
        // Проверка на нажатия "Отмена"
        if (start == null || end == null) {
            //выход т.к не с чем работать
            return;
        }


        // берём url
        url = ((RepositoryObs) (repositoryTable.getSelectionModel().getSelectedItem())).getUrl();


        // here runs the JavaFX thread
        // Boolean as generic parameter since you want to return it
        task = new Task<Boolean>() {
            @Override
            public Boolean call() throws GitAPIException {
                // do your operation in here
                try {
                    closeRepository();
                    File destinationFile = RepositoryDownloader.downloadRepoContent(url, "master");
                    setNewRepDirectory(destinationFile);

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlert("Ошибка", "Ошибка загрузки репозитория!");
                }

                if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath())) {
                    Utils.showAlert("Ошибка", "Выбрана неверная директория!");
                    setStart(false);
                    return false;
                }
                if (!getuInterface().startProjectAnalyze(start, end)) {
                    Utils.showAlert("Ошибка", "Ошибка анализа файлов проекта!");
                    setStart(false);
                } else {
                    setStart(true);
                }

                calcIssuesAndPullRequests();

                return true;
            }
        };

        task.setOnRunning((e) -> Utils.openLoadingWindow(task));
        task.setOnSucceeded((e) -> {

            //выводим данные о репозитории в потоку javafx
            Platform.runLater(() -> {
                showMainInf();
                showAvtors();
                showAllFiles();
                showAllBranches();
                buildGraph(null);

                showChartOnImageView();
                closeRepository();
            });

            Utils.closeLoadingWindow();
            // process return value again in JavaFX thread
        });
        task.setOnFailed((e) -> {
            // eventual error handling by catching exceptions from task.get
            task.getException().printStackTrace();
            LOGGER.error(task.getException().getMessage());
            Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
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
            dateForTeamController.setStatsRepositoryController(this);
            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void calcIssuesAndPullRequests() {
        this.gitApi = new GitApi();
        String[] tmp = url.split("/");
        String repos = tmp[tmp.length - 1];
        String owner = tmp[tmp.length - 2];
        try {
            gitApi.calcAllPullRequests(repos, owner);
            gitApi.calcAllIssues(repos, owner);
            showPullRequests();
            showIssues();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Utils.showAlert("Ошибка", "Невозможно поднятуть \"pull requests\" и \"issues\"");
            });
        }
    }


    /**
     * Отображает все pull requests
     */
    private void showPullRequests() {
        clmnPullNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        clmnPullTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        clmnPullAvtor.setCellValueFactory(new PropertyValueFactory<>("author"));

        clmnPullDateCreated.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<PullRequestObs, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<PullRequestObs, String> film) {
                        SimpleStringProperty property = new SimpleStringProperty();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        property.setValue(dateFormat.format(film.getValue().getCreatedAt()));
                        return property;
                    }
                });
        clmnPullAffectedFiles.setCellValueFactory(new PropertyValueFactory<>("changedFiles"));
        clmnPullIsMerged.setCellValueFactory(new PropertyValueFactory<>("isMerged"));

        List<PullRequest> pullRequests = gitApi.getPullRequests();
        ObservableList<PullRequestObs> data = FXCollections.observableArrayList();
        for (int i = 0; i < pullRequests.size(); i++) {
            String author = null;
            if (pullRequests.get(i).getHead().getUser() == null) {
                author = pullRequests.get(i).getBase().getUser().getLogin();
            } else {
                author = pullRequests.get(i).getHead().getUser().getLogin();
            }

            data.add(new PullRequestObs(pullRequests.get(i).getNumber(),
                    pullRequests.get(i).getTitle(),
                    author, pullRequests.get(i).getCreatedAt(),
                    pullRequests.get(i).getState(),
                    (pullRequests.get(i).isMerged() == true ? "да" : "нет"),
                    pullRequests.get(i).getChangedFiles(),
                    pullRequests.get(i).getAdditions(),
                    pullRequests.get(i).getDeletions()));
        }

        tablePullRequests.setItems(data);

    }

    /**
     * Отображает все issues
     */
    private void showIssues() {
        clmnIssuesNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        clmnIssuesTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        clmnIssuesAvtor.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnIssuesState.setCellValueFactory(new PropertyValueFactory<>("state"));

        clmnIssuesDateCreated.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<IssuesObs, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<IssuesObs, String> film) {
                        SimpleStringProperty property = new SimpleStringProperty();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        property.setValue(dateFormat.format(film.getValue().getCreatedAt()));
                        return property;
                    }
                });


        List<Issue> issues = gitApi.getIssues();
        ObservableList<IssuesObs> data = FXCollections.observableArrayList();
        for (int i = 0; i < issues.size(); i++) {
            data.add(new IssuesObs(issues.get(i).getNumber(), issues.get(i).getTitle(),
                    issues.get(i).getUser().getLogin(), issues.get(i).getCreatedAt(), issues.get(i).getState()));
        }

        tableIssues.setItems(data);
    }


    @Override
    public void closeRepository() {
        getuInterface().closeRepository();
    }

    @Override
    public void showAllFiles() {
        clmnPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        clmnIsBinary.setCellValueFactory(new PropertyValueFactory<>("isBinary"));
        clmnLOC.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));

        TableModel allFiles = getuInterface().getAllFiles();
        ObservableList<FilesObs> data = FXCollections.observableArrayList();
        for (int i = 0; i < allFiles.getRowCount(); i++) {
            data.add(new FilesObs((String) (allFiles.getValueAt(i, 0)),
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
        clmnAvtorFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));


        TableModel authors = getuInterface().getAuthors();
        ObservableList<AuthorObs> data = FXCollections.observableArrayList();

        for (int i = 0; i < authors.getRowCount(); i++) {
            String gitUsername = (String) (authors.getValueAt(i, 0));
            String gitEmail = (String) (authors.getValueAt(i, 5));
            data.add(new AuthorObs(gitUsername, (int) authors.getValueAt(i, 1),
                    (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4),
                    gitEmail, getFIO(gitUsername, gitEmail)));
        }

        avtorTable.setItems(data);
    }

    public String getFIO(String gitUsername, String gitEmail) {
        try {
            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(MysqlConnector.selectDeveloperFIO);
            preparedStatement.setString(1, gitEmail);
            ResultSet rs = MysqlConnector.executeQuery();

            while (rs.next()) {
                return rs.getString(1);
            }
            MysqlConnector.closeStmt();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    @Override
    public void showMainInf() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedStartDateTime = start.format(formatter);
        String formattedEndDateTime = end.format(formatter);

        lbNameRep.setText("Анализ репозитория с " + formattedStartDateTime + " по " + formattedEndDateTime);

        lbNazvCur.setText(getuInterface().getRepName());
        lbBranchCur.setText(getuInterface().getBranchName());
        lbKolCommitCur.setText(Integer.toString(getuInterface().getNumberOfCommits()));
        lbKolStrokCur.setText(Long.toString(getuInterface().getTotalNumberOfLines()));

        String repName = null;
        if (repositoryTable.getSelectionModel().getSelectedItem() != null) {
            String url = ((RepositoryObs) (repositoryTable.getSelectionModel().getSelectedItem())).getUrl();
            String[] parts = url.split("/");
            repName = parts[parts.length - 1];
            lbNameRep.setText(repName);
            lbNazvCur.setText(repName);
        }
        lbKolStrokAdd.setText(Long.toString(getuInterface().getTotalLinesAddedWithDate()));
        lbKolStrokDel.setText(Long.toString(getuInterface().getTotalLinesRemovedWithDate()));
    }

    public void showAllBranches() {
        // тек
        clmnTecName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnTecId.setCellValueFactory(new PropertyValueFactory<>("id"));

        ArrayList<Branch> branches = getuInterface().getListCurBranches();
        ObservableList<BranchesObs> data = FXCollections.observableArrayList();

        for (int i = 0; i < branches.size(); i++) {
            data.add(new BranchesObs(branches.get(i).getName(), branches.get(i).getId()));
        }

        tableTecBranches.setItems(data);

        //merged
        clmnMergedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnMergedId.setCellValueFactory(new PropertyValueFactory<>("id"));

        branches = getuInterface().getListMergedBranches();
        data = FXCollections.observableArrayList();

        for (int i = 0; i < branches.size(); i++) {
            data.add(new BranchesObs(branches.get(i).getName(), branches.get(i).getId()));
        }

        tableMergedBranches.setItems(data);
    }


    /**
     * Подгружаем внешний репозиторий
     * Для этого открываем соответствующеее диалоговое окно
     *
     * @param event - событие
     */
    public void OutRepButtonAction(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/stats/cloneRepView.fxml"));
            AnchorPane rootLayout = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Удалённый репозиторий");
            stage.setScene(new Scene(rootLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));

            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            CloneRepViewController controller = loader.getController();
            controller.setFxViewInterfaceController(this);

            stage.showAndWait();

            Platform.runLater(() -> btStart.requestFocus());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показать коммиты автора
     *
     * @param event - событие
     */
    public void ShowCommitsButtonAction(ActionEvent event) {
        if (isStart()) {
            AuthorObs tableAuthor = (AuthorObs) avtorTable.getSelectionModel().getSelectedItem();
            Author selectedAuthor = getuInterface().getAuthorByName(tableAuthor.getName());
            if (selectedAuthor == null) {
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
                controller.setLbName(selectedAuthor.getName());
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
    public void buildGraph(ActionEvent event) {
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

                HashMap<Author, ArrayList<Integer>> authorCommitsByWeek = this.getuInterface().getCommitsByWeek(allAvtors);
                numberLineChart = LineChartCreator.createNumberLineChart(selected, authorCommitsByWeek);

                AnchorPane.setLeftAnchor(numberLineChart, 0.0);
                AnchorPane.setTopAnchor(numberLineChart, 0.0);
                AnchorPane.setRightAnchor(numberLineChart, 0.0);
                AnchorPane.setBottomAnchor(numberLineChart, 53.0);
                GraphAnchor.getChildren().add(numberLineChart);

            } else if (selected.equals(LineChartCreator.options.get(1))) {
                HashMap<Author, ArrayList<Integer>> authorByDaysInCurMonth = this.getuInterface().getCommitsByDaysInCurMonth(allAvtors);
                numberLineChart = LineChartCreator.createNumberLineChart(selected, authorByDaysInCurMonth);

                AnchorPane.setLeftAnchor(numberLineChart, 0.0);
                AnchorPane.setTopAnchor(numberLineChart, 0.0);
                AnchorPane.setRightAnchor(numberLineChart, 0.0);
                AnchorPane.setBottomAnchor(numberLineChart, 53.0);
                GraphAnchor.getChildren().add(numberLineChart);

            } else if (selected.equals(LineChartCreator.options.get(2))) {
                HashMap<Author, ArrayList<Integer>> commitsByMonths = this.getuInterface().getCommitsByMonths(allAvtors);
                numberLineChart = LineChartCreator.createNumberLineChart(selected, commitsByMonths);

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

                HashMap<Author, HashMap<LocalDate, Integer>> commitsByCustomDate = this.getuInterface().getCommitsByCustomDate(allAvtors);
                dateLineChart = LineChartCreator.createDateLineChart(commitsByCustomDate, graphDatePickerStart.getValue(), graphDatePickerEnd.getValue());

                AnchorPane.setLeftAnchor(dateLineChart, 0.0);
                AnchorPane.setTopAnchor(dateLineChart, 0.0);
                AnchorPane.setRightAnchor(dateLineChart, 0.0);
                AnchorPane.setBottomAnchor(dateLineChart, 53.0);
                GraphAnchor.getChildren().add(dateLineChart);

            }
        }
    }
}
