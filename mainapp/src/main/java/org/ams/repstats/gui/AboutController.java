package org.ams.repstats.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 08.01.2017
 * Time: 16:58
 */
public class AboutController {
    @FXML
    private Button btExit;

    public void ExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }
}