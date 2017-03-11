package org.ams.repstats.gui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.ams.repstats.view.FXViewInterface;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileFilter;

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
    private FXViewInterface fxViewInterface;
    private boolean isStart = false;

    Task task = new Task<Void>() {
        @Override
        public Void call() {
            try {
                Git git = Git.cloneRepository()
                        .setURI(tbURL.getText())
                        .setDirectory(new File("./cloneRep"))
                        .call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
            fxViewInterface.setNewRepDirectory(new File("./cloneRep"));
            updateProgress(1, 1);
            this.done();
            return null;
        }
    };


    public void setFxViewInterface(FXViewInterface fxViewInterface) {
        this.fxViewInterface = fxViewInterface;
    }

    public void ExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    public void ChooseButtonAction(ActionEvent event) {
        if (isStart == false) {
            isStart = true;
            try {
                pbDownload.setProgress(-1.0);
                //delete(new File("./cloneRep"));
                File dir = new File("./cloneRep");
                if (dir.exists()) {
                    deleteRecursive(dir);
                }
                pbDownload.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteRecursive(File path) {
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
     * Рекурсивное удаление папки и файлов в ней
     *
     * @param file
     */
    public void delete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                delete(f);
            file.delete();
        } else {
            file.delete();
        }
    }


}
