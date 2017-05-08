package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.05.2017
 * Time: 16:36
 */
public class BranchesTable {

    private SimpleStringProperty name;
    private SimpleStringProperty id;

    public BranchesTable(String name, String id) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
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

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
