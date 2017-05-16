package org.ams.repstats.fortableview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 14.05.2017
 * Time: 18:43
 */
public class PullRequestTable {

    private SimpleIntegerProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty name;
    private ObjectProperty<Date> createdAt = new SimpleObjectProperty<Date>();
    private SimpleStringProperty state;
    private SimpleStringProperty isMerged;
    private SimpleIntegerProperty changedFiles;
    private SimpleIntegerProperty additions;
    private SimpleIntegerProperty deletions;

    public PullRequestTable(int number, String title, String author, Date createdAt, String state) {
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.name = new SimpleStringProperty(author);
        this.createdAt = new SimpleObjectProperty<Date>(createdAt);
        this.state = new SimpleStringProperty(state);
    }

    public PullRequestTable(int number, String title, String author, Date createdAt, String state, String isMerged,
                            int changedFiles, int additions, int deletions) {
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.name = new SimpleStringProperty(author);
        this.createdAt = new SimpleObjectProperty<Date>(createdAt);
        this.state = new SimpleStringProperty(state);
        this.isMerged = new SimpleStringProperty(isMerged);

        this.changedFiles = new SimpleIntegerProperty(changedFiles);
        this.additions = new SimpleIntegerProperty(additions);
        this.deletions = new SimpleIntegerProperty(deletions);

    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Date getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<Date> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt.set(createdAt);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public int getChangedFiles() {
        return changedFiles.get();
    }

    public SimpleIntegerProperty changedFilesProperty() {
        return changedFiles;
    }

    public void setChangedFiles(int changedFiles) {
        this.changedFiles.set(changedFiles);
    }

    public int getAdditions() {
        return additions.get();
    }

    public SimpleIntegerProperty additionsProperty() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions.set(additions);
    }

    public int getDeletions() {
        return deletions.get();
    }

    public SimpleIntegerProperty deletionsProperty() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions.set(deletions);
    }

    public String getIsMerged() {
        return isMerged.get();
    }

    public SimpleStringProperty isMergedProperty() {
        return isMerged;
    }

    public void setIsMerged(String isMerged) {
        this.isMerged.set(isMerged);
    }
}
