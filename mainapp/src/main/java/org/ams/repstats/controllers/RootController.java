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
    public static BorderPane rootLayout;                                                      ///< Layout

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
            loader.setLocation(this.getClass().getClassLoader().getResource("view/admin/teams/teamEditView.fxml"));
            AnchorPane adminTeamView = (AnchorPane) loader.load();


            // Помещаем итерфейс  в центр корневого макета.
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
            loader.setLocation(this.getClass().getClassLoader().getResource("view/admin/developers/developersEditView.fxml"));
            AnchorPane adminDevelopersView = (AnchorPane) loader.load();

            // Помещаем итерфейс  в центр корневого макета.
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
            loader.setLocation(this.getClass().getClassLoader().getResource("view/admin/projects/projectEditView.fxml"));
            AnchorPane adminProjectsView = (AnchorPane) loader.load();

            // Помещаем итерфейс  в центр корневого макета.
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
            loader.setLocation(this.getClass().getClassLoader().getResource("view/admin/repository/repositoryEditView.fxml"));
            AnchorPane adminRepositoryView = (AnchorPane) loader.load();

            // Помещаем итерфейс в центр корневого макета.
            rootLayout.setCenter(adminRepositoryView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Окно "О программе"
     */
    public void buttonAboutAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/aboutView.fxml"));
            AnchorPane aboutLayout = loader.load();

            Stage stage = new Stage();
            stage.setTitle("О Программе");
            stage.setScene(new Scene(aboutLayout));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);

            //Инициализируем и запускаем
            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Статистика -> Команда
     */
    public void showStatsTeam() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/stats/statsTeamView.fxml"));
            AnchorPane statsTeamView = (AnchorPane) loader.load();


            // Помещаем итерфейс  в центр корневого макета.
            rootLayout.setCenter(statsTeamView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Статистика -> Разработчика
     */
    public void showStatsDeveloper() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/stats/statsDeveloperView.fxml"));
            AnchorPane statsDeveloperView = (AnchorPane) loader.load();


            // Помещаем итерфейс в центр корневого макета.
            rootLayout.setCenter(statsDeveloperView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Статистика -> по проекту
     */
    public void showStatsProject() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/stats/statsProjectView.fxml"));
            AnchorPane statsProjectView = (AnchorPane) loader.load();


            // Помещаем итерфейс  в центр корневого макета.
            rootLayout.setCenter(statsProjectView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Статистика -> Репозитория
     */
    public void showStatsRepository() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/stats/statsRepositoryView.fxml"));
            AnchorPane statsRepositoryView = (AnchorPane) loader.load();


            // Помещаем итерфейс в центр корневого макета.
            rootLayout.setCenter(statsRepositoryView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Файл -> Настройки
     */
    public void showSettings() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/settingsView.fxml"));
            AnchorPane statsRepositoryView = (AnchorPane) loader.load();

            //Создаём новую сцену
            Stage stage = new Stage();
            stage.setTitle("Настройки");
            stage.setScene(new Scene(statsRepositoryView));
            stage.getIcons().add(new Image("icons/gitIcon.png"));
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
    public void exitButtonAction(ActionEvent event) {
        System.exit(0);
    }
}
