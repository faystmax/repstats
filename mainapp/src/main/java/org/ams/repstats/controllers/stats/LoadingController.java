package org.ams.repstats.controllers.stats;

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
    //endregion

    @FXML
    public void initialize() {
    }

    /**
     * Закрытие окна
     */
    public void closeStage() {
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }
}
