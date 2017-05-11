package org.ams.repstats.controllers.stats;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
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
    private Task<Boolean> task;
    //endregion

    @FXML
    public void initialize() {
    }


    public void cancelAction(ActionEvent event) {
        task.cancel(true);
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }

    public void setTask(Task<Boolean> task) {
        this.task = task;
    }
}
