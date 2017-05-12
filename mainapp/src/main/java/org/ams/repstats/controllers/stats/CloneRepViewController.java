package org.ams.repstats.controllers.stats;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.ams.repstats.utils.RepositoryDownloader;
import org.ams.repstats.utils.Utils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                public Boolean call() throws GitAPIException {
                    // do your operation in here
                    try {
                        File destinationFile = RepositoryDownloader.downloadRepoContent(tbURL.getText(), "master");
                        Platform.runLater(() -> {
                            fxViewInterfaceController.setNewRepDirectory(destinationFile);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showAlert("Ошибка", "Ошибка загрузки репозитория!");
                    }

                    return true;
                }
            };

            task.setOnRunning((e) -> {
                Platform.runLater(() -> {
                    pbDownload.setProgress(-1);
                });
            });
            task.setOnSucceeded((e) -> {
                Platform.runLater(() -> {
                    pbDownload.setProgress(1);
                    isStart = false;

                });
            });
            task.setOnFailed((e) -> {
                // eventual error handling by catching exceptions from task.get
                task.getException().printStackTrace();
                Platform.runLater(() -> {
                    pbDownload.setProgress(0);
                    isStart = false;

                });
                Utils.showAlert("Ошибка", "Введённый вами репозиторий не существует," +
                        " либо у вас отсутствует подключение к интернету");
            });
            new Thread(task).start();

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
}
