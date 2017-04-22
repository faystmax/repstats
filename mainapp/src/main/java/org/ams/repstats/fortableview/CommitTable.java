package org.ams.repstats.fortableview;

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
public class CommitTable {
    private SimpleStringProperty message;
    private ObjectProperty<ZonedDateTime> date = new SimpleObjectProperty<ZonedDateTime>();
    private SimpleIntegerProperty filesChanged;

    public CommitTable(String message, ZonedDateTime date, int filesChanged) {
        this.message = new SimpleStringProperty(message);
        this.date = new SimpleObjectProperty<ZonedDateTime>(date);
        this.filesChanged = new SimpleIntegerProperty(filesChanged);
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
}
