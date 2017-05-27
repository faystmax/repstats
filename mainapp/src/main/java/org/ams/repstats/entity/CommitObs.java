package org.ams.repstats.entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.ZonedDateTime;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 20:55
 */
public class CommitObs {
    private SimpleIntegerProperty changeLines;
    private SimpleIntegerProperty linesAdded;
    private SimpleIntegerProperty linesRemoved;
    private SimpleStringProperty message;
    private ObjectProperty<ZonedDateTime> date = new SimpleObjectProperty<ZonedDateTime>();
    private SimpleIntegerProperty filesChanged;

    public CommitObs(String message, ZonedDateTime date, int filesChanged, int linesAdded, int linesRemoved) {
        this.message = new SimpleStringProperty(message);
        this.date = new SimpleObjectProperty<ZonedDateTime>(date);
        this.filesChanged = new SimpleIntegerProperty(filesChanged);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.changeLines = new SimpleIntegerProperty(linesAdded + Math.abs(linesRemoved));
        
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public ZonedDateTime getDate() {
        return date.get();
    }

    public ObjectProperty<ZonedDateTime> dateProperty() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date.set(date);
    }

    public int getFilesChanged() {
        return filesChanged.get();
    }

    public SimpleIntegerProperty filesChangedProperty() {
        return filesChanged;
    }

    public void setFilesChanged(int filesChanged) {
        this.filesChanged.set(filesChanged);
    }

    public int getLinesAdded() {
        return linesAdded.get();
    }

    public SimpleIntegerProperty linesAddedProperty() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded.set(linesAdded);
    }

    public int getLinesRemoved() {
        return linesRemoved.get();
    }

    public SimpleIntegerProperty linesRemovedProperty() {
        return linesRemoved;
    }

    public void setLinesRemoved(int linesRemoved) {
        this.linesRemoved.set(linesRemoved);
    }

    public int getChangeLines() {
        return changeLines.get();
    }

    public SimpleIntegerProperty changeLinesProperty() {
        return changeLines;
    }

    public void setChangeLines(int changeLines) {
        this.changeLines.set(changeLines);
    }
}
