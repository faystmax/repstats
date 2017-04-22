package org.ams.repstats;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;


/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 13:50
 */
public class Utils {
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
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("errorIcon.png").toString()));
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
        stage.getIcons().add(new Image(Utils.class.getClassLoader().getResource("errorIcon.png").toString()));


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
        alert.setHeaderText(text);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
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
        if (check < 0) {
            return false;
        }
        return true;
    }

    /*
    public static void configureStringColumnTeamTable(TableColumn tableColumn, String propertyName, String querry,Logger LOGGER ){
        tableColumn.setCellValueFactory(new PropertyValueFactory<TeamTable, String>(propertyName));
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TeamTable, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TeamTable, String> t) {
                        //обновляем в базе
                        TeamTable changeable = ((TeamTable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        //провверка ввода
                        if(!Utils.isValidStringValue(t.getNewValue())){
                            Utils.showAlert("Ошибка ввода!","Неверное значение поля");
                            changeable.setName(t.getOldValue());
                            // обновляем колонку
                            tableColumn.setVisible(false);
                            tableColumn.setVisible(true);
                            return;
                        }
                        try {
                            PreparedStatement preparedStatement = MysqlConnector.prepeareStmt(querry);
                            preparedStatement.setString(1, t.getNewValue());
                            preparedStatement.setInt(2, changeable.getId());
                            int newId = MysqlConnector.executeUpdate();
                            changeable.setTechnology(t.getNewValue());
                        } catch (SQLException e) {
                            LOGGER.error((e.getMessage()));
                            Utils.showError("Ошибка Изминения", "Невозможно изменить выбранную запись!",
                                    "", e);
                        }
                    }
                }
        );

    }*/

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
