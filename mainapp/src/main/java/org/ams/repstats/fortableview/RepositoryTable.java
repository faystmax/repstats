package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 27.04.2017
 * Time: 9:00
 */
public class RepositoryTable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty url;
    private SimpleObjectProperty<Date> dateOfCreation;
    private SimpleIntegerProperty idDeveloperResponsible;
    private SimpleStringProperty FIO;
    private SimpleStringProperty description;
    private SimpleIntegerProperty id_project_repository;

    public RepositoryTable(int id, String name, String url, Date dateOfCreation, int idDeveloperResponsible, String FIO, String description, int id_project_repository) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.url = new SimpleStringProperty(url);
        this.dateOfCreation = new SimpleObjectProperty<Date>(dateOfCreation);
        this.idDeveloperResponsible = new SimpleIntegerProperty(idDeveloperResponsible);
        this.FIO = new SimpleStringProperty(FIO);
        this.description = new SimpleStringProperty(description);
        this.id_project_repository = new SimpleIntegerProperty(id_project_repository);
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

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public Date getDateOfCreation() {
        return dateOfCreation.get();
    }

    public SimpleObjectProperty<Date> dateOfCreationProperty() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation.set(dateOfCreation);
    }

    public int getIdDeveloperResponsible() {
        return idDeveloperResponsible.get();
    }

    public SimpleIntegerProperty idDeveloperResponsibleProperty() {
        return idDeveloperResponsible;
    }

    public void setIdDeveloperResponsible(int idDeveloperResponsible) {
        this.idDeveloperResponsible.set(idDeveloperResponsible);
    }

    public String getFIO() {
        return FIO.get();
    }

    public SimpleStringProperty FIOProperty() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO.set(FIO);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getId_project_repository() {
        return id_project_repository.get();
    }

    public SimpleIntegerProperty id_project_repositoryProperty() {
        return id_project_repository;
    }

    public void setId_project_repository(int id_project_repository) {
        this.id_project_repository.set(id_project_repository);
    }
}
