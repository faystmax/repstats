package org.ams.repstats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.ams.repstats.controllers.RootController;
import org.ams.repstats.controllers.stats.StatsRepositoryController;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.userinterface.UInterface;
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

    private static UInterfaceFactory factory = new UInterfaceFactory();         ///< Фабрика для UInterface
    private static ViewInterface viewInterface;                                 ///< Bнтерфейс - консоль,gui
    private static UInterface uInterface;                                       ///< Vост для Фасада

    private Stage primaryStage;                                                 ///< Главный каркас
    private BorderPane rootLayout;                                              ///< Родительский Layout

    @Override
    public void start(Stage primaryStage) {
        //setUserAgentStylesheet(STYLESHEET_CASPIAN);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Git Statistics");
        this.primaryStage.getIcons().add(new Image("gitIcon.png"));

        // TODO настройку открытия определённого окна при загрузке прилжения
        initRootLayout(primaryStage);
        showStats();
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout(Stage primaryStage) {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
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

            //Конектимся к базе
            MysqlConnector.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает в корневом статистику
     */
    public void showStats() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("view/stats/statsRepositoryView.fxml"));
            AnchorPane statsView = (AnchorPane) loader.load();

            // Ставим интерфейс в зависимости от моста
            ViewInterface controller = loader.getController();
            controller.setUInterface(MainApp.uInterface);

            // Помещаем сведения об адресатах в центр корневого макета.
            rootLayout.setCenter(statsView);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Возвращает главную сцену.
     *
     * @return - главную сцену
     */
    public Stage getPrimaryStage() {
        return primaryStage;
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