package org.ams.repstats.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 30.04.2017
 * Time: 20:43
 */
public class LoadingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private ProgressBar progressBar;
    //endregion

    Task task;

    @FXML
    public void initialize() {
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void startTask() {
        new Thread(task).start();
    }

    /**
     * Закрытие окна
     */
    public void closeStage() {
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }

    public void downloadError(Exception e) {
        Utils.showError("Ошибка скачивания", "Не удалось скачать репозиторий", "", e);
        closeStage();
    }
}
