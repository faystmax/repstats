package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 23.04.2017
 * Time: 21:47
 */
public class ProjectTable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleObjectProperty<Date> dateStart;
    private SimpleObjectProperty<Date> deadline;
    private SimpleIntegerProperty prior;

    public ProjectTable(int id, String name, Date dateStart, Date deadline, int prior) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.dateStart = new SimpleObjectProperty<Date>(dateStart);
        this.deadline = new SimpleObjectProperty<Date>(deadline);
        this.prior = new SimpleIntegerProperty(prior);
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

    public Date getDateStart() {
        return dateStart.get();
    }

    public SimpleObjectProperty<Date> dateStartProperty() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart.set(dateStart);
    }

    public Date getDeadline() {
        return deadline.get();
    }

    public SimpleObjectProperty<Date> deadlineProperty() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline.set(deadline);
    }

    public int getPrior() {
        return prior.get();
    }

    public SimpleIntegerProperty priorProperty() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior.set(prior);
    }

    public String getDateStartAsString() {
        SimpleDateFormat smp = new SimpleDateFormat("dd MMMMM yyyy");
        String strDate = (null == dateStart || null == dateStart.get())
                ? "" : smp.format(dateStart.get());

        return strDate;
    }

    public String getDateDeadlineAsString() {
        SimpleDateFormat smp = new SimpleDateFormat("dd MMMMM yyyy");
        String strDate = (null == deadline || null == deadline.get())
                ? "" : smp.format(deadline.get());

        return strDate;
    }
}

