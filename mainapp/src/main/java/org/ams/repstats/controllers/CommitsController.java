package org.ams.repstats.controllers;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Commit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.ams.repstats.fortableview.CommitTable;
import org.ams.repstats.userinterface.UInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 20:12
 */
public class CommitsController {

    //region << UI Компоненты
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private Label lbName;
    @FXML
    private TableColumn clmnFilesChanged;
    @FXML
    private TableColumn<CommitTable, String> clmnDate;
    @FXML
    private TableColumn clmnMessage;
    @FXML
    private Button btExit;
    @FXML
    private TableView tableForCommits;
    //endregion

    private Author author;
    private UInterface uInterface;

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setUInterface(UInterface uInterface) {
        this.uInterface = uInterface;
    }

    public void setLbName(String name) {
        lbName.setText("Коммиты " + name);
    }

    public void showCommits() {


        Collection<Commit> commits = uInterface.getLastCommits(author);

        clmnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        //clmnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmnDate.setCellValueFactory(
                date -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    property.setValue(dateFormat.format(Date.from(date.getValue().getDate().toInstant())));
                    return property;
                });
        clmnFilesChanged.setCellValueFactory(new PropertyValueFactory<>("filesChanged"));
        if (commits != null) {
            ObservableList<CommitTable> data = FXCollections.observableArrayList();
            for (Commit commit : commits) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.YYYY");

                if (datePickerStart.getValue() != null) {

                    if (commit.getCommitDateTime().toLocalDate().isAfter(datePickerStart.getValue()))
                        data.add(new CommitTable(commit.getCommitMessage(),
                                commit.getCommitDateTime(),
                                commit.getFilesChanged().size()));
                } else {
                    data.add(new CommitTable(commit.getCommitMessage(),
                            commit.getCommitDateTime(),
                            commit.getFilesChanged().size()));
                }

            }

            tableForCommits.setItems(data);

        }
    }

    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

}
