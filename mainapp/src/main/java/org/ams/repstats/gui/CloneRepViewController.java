package org.ams.repstats.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.ams.repstats.mytask.myTask;
import org.ams.repstats.view.FXViewInterface;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 02.01.2017
 * Time: 16:00
 */
public class CloneRepViewController {

    @FXML
    public ProgressBar pbDownload;
    @FXML
    private Button btExit;
    @FXML
    private Button btChoose;
    @FXML
    private TextField tbURL;

    //Ссылка на родительский контроллер
    private FXViewInterface fxViewInterface;    ///< ссылка на родителя
    private boolean isStart = false;            ///< флаг начала

    /**
     * Task для подкачки  внешнего репозитория
     */
    private myTask task;


    /**
     * Инициализация родительского окна
     * Для последующей
     *
     * @param fxViewInterface
     */
    public void setFxViewInterface(FXViewInterface fxViewInterface) {
        this.fxViewInterface = fxViewInterface;
    }

    /**
     * закрытие окна
     *
     * @param event
     */
    public void ExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * @param event
     */
    public void ChooseButtonAction(ActionEvent event) {
        if (isStart == false) {
            isStart = true;
            try {
                pbDownload.setProgress(-1.0);
                File dir = new File("./cloneRep");
                if (dir.exists()) {
                    fxViewInterface.setNewRepDirectory(null);
                    deleteRecursive(dir);
                }
                task = new myTask(this, fxViewInterface, tbURL);
                pbDownload.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Удаление дирректории рекурсивно
     *
     * @param path
     */
    public void deleteRecursive(File path) {
        try {
            Git git = Git.open(path);
            git.getRepository().close();
            this.fxViewInterface.closeRepository();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    pathname.listFiles(this);
                    pathname.delete();
                } else {
                    pathname.delete();
                }
                return false;
            }
        });
        path.delete();
    }

    /**
     * Отображаем Окно с ошибкой
     *
     * @param title
     * @param text
     */
    public void showAlert(String title, String text) {
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
}
