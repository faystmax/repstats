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
    public static BorderPane rootLayout;                                                ///< Layout

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    /**
     * Основной шаблон
     *
     * @param fxmlfile
     */
    public void setCenterFxml(String fxmlfile) {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource(fxmlfile));
            AnchorPane adminTeamView = (AnchorPane) loader.load();


            // Помещаем итерфейс  в центр корневого макета.
            rootLayout.setCenter(adminTeamView);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Администрирование -> Команда
     */
    public void showAdminTeam() {
        setCenterFxml("view/admin/teams/teamEditView.fxml");
    }

    /**
     * Администрирование -> Разработчики
     */
    public void showAdminDevelopers() {
        setCenterFxml("view/admin/developers/developersEditView.fxml");
    }

    /**
     * Администрирование -> Проекты
     */
    public void showAdminProjects() {
        setCenterFxml("view/admin/projects/projectEditView.fxml");
    }

    /**
     * Администрирование -> Репозитории
     */
    public void showAdminRepository() {
        setCenterFxml("view/admin/repository/repositoryEditView.fxml");
    }

    /**
     * Статистика -> Команда
     */
    public void showStatsTeam() {
        setCenterFxml("view/stats/statsTeamView.fxml");
    }

    /**
     * Статистика -> Разработчика
     */
    public void showStatsDeveloper() {
        setCenterFxml("view/stats/statsDeveloperView.fxml");
    }

    /**
     * Статистика -> по проекту
     */
    public void showStatsProject() {
        setCenterFxml("view/stats/statsProjectView.fxml");
    }

    /**
     * Статистика -> Репозитория
     */
    public void showStatsRepository() {
        setCenterFxml("view/stats/statsRepositoryView.fxml");
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
