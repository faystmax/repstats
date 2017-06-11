package org.ams.repstats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.ams.repstats.controllers.RootController;
import org.ams.repstats.controllers.stats.StatsRepositoryController;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.uinterface.UInterface;
import org.ams.repstats.utils.properties.EStartWindow;
import org.ams.repstats.utils.properties.MainProperties;
import org.ams.repstats.utils.properties.StartWindowSetter;
import org.ams.repstats.view.ConsoleViewInterface;
import org.ams.repstats.view.ViewInterface;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 25.12.2016
 * Time: 16:00
 */
public class MainApp extends Application {

    private static UInterfaceFactory factory = new UInterfaceFactory();         ///< фабрика для UInterface
    private static ViewInterface viewInterface;                                 ///< интерфейс - консоль/gui
    private static UInterface uInterface;                                       ///< мост для Фасада

    private Stage primaryStage;                                                 ///< главный каркас
    private BorderPane rootLayout;                                              ///< родительский Layout
    private FXMLLoader loader;                                                  ///< загрузчик xml

    /**
     * Переопределяем метод старта приложения
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Git Statistics");
        this.primaryStage.getIcons().add(new Image("icons/gitIcon.png"));


        initRootLayout(primaryStage);

        // Загрузка свойств
        MainProperties.loadProperties();

        // Конектимся к базе
        MysqlConnector.getConnection();

        // подгружаем репозитории
        MainProperties.loadRepositories();

        // В зависимости от информации в конфиге запускаем то или иное окно
        showStartWindow();
    }

    /**
     * Переопределяем метод закрытия приложения
     */
    @Override
    public void stop() {
        MainProperties.writeProperties();
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout(Stage primaryStage) {
        try {
            // Загружаем корневой макет из fxml файла.
            loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Посылаем в контроллер к rootLayout основной каркас приложения
            // для последующей смене сцен
            RootController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setRootLayout(rootLayout);

            // Отображаем сцену, содержащую корневой макет.
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показваем начальное окно
     */
    public void showStartWindow() {
        RootController controller = loader.getController();
        // В зависимости от свойств в конфиге запускаем то или иное окно
        if (StartWindowSetter.currentStartWindow == EStartWindow.REP) {
            controller.showStatsRepository();
        } else if (StartWindowSetter.currentStartWindow == EStartWindow.DEVELOPER) {
            controller.showStatsDeveloper();
        } else if (StartWindowSetter.currentStartWindow == EStartWindow.PROJECT) {
            controller.showStatsProject();
        } else if (StartWindowSetter.currentStartWindow == EStartWindow.TEAM) {
            controller.showStatsTeam();
        }
    }

    /**
     * Точка входа
     *
     * @param args - аргументы из командной строки
     */
    public static void main(String[] args) {
        if (args.length == 2 && args[0].equals("fx") && args[1].equals("analyzer")) {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new StatsRepositoryController();
            MainApp.viewInterface.setUInterface(MainApp.uInterface);
            launch(args);
        } else if (args.length == 2 && args[0].equals("c") && args[1].equals("analyzer")) {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new ConsoleViewInterface(MainApp.uInterface);
            MainApp.viewInterface.start();
        } else {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new StatsRepositoryController();
            MainApp.viewInterface.setUInterface(MainApp.uInterface);
            launch(args);
        }
    }
}