package org.ams.repstats.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 19.04.2017
 * Time: 17:27
 */
public class RootController {

    /**
     * Окно "О программе"
     */
    public void ButtonAboutAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/about.fxml"));
            GridPane rootLayout = null;

            rootLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("О Программе");
            stage.setScene(new Scene(rootLayout));
            stage.getIcons().add(new Image("gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем и запускаем
            AboutController controller = loader.getController();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Закрываем приложение
     *
     * @param event - событие
     */
    public void ExitButtonAction(ActionEvent event) {
        System.exit(0);
    }
}
