package org.ams.repstats.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.ams.repstats.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 19.04.2017
 * Time: 17:27
 */
public class RootController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class); ///< ссылка на логер


    //region << UI Компоненты
    @FXML
    private MenuBar menu;
    @FXML
    private MenuItem menuSettings;
    @FXML
    private MenuItem menuClose;
    @FXML
    private MenuItem menuStatsTeam;
    @FXML
    private MenuItem menuStatsDeveloper;
    @FXML
    private MenuItem menuStatsProject;
    @FXML
    private MenuItem menuStatsRep;
    @FXML
    private MenuItem menuAdminTeam;
    @FXML
    private MenuItem menuAdminDevelop;
    @FXML
    private MenuItem menuAdminProject;
    @FXML
    private MenuItem menuAdminRep;
    @FXML
    private Menu menuHelp;
    //endregion

    private Stage primaryStage;                             ///< Главный каркас
    public static BorderPane rootLayout;                    ///< Layout
    public String username = new String();                  ///< Логин админа
    public String password = new String();                  ///< Пароль админа


    @FXML
    public void initialize() {
        menuSettings.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        ImageView imageViewIcon = new ImageView(new Image(this.getClass().getClassLoader().getResource("icons/settings.png").toString()));
        imageViewIcon.setFitHeight(20);
        imageViewIcon.setFitWidth(20);
        menuSettings.setGraphic(imageViewIcon);
    }

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
        if (checkAdminPermission()) {
            setCenterFxml("view/admin/teams/teamEditView.fxml");
        }
    }

    /**
     * Администрирование -> Разработчики
     */
    public void showAdminDevelopers() {
        if (checkAdminPermission()) {
            setCenterFxml("view/admin/developers/developersEditView.fxml");
        }
    }

    /**
     * Администрирование -> Проекты
     */
    public void showAdminProjects() {
        if (checkAdminPermission()) {
            setCenterFxml("view/admin/projects/projectEditView.fxml");
        }
    }

    /**
     * Администрирование -> Репозитории
     */
    public void showAdminRepository() {
        if (checkAdminPermission()) {
            setCenterFxml("view/admin/repository/repositoryEditView.fxml");
        }
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
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.close();
    }

    /**
     * Проверка на администратора
     *
     * @return true если верный ввод, false иначе
     */
    public boolean checkAdminPermission() {
        if (!username.equals("admin") || !password.equals("admin")) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Окно авторизации");
            dialog.setHeaderText("Введите логин и пароль администратора");

            // Set the icon (must be included in the project).
            dialog.setGraphic(new ImageView(this.getClass().getClassLoader().getResource("icons/login.png").toString()));

            // Get the Stage.
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            // Add a custom icon.
            stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("icons/lock.png").toString()));


            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Вход", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField username = new TextField();
            username.setPromptText("Username");
            PasswordField password = new PasswordField();
            password.setPromptText("Password");

            grid.add(new Label("Логин:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Пароль:"), 0, 1);
            grid.add(password, 1, 1);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> username.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), password.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get().getKey().equals("admin") && result.get().getValue().equals("admin")) {
                    this.username = result.get().getKey();
                    this.password = result.get().getValue();
                    return true;
                } else {
                    Utils.showAlert("Ошибка", "Неверный логин или пароль!");
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

}
