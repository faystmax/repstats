package org.ams.repstats.view;

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
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ams.repstats.fortableview.TableAuthor;
import org.ams.repstats.fortableview.TableFiles;
import org.ams.repstats.gui.AboutController;
import org.ams.repstats.gui.CloneRepViewController;
import org.ams.repstats.gui.CommitsController;
import org.ams.repstats.userinterface.GitUInterface;
import org.ams.repstats.userinterface.SvnUInterface;

import javax.swing.table.TableModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 25.12.2016
 * Time: 16:00
 */
public class FXViewInterface extends ViewInterfaceAbstract {
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
    private DirectoryChooser directoryChooser;
    File projectDir;


    @FXML
    public void initialize() {
        choiceBox.setItems(FXCollections.observableArrayList("Git", "Svn"));
        choiceBox.setValue("Git");

        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue == "Git")
                        setUInterface(new GitUInterface());
                    else if (newValue == "Svn")
                        setUInterface(new SvnUInterface());
                });
    }


    public void showChartOnImageView() {
        BufferedImage img = getuInterface().getChart();
        WritableImage wimg = new WritableImage(img.getWidth(), img.getHeight());
        SwingFXUtils.toFXImage(img, wimg);
        imageView.setImage(wimg);
    }

    public void ButtonAboutAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/about.fxml"));
            GridPane rootLayout = null;

            rootLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("О Программе");
            stage.setScene(new Scene(rootLayout, 345, 160));
            stage.getIcons().add(new Image("gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            AboutController controller = loader.getController();

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * выход
     *
     * @param event
     */
    public void ExitButtonAction(ActionEvent event) {
        System.exit(0);
    }


    /**
     * Кнопка выбора директории
     */
    @Override
    public void ChooseProjectAction() {
        directoryChooser = new DirectoryChooser();
        projectDir = directoryChooser.showDialog(null);
        //выход если ничего не выбрали
        if (projectDir == null) {
            tbProject.setText("");
            return;
        }
        if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath()) == true) {
            ShowAlert("Ошибка", "Выбрана неверная директория!");
            return;
        }
        if (projectDir != null) {
            setNewRepDirectory(projectDir);
        }

    }

    public void setNewRepDirectory(File file) {
        projectDir = file;
        tbProject.setText(file.getAbsolutePath());
    }

    /**
     * Кнопка начала работы
     */
    @Override
    public void start() {
        projectDir = new File(tbProject.getText());
        if (!getuInterface().сhooseProjectDirectory(projectDir.getAbsolutePath()) == true) {
            ShowAlert("Ошибка", "Выбрана неверная директория!");
            this.setStart(false);
            return;
        }
        if (!getuInterface().startProjectAnalyze() == true) {
            ShowAlert("Ошибка", "Ошибка анализа файлов проекта!");
            this.setStart(false);
        } else {
            this.setStart(true);
        }

        //showChartOnImageView();
        showMainInf();
        ShowAvtors();
        ShowAllFiles();
    }

    @Override
    public void ShowAllFiles() {
        clmnPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        clmnIsBinary.setCellValueFactory(new PropertyValueFactory<>("isBinary"));
        clmnLOC.setCellValueFactory(new PropertyValueFactory<>("numberOfLines"));

        TableModel allFiles = getuInterface().getAllFiles();
        ObservableList<TableFiles> data = FXCollections.observableArrayList();
        for (int i = 0; i < allFiles.getRowCount(); i++) {
            data.add(new TableFiles((String) (allFiles.getValueAt(i, 0)),
                    (String) allFiles.getValueAt(i, 1),
                    (String) allFiles.getValueAt(i, 2)));
        }

        tableAllFiles.setItems(data);
    }

    @Override
    public void ShowAvtors() {
        clmnAvtorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmnCommitCount.setCellValueFactory(new PropertyValueFactory<>("commitCount"));
        clmnLinesAdd.setCellValueFactory(new PropertyValueFactory<>("linesAdded"));
        clmnLinesDelete.setCellValueFactory(new PropertyValueFactory<>("linesRemoved"));
        clmnNetContribution.setCellValueFactory(new PropertyValueFactory<>("netContribution"));

        TableModel authors = getuInterface().getAuthors();
        ObservableList<TableAuthor> data = FXCollections.observableArrayList();
        for (int i = 0; i < authors.getRowCount(); i++) {
            data.add(new TableAuthor((String) (authors.getValueAt(i, 0)), (int) authors.getValueAt(i, 1),
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
     * @param title
     * @param text
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
     *
     * @param event
     */
    public void OutRepButtonAction(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/cloneRepView.fxml"));
            GridPane rootLayout = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Удалённый репозиторий");
            stage.setScene(new Scene(rootLayout, 365, 136));
            stage.getIcons().add(new Image("gitIcon.png"));

            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем
            CloneRepViewController controller = loader.getController();
            controller.setFxViewInterface(this);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * показать коммиты
     *
     * @param event
     */
    public void ShowCommitsButtonAction(ActionEvent event) {
        if (isStart()) {
            TableAuthor tableAuthor = (TableAuthor) avtorTable.getSelectionModel().getSelectedItem();
            Author selectedAuthor = getuInterface().getAuthorByName(tableAuthor.getName());
            if (selectedAuthor == null) {
                ShowAlert("Внимание", "Вы не выбрали автора!");
                return;
            }

            try {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("fxml/сommitsView.fxml"));
                GridPane rootLayout = loader.load();

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
