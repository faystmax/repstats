package org.ams.repstats.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Сущность для отображения в таблице
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 11:29
 */
public class TeamObs {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty technology;
    private SimpleIntegerProperty count;

    public TeamObs() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.technology = new SimpleStringProperty();
        this.count = new SimpleIntegerProperty();
    }

    public TeamObs(int id, String name, String technology) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.technology = new SimpleStringProperty(technology);
    }

    public TeamObs(int id, String name, String technology, int count) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.technology = new SimpleStringProperty(technology);
        this.count = new SimpleIntegerProperty(count);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public String getTechnology() {
        return technology.get();
    }

    public SimpleStringProperty technologyProperty() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology.set(technology);
    }

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }
}
