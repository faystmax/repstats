package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 11:29
 */
public class TeamTable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty technology;

    public TeamTable() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.technology = new SimpleStringProperty();
    }

    public TeamTable(int id, String name, String technology) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.technology = new SimpleStringProperty(technology);
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
}
