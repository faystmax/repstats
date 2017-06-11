package org.ams.repstats.entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Сущность для отображения в таблице
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 14.05.2017
 * Time: 23:00
 */
public class IssuesObs {
    private SimpleIntegerProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty name;
    private ObjectProperty<Date> createdAt = new SimpleObjectProperty<Date>();
    private SimpleStringProperty state;

    public IssuesObs(int number, String title, String name, Date createdAt, String state) {
        this.number = new SimpleIntegerProperty(number);
        this.title = new SimpleStringProperty(title);
        this.name = new SimpleStringProperty(name);
        this.createdAt = new SimpleObjectProperty<Date>(createdAt);
        this.state = new SimpleStringProperty(state);
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
}
