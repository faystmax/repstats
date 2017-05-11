package org.ams.repstats.fortableview;

import com.selesse.gitwrapper.myobjects.Author;
import com.selesse.gitwrapper.myobjects.Commit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

    //доп. поля
    private ArrayList<String> urls = new ArrayList<String>();
    private SimpleIntegerProperty commitCount;
    private SimpleIntegerProperty linesAdd;
    private SimpleIntegerProperty linesDelete;
    private SimpleIntegerProperty netContribution;
    private Author author;

    private Collection<Commit> commits = new ArrayList<Commit>();

    public ProjectTable(int id, String name, Date dateStart, Date deadline, int prior) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.dateStart = new SimpleObjectProperty<Date>(dateStart);
        this.deadline = new SimpleObjectProperty<Date>(deadline);
        this.prior = new SimpleIntegerProperty(prior);
        commitCount = new SimpleIntegerProperty(0);
        linesAdd = new SimpleIntegerProperty(0);
        linesDelete = new SimpleIntegerProperty(0);
        netContribution = new SimpleIntegerProperty(0);
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

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public int getCommitCount() {
        return commitCount.get();
    }

    public SimpleIntegerProperty commitCountProperty() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount.set(commitCount);
    }

    public int getLinesAdd() {
        return linesAdd.get();
    }

    public SimpleIntegerProperty linesAddProperty() {
        return linesAdd;
    }

    public void setLinesAdd(int linesAdd) {
        this.linesAdd.set(linesAdd);
    }

    public int getLinesDelete() {
        return linesDelete.get();
    }

    public SimpleIntegerProperty linesDeleteProperty() {
        return linesDelete;
    }

    public void setLinesDelete(int linesDelete) {
        this.linesDelete.set(linesDelete);
    }

    public int getNetContribution() {
        return netContribution.get();
    }

    public SimpleIntegerProperty netContributionProperty() {
        return netContribution;
    }

    public void setNetContribution(int netContribution) {
        this.netContribution.set(netContribution);
    }

    public Collection<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Collection<Commit> commits) {
        this.commits = commits;
    }

    public void addCommitCount(int commitCount) {
        this.commitCount.set(this.commitCount.get() + commitCount);
    }

    public void addLinesAdd(int linesAdd) {
        this.linesAdd.set(this.linesAdd.get() + linesAdd);
    }

    public void addLinesDelete(int linesDelete) {
        this.linesDelete.set(this.linesDelete.get() + linesDelete);
    }

    public void addNetContributiont(int netContribution) {
        this.netContribution.set(this.netContribution.get() + netContribution);
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}

