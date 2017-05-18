package org.ams.repstats.controllers.stats;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 18.05.2017
 * Time: 16:13
 */
public class DateForTeamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateForTeamController.class); ///< ссылка на логер

    //region << UI Компоненты
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private Button btExit;
    //endregion

    private StatsTeamController statsTeamController;

    @FXML
    public void initialize() {
        datePickerStart.setValue(LocalDate.now().minusDays(1));
        datePickerEnd.setValue(LocalDate.now());
    }

    public void startButtonAction(ActionEvent event) {
        if (datePickerStart.getValue() == null || datePickerEnd.getValue() == null) {
            Utils.showAlert("Ошибка", "Выберите промежуток времени!");
            return;
        }

        if (datePickerStart.getValue().isAfter(datePickerEnd.getValue())) {
            Utils.showAlert("Ошибка", "Выберите корректный  промежуток времени!");
            return;
        }

        statsTeamController.start = datePickerStart.getValue();
        statsTeamController.end = datePickerEnd.getValue();
        exitButtonAction(null);
    }

    /**
     * Закрытие окна
     */
    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    public void setStatsTeamController(StatsTeamController statsTeamController) {
        this.statsTeamController = statsTeamController;
    }
}
