package org.ams.repstats.controllers.stats;

import com.selesse.gitwrapper.myobjects.Author;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.repstats.MysqlConnector;
import org.ams.repstats.fortableview.AuthorTable;
import org.ams.repstats.fortableview.FilesTable;
import org.ams.repstats.fortableview.TeamTable;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.view.ViewInterfaceAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 30.04.2017
 * Time: 13:55
 */
public class StatsTeamController extends ViewInterfaceAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsTeamController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private TableView teamTable;
    @FXML
    private TableColumn teamNameClmn;
    @FXML
    private TableColumn teamTechnolClmn;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private MenuItem btAbout;
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
        teamTable.setEditable(false);
        showAllTeams();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                teamTable.requestFocus();
            }
        });
    }

    /**
     * Заполняем таблицу с командами
     */
    private void showAllTeams() {

        // region << Инициализируем колонки таблицы
        // Имя
        //Utils.configureStringColumnTeamTable(teamNameClmn,"name",MysqlConnector.updateNameTeam,LOGGER);
        teamNameClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("name"));

        // Технология
        //Utils.configureStringColumnTeamTable(teamTechnolClmn,"technology",MysqlConnector.updateTechnTeam,LOGGER);
        teamTechnolClmn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>("technology"));

        //endregion

        // Извлекаем данные из базы
        try {
            MysqlConnector.prepeareStmt(MysqlConnector.selectAllTeams);
            ResultSet rs = MysqlConnector.executeQuery();

            ObservableList<TeamTable> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new TeamTable(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)));
            }
            MysqlConnector.closeStmt();

            teamTable.setItems(data);
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

    /**
     * Кнопка начала анализа команд
     */
    public void startTeamAnylyze() {
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
