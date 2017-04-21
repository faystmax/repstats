package org.ams.repstats.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 19.04.2017
 * Time: 17:27
 */
public class RootController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class); ///< ссылка на логер
    private Stage primaryStage;                                                         ///< Главный каркас
    private BorderPane rootLayout;                                                      ///< Layout

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    /**
     * Администрирование -> Команда
     */
    public void showAdminTeam() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/teamEditView.fxml"));
            AnchorPane adminTeamView = (AnchorPane) loader.load();


            // Помещаем итерфейс для редактирования команд в центр корневого макета.
            rootLayout.setCenter(adminTeamView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Администрирование -> Разработчики
     */
    public void showAdminDevelopers() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/developersEditView.fxml"));
            AnchorPane adminDevelopersView = (AnchorPane) loader.load();

            // Помещаем итерфейс для редактирования разработчиков в центр корневого макета.
            rootLayout.setCenter(adminDevelopersView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Администрирование -> Проекты
     */
    public void showAdminProjects() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/projectEditView.fxml"));
            AnchorPane adminProjectsView = (AnchorPane) loader.load();

            // Помещаем итерфейс для редактирования пректов в центр корневого макета.
            rootLayout.setCenter(adminProjectsView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Администрирование -> Репозитории
     */
    public void showAdminRepository() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/repositoryEditView.fxml"));
            AnchorPane adminRepositoryView = (AnchorPane) loader.load();

            // Помещаем итерфейс для редактирования репозиториев в центр корневого макета.
            rootLayout.setCenter(adminRepositoryView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Окно "О программе"
     */
    public void ButtonAboutAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/aboutView.fxml"));
            AnchorPane aboutLayout = null;

            aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("О Программе");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
