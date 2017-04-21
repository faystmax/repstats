package org.ams.repstats.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.ams.repstats.controllers.mytask.MyDownloadRepTask;
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

    //region <<<  элементы UI
    @FXML
    public ProgressBar pbDownload;
    @FXML
    private Button btExit;
    @FXML
    private Button btChoose;
    @FXML
    private TextField tbURL;
    //endregion

    private FXViewInterfaceController fxViewInterfaceController;    ///< ссылка на родителя
    private boolean isStart = false;                                ///< флаг начала


    /**
     * Инициализация родительского окна
     * Для последующей
     *
     * @param fxViewInterfaceController -контроллер род. окна
     */
    public void setFxViewInterfaceController(FXViewInterfaceController fxViewInterfaceController) {
        this.fxViewInterfaceController = fxViewInterfaceController;
    }

    /**
     * закрытие окна
     *
     * @param event - событие
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    /**
     * @param event - событие
     */
    public void chooseButtonAction(ActionEvent event) {
        if (!isStart) {
            isStart = true;
            try {
                File dir = new File("./cloneRep");
                if (dir.exists()) {
                    fxViewInterfaceController.setNewRepDirectory(null);
                    deleteRecursive(dir);
                }
                /*
      Task для подкачки  внешнего репозитория
     */
                MyDownloadRepTask task = new MyDownloadRepTask(this, fxViewInterfaceController, tbURL);
                pbDownload.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ошибка подкачки репозитория
     */
    public void downloadError() {
        this.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                " либо у вас отсутствует подключение к интернету");
        isStart = false;
    }


    /**
     * Удаление дирректории рекурсивно
     *
     * @param path - путь для удаления
     */
    public void deleteRecursive(File path) {
        try {
            Git git = Git.open(path);
            git.getRepository().close();
            this.fxViewInterfaceController.closeRepository();
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
     * @param title - заголовок
     * @param text - текст ошибки
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
