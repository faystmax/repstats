package org.ams.repstats.controllers;

import com.selesse.gitwrapper.myobjects.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.repstats.fortableview.AuthorTable;
import org.ams.repstats.fortableview.FilesTable;
import org.ams.repstats.view.ViewInterfaceAbstract;

import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 25.12.2016
 * Time: 16:00
 */
public class FXViewInterfaceController extends ViewInterfaceAbstract {

    //region << UI Компоненты
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
/*        choiceBox.setItems(FXCollections.observableArrayList("Git", "Svn"));
        choiceBox.setValue("Git");

        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue == "Git")
                        setUInterface(new GitUInterface());
                    else if (newValue == "Svn")
                        setUInterface(new SvnUInterface());
                });*/
    }

    // TODO  в будущем
    public void showChartOnImageView() {
        BufferedImage img = getuInterface().getChart();
        WritableImage wimg = new WritableImage(img.getWidth(), img.getHeight());
        SwingFXUtils.toFXImage(img, wimg);
        imageView.setImage(wimg);
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

        TableModel authors = getuInterface().getAuthors();
        ObservableList<AuthorTable> data = FXCollections.observableArrayList();
        for (int i = 0; i < authors.getRowCount(); i++) {
            data.add(new AuthorTable((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
                    (int) authors.getValueAt(i, 2), (int) authors.getValueAt(i, 3), (int) authors.getValueAt(i, 4)));
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

    }


    /**
     * Отображаем Окно с ошибкой
     *
     * @param title - заголовок
     * @param text - текст сообщения
     */
    private void ShowAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("errorIcon.png").toString()));
        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.setContentText(text);
        alert.showAndWait();

    }

    /**
     * Подгружаем внешний репозиторий
     * Для этого открываем соответствующеее диалоговое окно
     * @param event - событие
     */
    public void OutRepButtonAction(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/cloneRepView.fxml"));
            GridPane rootLayout = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Удалённый репозиторий");
            stage.setScene(new Scene(rootLayout, 365, 136));
            stage.getIcons().add(new Image("gitIcon.png"));

            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            CloneRepViewController controller = loader.getController();
            controller.setFxViewInterfaceController(this);

            stage.showAndWait();

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
                ShowAlert("Внимание", "Вы не выбрали автора!");
                return;
            }

            try {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("view/сommitsView.fxml"));
                AnchorPane rootLayout = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Коммиты");
                stage.setScene(new Scene(rootLayout, 480, 370));
                stage.getIcons().add(new Image("gitIcon.png"));
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
