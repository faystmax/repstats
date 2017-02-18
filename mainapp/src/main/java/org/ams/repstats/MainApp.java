package org.ams.repstats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.ams.repstats.uifactory.TypeUInterface;
import org.ams.repstats.uifactory.UInterfaceFactory;
import org.ams.repstats.userinterface.UInterface;
import org.ams.repstats.view.ConsoleViewInterface;
import org.ams.repstats.view.FXViewInterface;
import org.ams.repstats.view.ViewInterface;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 25.12.2016
 * Time: 16:00
 */
public class MainApp extends Application {

    private static ViewInterface viewInterface;
    private static UInterface uInterface;            //мост для Фасада
    private static UInterfaceFactory factory = new UInterfaceFactory();

    public MainApp() {
        uInterface = factory.create(TypeUInterface.git);
        viewInterface = new FXViewInterface();
        viewInterface.setUInterface(uInterface);
    }

    public MainApp(ViewInterface viewInterface, UInterface uInterface) {
        MainApp.viewInterface = viewInterface;
        MainApp.uInterface = uInterface;
        MainApp.viewInterface.setUInterface(MainApp.uInterface);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Git Statistics");
        primaryStage.getIcons().add(new Image("gitIcon.png"));
        initRootLayout(primaryStage);
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout(Stage primaryStage) {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/view.fxml"));
            GridPane rootLayout = loader.load();

            // Отображаем сцену, содержащую корневой макет.
            primaryStage.setScene(new Scene(rootLayout, 900, 500));
            ViewInterface controller = loader.getController();
            controller.setUInterface(MainApp.uInterface);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args.length == 2 && args[0].equals("fx") && args[1].equals("analyzer")) {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new FXViewInterface();
            MainApp.viewInterface.setUInterface(MainApp.uInterface);
            launch(args);
        } else if (args.length == 2 && args[0].equals("c") && args[1].equals("analyzer")) {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new ConsoleViewInterface(MainApp.uInterface);
            MainApp.viewInterface.start();
        } else {
            MainApp.uInterface = factory.create(TypeUInterface.git);
            MainApp.viewInterface = new FXViewInterface();
            MainApp.viewInterface.setUInterface(MainApp.uInterface);
            launch(args);
        }
    }

}