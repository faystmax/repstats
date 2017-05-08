package org.ams.repstats.controllers.stats;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Branch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import org.ams.repstats.fortableview.BranchesTable;
import org.ams.repstats.fortableview.FilesTable;
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
import java.util.ArrayList;

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
    private Label lbKolStrok;
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
    @FXML
    private LineChart commitsChart;
    //endregion

    private DirectoryChooser directoryChooser;
    private File projectDir;


    @FXML
    public void initialize() {
        // По дефолту исп-ем гит
        this.setUInterface((new UInterfaceFactory()).create(TypeUInterface.git));

        // Отключаем редактирование таблицы
        repositoryTable.setEditable(false);

        // Крепим всвой placeholder
        Utils.setEmptyTableMessage(avtorTable);
        Utils.setEmptyTableMessage(tableAllFiles);
        Utils.setEmptyTableMessage(repositoryTable);

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
    }

    private void configureRepositoryClmn() {
        repositoryTable.setEditable(true);
        // Название
        reposNameClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("name"));

        // Url
        reposUrlClmn.setCellValueFactory(new PropertyValueFactory<RepositoryTable, String>("url"));
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

        // here runs the JavaFX thread
        // Boolean as generic parameter since you want to return it
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

                //выводим данные о репозитории в потоку javafx
                Platform.runLater(() -> {
                    showMainInf();
                    showAvtors();
                    showAllFiles();
                    showAllBranches();
                    showCommitsChart();
                    showChartOnImageView();
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
            Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                    " либо у вас отсутствует подключение к интернету");
            Utils.closeLoadingWindow();
        });
        new Thread(task).start();
    }

    /**
     * Кнопка начала анализа Репозитория
     */
    public void startRepositoryAnalyze() {
        if (repositoryTable.getSelectionModel().getSelectedItem() == null) {
            Utils.showAlert("Ошибка", "Сначала выберите репозиторий!");
            return;
        }

        // берём url
        String url = ((RepositoryTable) (repositoryTable.getSelectionModel().getSelectedItem())).getUrl();


        // here runs the JavaFX thread
        // Boolean as generic parameter since you want to return it
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() throws GitAPIException {
                // do your operation in here
                downloadRepository(url);
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

                //выводим данные о репозитории в потоку javafx
                Platform.runLater(() -> {
                    showMainInf();
                    showAvtors();
                    showAllFiles();
                    showAllBranches();
                    showCommitsChart();
                    showChartOnImageView();
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
            Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                    " либо у вас отсутствует подключение к интернету");
            Utils.closeLoadingWindow();
        });
        new Thread(task).start();
    }

    /**
     * Загрузка репозитория
     *
     * @param url - url репозитория
     * @throws GitAPIException - при ошибке скачивания
     */
    private void downloadRepository(String url) throws GitAPIException {
        File dir = new File("./cloneRep");
        if (dir.exists()) {
            setNewRepDirectory(null);
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
        getuInterface().closeRepository();
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
        clmnAvtorFIO.setCellValueFactory(new PropertyValueFactory<>("FIO"));



        TableModel authors = getuInterface().getAuthors();
        ObservableList<AuthorTable> data = FXCollections.observableArrayList();

        for (int i = 0; i < authors.getRowCount(); i++) {
            String gitUsername = (String) (authors.getValueAt(i, 0));
            String gitEmail = (String) (authors.getValueAt(i, 5));
            data.add(new AuthorTable(gitUsername, (int) authors.getValueAt(i, 1),
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


        lbNameRep.setText(getuInterface().getRepName());
        lbNazvCur.setText(getuInterface().getRepName());
        lbBranchCur.setText(getuInterface().getBranchName());
        lbKolCommitCur.setText(Integer.toString(getuInterface().getNumberOfCommits()));
        lbKolStrokCur.setText(Long.toString(getuInterface().getTotalNumberOfLines()));

        String repName = null;
        if (repositoryTable.getSelectionModel().getSelectedItem() != null) {
            String url = ((RepositoryTable) (repositoryTable.getSelectionModel().getSelectedItem())).getUrl();
            String[] parts = url.split("/");
            repName = parts[parts.length - 1];
            lbNameRep.setText(repName);
            lbNazvCur.setText(repName);
        }
        //getuInterface().getListAllBranches();
    }

    public void showAllBranches() {
        // тек
        clmnTecName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnTecId.setCellValueFactory(new PropertyValueFactory<>("id"));

        ArrayList<Branch> branches = getuInterface().getListCurBranches();
        ObservableList<BranchesTable> data = FXCollections.observableArrayList();

        for (int i = 0; i < branches.size(); i++) {
            data.add(new BranchesTable(branches.get(i).getName(), branches.get(i).getId()));
        }

        tableTecBranches.setItems(data);

        //merged
        clmnMergedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnMergedId.setCellValueFactory(new PropertyValueFactory<>("id"));

        branches = getuInterface().getListMergedBranches();
        data = FXCollections.observableArrayList();

        for (int i = 0; i < branches.size(); i++) {
            data.add(new BranchesTable(branches.get(i).getName(), branches.get(i).getId()));
        }

        tableMergedBranches.setItems(data);
    }

    public void showCommitsChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Месяц");

        //commitsChart = new LineChart<String,Number>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Все Коммиты");

        //
        final ArrayList<Integer> commitsByMonths = this.getuInterface().getCommitsByMonths();

        series.getData().add(new XYChart.Data("Янв", commitsByMonths.get(0)));
        series.getData().add(new XYChart.Data("Фев", commitsByMonths.get(1)));
        series.getData().add(new XYChart.Data("Мар", commitsByMonths.get(2)));
        series.getData().add(new XYChart.Data("Апр", commitsByMonths.get(3)));
        series.getData().add(new XYChart.Data("Май", commitsByMonths.get(4)));
        series.getData().add(new XYChart.Data("Июнь", commitsByMonths.get(5)));
        series.getData().add(new XYChart.Data("Июль", commitsByMonths.get(6)));
        series.getData().add(new XYChart.Data("Авг", commitsByMonths.get(7)));
        series.getData().add(new XYChart.Data("Сен", commitsByMonths.get(8)));
        series.getData().add(new XYChart.Data("Окт", commitsByMonths.get(9)));
        series.getData().add(new XYChart.Data("Ноя", commitsByMonths.get(10)));
        series.getData().add(new XYChart.Data("Дек", commitsByMonths.get(11)));

        commitsChart.getData().clear();
        commitsChart.getData().add(series);
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
            AuthorTable tableAuthor = (AuthorTable) avtorTable.getSelectionModel().getSelectedItem();
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
