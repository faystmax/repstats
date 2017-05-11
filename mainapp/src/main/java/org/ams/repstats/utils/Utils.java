package org.ams.repstats.utils;

import com.sun.istack.internal.NotNull;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ams.repstats.controllers.stats.LoadingController;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;

import java.io.*;
import java.util.Map;
import java.util.Optional;


/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 13:50
 */
public class Utils {

    public static Stage loadingStage;

    /**
     * Отображаем Диалоговое окно Alert
     *
     * @param title - заголовок
     * @param text  - текст сообщения
     */
    public static void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("icons/infIcon.png").toString()));
        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.setContentText(text);
        alert.showAndWait();

    }

    /**
     * Отображаем Диалоговое окно
     * с  подробностями об ошибки
     *
     * @param title       - заголовок
     * @param text        - текст сообщения
     * @param contentText - доп текст сообщения
     * @param ex          - сам Exception
     */
    public static void showError(String title, String text, String contentText, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.setContentText(contentText);
        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("icons/errorIcon.png").toString()));


        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static boolean conformationDialog(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("icons/questIcon.png").toString()));

        ButtonType buttonTypeOk = new ButtonType("Oк", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOk) {
            return true;
        } else {
            return false;
        }
    }

    public static void informationDialog(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        // Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("icons/infIcon.png").toString()));

        alert.showAndWait();
    }

    /**
     * Проверка строки на корректность ввода
     *
     * @param check
     * @return
     */
    public static boolean isValidStringValue(String check) {
        if (check.isEmpty() || check.length() > 300) {
            return false;
        }
        return true;
    }

    public static boolean isValidStringValue(Integer check) {
        if (check == null || check < 0) {
            return false;
        }
        return true;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    /**
     * Удаление дирректории рекурсивно
     *
     * @param path - путь для удаления
     */
    public static void deleteRecursive(File path) {
        try {
            Git git = Git.open(path);
            git.getRepository().close();

        } catch (IOException e) {
        }
        path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    pathname.listFiles(this);
                    pathname.delete();
                } else {
                    pathname.delete();
                }
                return false;
            }
        });
        path.delete();
    }

    /**
     * Загрузка репозитория
     *
     * @param githubRemoteUrl Remote git http url which ends with .git.
     * @param branchName      Name of the branch which should be downloaded
     * @param destinationDir  Destination directory where the downloaded files should be present.
     * @return
     * @throws Exception
     */
    public static boolean downloadRepoContent(@NotNull String githubRemoteUrl, @NotNull String branchName, @NotNull String destinationDir) throws Exception {


        File destinationFile = new File(destinationDir);
        //delete any existing file
        FileUtils.deleteDirectory(destinationFile);
        Git call = Git.cloneRepository().setURI(githubRemoteUrl)
                .setBranch(branchName)
                .setDirectory(destinationFile)
                .call();
        call.getRepository().close();
        if (destinationFile.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Открытие окна загрузки
     * @param task
     */
    public static void openLoadingWindow(Task<Boolean> task) {
        try {
            //Запуск анализа - открытие окна загрузки
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Utils.class.getClassLoader().getResource("view/stats/loadingView.fxml"));
            AnchorPane aboutLayout = loader.load();

            loadingStage = new Stage(StageStyle.TRANSPARENT);
            loadingStage.setTitle("Прогресс");
            loadingStage.setScene(new Scene(aboutLayout));
            loadingStage.getIcons().add(new Image("icons/loadIcon.png"));
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            LoadingController controller = loader.getController();
            controller.setTask(task);

            //Инициализируем и запускаем
            loadingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Закрытие окна загрузки
     */
    public static void closeLoadingWindow() {
        loadingStage.close();
    }

    /**
     * Установка лейбла
     *
     * @param tableView label
     */
    public static void setEmptyTableMessage(TableView tableView) {
        tableView.setPlaceholder(new Label("Данные отсутствуют"));
    }
}

