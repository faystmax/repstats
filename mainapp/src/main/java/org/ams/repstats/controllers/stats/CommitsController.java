package org.ams.repstats.controllers.stats;

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
import org.ams.repstats.fortableview.DeveloperTable;
import org.ams.repstats.fortableview.ProjectTable;
import org.ams.repstats.fortableview.RepositoryTable;
import org.ams.repstats.userinterface.UInterface;
import org.ams.repstats.utils.Utils;

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
    private TableColumn clmnChangeLine;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
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
    private ProjectTable projectTable;
    private RepositoryTable repositoryTable;
    private DeveloperTable developerTable;

    @FXML
    public void initialize() {
        configureTableColumn();

        // Крепим свой placeholder
        Utils.setEmptyTableMessage(tableForCommits);
    }

    private void configureTableColumn() {
        clmnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        clmnDate.setCellValueFactory(
                date -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    property.setValue(dateFormat.format(Date.from(date.getValue().getDate().toInstant())));
                    return property;
                });
        clmnFilesChanged.setCellValueFactory(new PropertyValueFactory<>("filesChanged"));
        clmnChangeLine.setCellValueFactory(new PropertyValueFactory<>("changeLines"));
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setUInterface(UInterface uInterface) {
        this.uInterface = uInterface;
    }

    public void setLbName(String name) {
        lbName.setText("Коммиты " + name);
    }

    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) btExit.getScene().getWindow();
        stage.close();
    }

    public void setProjectTable(ProjectTable projectTable) {
        this.projectTable = projectTable;
    }

    public void setRepositoryTable(RepositoryTable repositoryTable) {
        this.repositoryTable = repositoryTable;
    }

    public void setDeveloperTable(DeveloperTable developerTable) {
        this.developerTable = developerTable;
    }

    /**
     * Основной метод отображение коммитов
     */
    public void showCommits() {

        Collection<Commit> commits;
        if (this.projectTable != null) {
            commits = this.projectTable.getCommits();
        } else if (this.repositoryTable != null) {
            commits = this.repositoryTable.getCommits();
        } else if (this.developerTable != null) {
            commits = this.developerTable.getCommits();
        } else {
            commits = uInterface.getLastCommits(author);
        }

        if (commits != null) {
            ObservableList<CommitTable> data = FXCollections.observableArrayList();
            for (Commit commit : commits) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.YYYY");

                if (datePickerStart.getValue() != null && datePickerEnd.getValue() != null) {
                    if (commit.getCommitDateTime().toLocalDate().isAfter(datePickerStart.getValue().minusDays(1)) &&
                            commit.getCommitDateTime().toLocalDate().isBefore(datePickerEnd.getValue().plusDays(1)))
                        this.addCommit(data, commit);
                } else if (datePickerStart.getValue() != null) {
                    if (commit.getCommitDateTime().toLocalDate().isAfter(datePickerStart.getValue().minusDays(1)))
                        this.addCommit(data, commit);
                } else if (datePickerEnd.getValue() != null) {
                    if (commit.getCommitDateTime().toLocalDate().isBefore(datePickerEnd.getValue().plusDays(1)))
                        this.addCommit(data, commit);
                } else {
                    this.addCommit(data, commit);
                }

            }
            tableForCommits.setItems(data);
        }
    }

    private void addCommit(ObservableList<CommitTable> data, Commit commit) {
        data.add(new CommitTable(commit.getCommitMessage(),
                commit.getCommitDateTime(),
                commit.getFilesChanged().size(),
                commit.getLinesAdded(),
                commit.getLinesRemoved()));
    }


}
