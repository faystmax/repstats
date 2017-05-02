package org.ams.repstats.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.ams.repstats.controllers.mytask.MyDownloadRepTask;
import org.ams.repstats.controllers.stats.StatsRepositoryController;
import org.ams.repstats.utils.Utils;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(CloneRepViewController.class); ///< ссылка на логер

    //region <<<  элементы UI
    @FXML
    public ProgressBar pbDownload;
    @FXML
    public Button btExit;
    @FXML
    private Button btChoose;
    @FXML
    private TextField tbURL;
    //endregion

    private StatsRepositoryController fxViewInterfaceController;    ///< ссылка на родителя
    private boolean isStart = false;                                ///< флаг начала

    @FXML
    public void initialize() {
        // wherever you assign event handlers...
        tbURL.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                chooseButtonAction(null);
            }
        });
        btExit.defaultButtonProperty().bind(btExit.focusedProperty());
        btChoose.defaultButtonProperty().bind(btChoose.focusedProperty());
        Platform.runLater(() -> tbURL.requestFocus());
        Platform.runLater(() -> tbURL.selectEnd());
    }

    /**
     * Инициализация родительского окна
     * Для последующей
     *
     * @param fxViewInterfaceController -контроллер род. окна
     */
    public void setFxViewInterfaceController(StatsRepositoryController fxViewInterfaceController) {
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
                 *  Task для подкачки  внешнего репозитория
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
        Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                " либо у вас отсутствует подключение к интернету");
        isStart = false;
        Platform.runLater(() -> btExit.requestFocus());
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





}
